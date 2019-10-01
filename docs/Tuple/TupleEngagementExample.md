---
layout: doc_page
---

# Tuple Sketch Engagement Example


## The Challenge : Measuring Customer Engagement
When customers visit our websites, blogs, or stores it is very useful to understand how engaged they are with us and our products.  There are many ways to characterize customer engagement, but one common way is to understand how frequently our customers are returning to visit.

For example, let's study the following histogram:

<img class="doc-img-full" src="{{site.docs_img_dir}}/tuple/EngagementHistogram.png" alt="EngagementHistogram.png" />

The X-axis is the number of days that a specific customer (identified by some unique ID) visits our site in a 30 day period.

The Y-axis is the number of distinct visitors (customers) that have visited our site Y number of times during the 30 day period. 

Reading this histogram we can see that about 100 distinct visitors visited our site exactly one day out of the 30 day period. About 11 visitors visited our site on 5 different days of the 30 day period. And, it seems that we have one customer that visited our site every day of the 30 day period!  We certainly want to encourage more of these loyal customers.

Different businesses will have different overall time periods of interest and different resolutions for the repeat visit intervals. They can be over years, months, weeks or days, hours or even minutes.  It is up to the business to decide what time intervals are of interest to measure. What we show here is clearly a made-up example to convey the concept.

So how do we do this?  Especially, how can we do this efficiently, quickly, and suitable for near-real-time results?

Well, we have a sketching solution for that!

## The Input Stream
The input data we need to create the above histogram can be viewed as a stream of tuples, where each tuple as at least two components, a time-stamp and an unique identifier (ID) that is a proxy for a customer or visitor.  In real systems, the tuples may have many other attributes, but for our purposes here, we only need these two.  The stream of tuples might be a live stream flowing in a network, or data being streamed from storage.  It doesn't matter.  

In order for a sketch to work properly it must see all relevant data for a particular day, domain or dimensional coordinates that that particular sketch is assigned to represent. Sketches are mergeable, thus parallelizable, which means that the domain can be partitioned into many substreams feeding separate sketches. At the appropriate time the substream sketches can be merged together into a single sketch to provide a snapshot-in-time analysis of the whole domain.

It is critical to emphasize that the input stream must not be pre-sampled (for example, a 10% random sample) as this will seriously impact the accuracy of any estimates derived from the sketch.  It is perfectly fine to pre-filter the input stream to remove robot traffic, for example, which will totally remove that traffic from the analysis.

## Duplicates
We want our customers to come back and visit us many times, which will create tuples with duplicate IDs in the stream.  This is a good thing, but for this analysis we need to handle duplicate ID's in two different ways that we separate by two different stages of the analysis.

### Stage 1: Fine-grain interval sketching
In our example our fine-grain interval is a day and the overall interval is 30 days.  In the first stage we want to process all the tuples for one day in a way that ultimately results in a single sketch for that day.  This may mean many sketches operating in parallel to process all the records for one day, but they are ultimately merged down to a single sketch representing all the data for one day.  

Since we want to analyze data for 30 days, at the end of Stage 1, we will have 30 sketches representing each of the 30 days.

In this first stage we only want to count visits by any one customer __once__ for a single day, even if a customer visits us multiple times during that day. Thus, we want to ignore any duplicate occurrences of the same ID within the same day.

### Stage 2: Merge and count across days
Once we have our 30 day sketches, we merge all 30 sketches together into one final sketch. This time, however, we want to count the number of duplicates that occur for any single ID across different days.  This will give us the number of days that any unique ID appeared across all 30 days.

## The IntegerSketch and Helper classes

For help us code our example we will leverage the [IntegerSketch Package](https://github.com/apache/incubator-datasketches-java/tree/master/src/main/java/org/apache/datasketches/tuple/aninteger) from the library. This package consists of 5 classes, the _IntegerSketch_ and 4 helper classes, all of which extend generic classes of the parent _tuple_ package.  Normally, the user/developer would develop these 5 classes to solve a particular analysis problem. These 5 classes can serve as an example of how to create your own Tuple Sketch solutions and we will use them to solve our customer engagement problem.

Please refer to the [Tuple Overview](https://datasketches.github.io/docs/Tuple/TupleOverview.html) section on this website for a quick review of how the Tuple Sketch works. 

### IntegerSketch class
```java
public class IntegerSketch extends UpdatableSketch<Integer, IntegerSummary> { ... }
```
The IntegerSketch class extends the generic UpdatableSketch specifying two type parameters, an Integer and an IntegerSummary.

The Integer type specifies the data type that will update the IntegerSummary.  The IntegerSummary specifies the structure of the summary field and what rules to use when updating the field with an Integer type.

```java
 public IntegerSketch(final int lgK, final IntegerSummary.Mode mode) {
    super(1 << lgK, ResizeFactor.X8.ordinal(), 1.0F, new IntegerSummaryFactory(mode));
  }
```

This first constructor takes an integer and a Mode.  The integer _lgK_ is a parameter that impacts the maximum size of the sketch object both in memory and when stored, and specifies what the accuracy of the sketch will be.  The larger the value the larger the sketch and the more accurate it will be. The "lg" in front of the "K" is a shorthand for Log_base2. This parameter must be an integer beweeen 4 and 26, with 12 being a typical value.  With the value 12, there will be up to 2^12 = 4096 possible rows retained by the sketch where each row consists of a key and a summary field.  In theory, the summary field can be anything, but for our example it is just a single integer. 

We will not be using the second constructor.

```java
  @Override
  public void update(final String key, final Integer value) {
    super.update(key, value);
  }

  @Override
  public void update(final long key, final Integer value) {
    super.update(key, value);
  }
```
The IntegerSketch has two update methods, one for String keys and an Integer value and the other for long keys and an Integer value.
The user system code would call one of these two methods to update the sketch.  In our example, we will call the second update method with an integer value representing a user ID and a value of one for the Integer.  The key will be hashed and passed to the internal sketching algorithm that will determine if the key-value pair should be retained by the sketch or not.  If it is retained, the 2nd parameter will be passed to the IntegerSummary class for handling.   

### IntegerSummary class
```java
public class IntegerSummary implements UpdatableSummary<Integer> {
  private int value_;
  private final Mode mode_;
  ...
```
The _IntegerSummary_ class is central to understanding how tuple sketches work in general and how we will configure it for our example.
  
The IntegerSummary class extends the generic UpdatableSummary specifying one parameter, Integer, the data type that will update this summary. This summary object is very simple. It has one updatable value field of type _int_ and a _final_ Mode field, which tells this summary object the rule to use when updating _value_.

```java
  /**
   * The aggregation modes for this Summary
   */
  public static enum Mode {

    /**
     * The aggregation mode is the summation function.
     * <p>New retained value = previous retained value + incoming value</p>
     */
    Sum,

    /**
     * The aggregation mode is the minimum function.
     * <p>New retained value = min(previous retained value, incoming value)</p>
     */
    Min,

    /**
     * The aggregation mode is the maximum function.
     * <p>New retained value = max(previous retained value, incoming value)</p>
     */
    Max,

    /**
     * The aggregation mode is always one.
     * <p>New retained value = 1</p>
     */
    AlwaysOne
  }
```
The _Mode_ enum defines the different rules that can be used when updating the summary.  In this case we have four rules: Sum, Min, Max, and AlwaysOne.  For our example, we will only use Sum and AlwaysOne. There is only one public constructor which specifies the mode that we wish to use.  The _getValue()_ method allows us to extract the value of the summary when the sketching is done.


```java
  @Override
  public void update(final Integer value) {
    switch (mode_) {
    case Sum:
      value_ += value;
      break;
    case Min:
      if (value < value_) { value_ = value; }
      break;
    case Max:
      if (value > value_) { value_ = value; }
      break;
    case AlwaysOne:
      value_ = 1;
      break;
    }
  }
```
This method is called by the sketch algorithms to update the summary with the value provided by the IntegerSketch update method described above. This is the code that implements the aggregation rules specified by the Mode. 


### IntegerSummarySetOperations class
This class allows us to define different updating rules for two different set operations: _Union_ and _Intersection_.  In this context "Union" is synonymous with "merge".  In our example we will only use the Union set operation.  

It is important to note here that this set operations class also uses the mode updating logic of the IntegerSummary class. These updating modes can be different than the mode used when the IntegerSummary is used with the IntegerSketch class.

### IntegerSummaryFactory class
This class is only called by the underlying sketch code when a new key-value pair needs to be retained by the sketch and a new empty Summary needs to be associated with the new key, and the new summary may need to be updated by the incoming value.

### IntegerSummaryDeserializer class
This class is only called by the underlying sketch code when deserializing a sketch and its summaries from a stored image.  We will not be using this class in our example.

## The [EngagementTest](https://github.com/apache/incubator-datasketches-java/blob/master/src/test/java/org/apache/datasketches/tuple/aninteger/EngagementTest.java) class
Note 1: the version in the GitHub master is more up-to-date than the version of this class in the 1.1.0-incubating release. This tutorial references the code in master.

Note 2: You can run the following _computeEngagementHistogram()_ method as a test, but in order to see the output you will need to un-comment the printf(...) statement at the very end of the class.


```java
  @Test
  public void computeEngagementHistogram() {
    int lgK = 12;
    int K = 1 << lgK; // = 4096
    int days = 30;
    int v = 0;
    IntegerSketch[] skArr = new IntegerSketch[days];
    for (int i = 0; i < days; i++) {
      skArr[i] = new IntegerSketch(lgK, AlwaysOne);
    }
    for (int i = 0; i <= days; i++) { //31 generating indices
      int numIds = numIDs(days, i);
      int numDays = numDays(days, i);
      int myV = v++;
      for (int d = 0; d < numDays; d++) {
        for (int id = 0; id < numIds; id++) {
          skArr[d].update(myV + id, 1);
        }
      }
      v += numIds;
    }

    int numVisits = unionOps(K, Sum, skArr);
    assertEquals(numVisits, 897);
  }
```
This little engagement test uses a power-law distribution of number of days visited versus the number of visitors in order to model what actual data might look like.  It is not essential to understand how the data is generated, but if you are curious it will be discussed at the end.

In lines 7 - 9, we create a simple array of 30 sketches for the 30 days.  Note that we set the update mode to _AlwaysOne_. (Because this little test does not generate any duplicates in the first stage, the mode _Sum_ would also work.)

The triple-nested for-loops update the 30 sketches using a pair of parametric generating functions discussed later. Line 23 passes the array of sketches to the _unionOps(...)_ method, which will output the results.  The returned _numVisits_ is just for testing.

```java
  private static int unionOps(int K, IntegerSummary.Mode mode, IntegerSketch ... sketches) {
    IntegerSummarySetOperations setOps = new IntegerSummarySetOperations(mode, mode);
    Union<IntegerSummary> union = new Union<>(K, setOps);
    int len = sketches.length;

    for (IntegerSketch isk : sketches) {
      union.update(isk);
    }
    CompactSketch<IntegerSummary> result = union.getResult();
    SketchIterator<IntegerSummary> itr = result.iterator();

    int[] freqArr = new int[len +1];

    while (itr.next()) {
      int value = itr.getSummary().getValue();
      freqArr[value]++;
    }
    println("Engagement Histogram:");
    printf("%12s,%12s\n","Days Visited", "Visitors");
    int sumVisitors = 0;
    int sumVisits = 0;
    for (int i = 0; i < freqArr.length; i++) {
      int visits = freqArr[i];
      if (visits == 0) { continue; }
      sumVisitors += visits;
      sumVisits += (visits * i);
      printf("%12d,%12d\n", i, visits);
    }
    println("Total Visitors: " + sumVisitors);
    println("Total Visits  : " + sumVisits);
    return sumVisits;
  }
```
In the unionOps method, line 2 initializes the _IntegerSummarySetOperations_ class with the given mode, which for our example must be _Sum_. Line 3 creates a new Union class initialized with the setOps class.

In lines 6-8 the union is updated with all of the sketches from the array.

In lines 9-10, the result is obtained from the union as a _CompactSketch_ and a _SketchIterator_ is obtained from the result so we can process all the retained rows of the sketch.

In lines 12-16, we accumulate the frequencies of occurences of rows with the same count value.

The remainder of the method is just the mechanics of printing out the results to the console, which should look like this:

```
Days Visited,    Visitors
           1,         102
           2,          77
           3,          30
           4,          15
           5,          11
           6,           5
           7,           4
           8,           4
           9,           3
          10,           3
          11,           3
          12,           2
          14,           2
          15,           2
          17,           2
          19,           2
          21,           1
          24,           1
          27,           1
          30,           1
Total Visitors: 271
Total Visits  : 897
```

This is the data that is plotted as a histogram at the top of this tutorial.

## Generating the Synthetic Data
This section is for folks interested in how the data for this example was generated.  This is optional reading.

Much of the data we analyze from the Internet has the classical "long-tailed" distribution or a power-law distribution.  When frequencies of occurences of some type is plotted on a log-log axis we tend to get a negatively sloping, mostly-straight line.  There have been numerous books and papers written about this phenomenon, but it is quite real and any analysis tools used to analyze big data should take this into account.

For this example, it was useful to generate data that at least had some resemblence to what a user might actually experience with their own data.

To plot a straight line on a graph we use the familiar ![](<img src="/docs/Tuple/tex/4b83ab92144c8118c40653189ab60df5.svg?invert_in_darkmode&sanitize=true" align=middle width=81.54091274999998pt height=22.831056599999986pt/>) formula from high school, where <img src="/docs/Tuple/tex/0e51a2dede42189d77627c4d742822c3.svg?invert_in_darkmode&sanitize=true" align=middle width=14.433101099999991pt height=14.15524440000002pt/> is the slope and <img src="/docs/Tuple/tex/4bdc8d9bcfb35e1c9bfb51fc69687dfc.svg?invert_in_darkmode&sanitize=true" align=middle width=7.054796099999991pt height=22.831056599999986pt/> is the y-intercept.  In our example, we want the line to start at the maximum number of days, <img src="/docs/Tuple/tex/2103f85b8b1477f430fc407cad462224.svg?invert_in_darkmode&sanitize=true" align=middle width=8.55596444999999pt height=22.831056599999986pt/>, and have a negative slope so our equation becomes <img src="/docs/Tuple/tex/6135f95a4c5190c1fb32a940218b7e49.svg?invert_in_darkmode&sanitize=true" align=middle width=95.82751364999999pt height=22.831056599999986pt/>.

As we noted above, we actually want this to be a straight line on a log-log graph, so each of the variables <img src="/docs/Tuple/tex/deceeaf6940a8c7a5a02373728002b0f.svg?invert_in_darkmode&sanitize=true" align=middle width=8.649225749999989pt height=14.15524440000002pt/>, <img src="/docs/Tuple/tex/332cc365a4987aacce0ead01b8bdcc0b.svg?invert_in_darkmode&sanitize=true" align=middle width=9.39498779999999pt height=14.15524440000002pt/> and <img src="/docs/Tuple/tex/2103f85b8b1477f430fc407cad462224.svg?invert_in_darkmode&sanitize=true" align=middle width=8.55596444999999pt height=22.831056599999986pt/> are actually log variables.  Now our equation becomes

<p align="center"><img src="/docs/Tuple/tex/ee2e5d9da4d950906fa92096b6429b1c.svg?invert_in_darkmode&sanitize=true" align=middle width=170.48488049999997pt height=14.611878599999999pt/></p>.

To transform this into a pair of parametric equations we perform the following operations. First we multiply through by <img src="/docs/Tuple/tex/4cce784506b1a2a25ec2e0fcda57d07b.svg?invert_in_darkmode&sanitize=true" align=middle width=59.74332374999999pt height=24.65753399999998pt/> to isolate <img src="/docs/Tuple/tex/2103f85b8b1477f430fc407cad462224.svg?invert_in_darkmode&sanitize=true" align=middle width=8.55596444999999pt height=22.831056599999986pt/> by itself

<p align="center"><img src="/docs/Tuple/tex/e3ddb2192b160548cd8cdd50d78798a5.svg?invert_in_darkmode&sanitize=true" align=middle width=172.2816183pt height=37.0084374pt/></p>

Then we insert our parametric variable <img src="/docs/Tuple/tex/77a3b857d53fb44e33b53e4c8b68351a.svg?invert_in_darkmode&sanitize=true" align=middle width=5.663225699999989pt height=21.68300969999999pt/>, which will vary from zero to <img src="/docs/Tuple/tex/2103f85b8b1477f430fc407cad462224.svg?invert_in_darkmode&sanitize=true" align=middle width=8.55596444999999pt height=22.831056599999986pt/>:

<p align="center"><img src="/docs/Tuple/tex/0ae2a195b9e9ba508c93b2d941164738.svg?invert_in_darkmode&sanitize=true" align=middle width=199.86247544999998pt height=37.0084374pt/></p>

solving for both <img src="/docs/Tuple/tex/332cc365a4987aacce0ead01b8bdcc0b.svg?invert_in_darkmode&sanitize=true" align=middle width=9.39498779999999pt height=14.15524440000002pt/> and <img src="/docs/Tuple/tex/deceeaf6940a8c7a5a02373728002b0f.svg?invert_in_darkmode&sanitize=true" align=middle width=8.649225749999989pt height=14.15524440000002pt/> separately on both sides gives

<p align="center"><img src="/docs/Tuple/tex/8c6a4860368623a37d240d1a41783b15.svg?invert_in_darkmode&sanitize=true" align=middle width=76.58257364999999pt height=19.8789426pt/></p>
<p align="center"><img src="/docs/Tuple/tex/6d62acd36363beecb55558641342120e.svg?invert_in_darkmode&sanitize=true" align=middle width=90.61807424999999pt height=17.57997945pt/></p>

These are continuous functions and when plotted we can see our negative sloping plot (here <img src="/docs/Tuple/tex/a08aa3f720eb59983ccb69372a8b620d.svg?invert_in_darkmode&sanitize=true" align=middle width=44.56994024999999pt height=21.18721440000001pt/>) starting at <img src="/docs/Tuple/tex/ab7c502ff4006d6fafd521bbc80ba537.svg?invert_in_darkmode&sanitize=true" align=middle width=47.00525609999998pt height=21.18721440000001pt/> and ending at <img src="/docs/Tuple/tex/53f72531c4ccb08b88a6764fc49b5cf4.svg?invert_in_darkmode&sanitize=true" align=middle width=47.75103794999999pt height=21.18721440000001pt/>. The parametric variable <img src="/docs/Tuple/tex/77a3b857d53fb44e33b53e4c8b68351a.svg?invert_in_darkmode&sanitize=true" align=middle width=5.663225699999989pt height=21.68300969999999pt/> varies from 0 to 30, inclusively.

<img class="doc-img-half" src="{{site.docs_img_dir}}/tuple/ContinuousLogLog.png" alt="ContinuousLogLog.png" />

This, of course, results in non-integer coordinates, which is not what we want. Descretizing the equations becomes

<p align="center"><img src="/docs/Tuple/tex/5f611a59c6f07a05ea5008ffb5f1b62d.svg?invert_in_darkmode&sanitize=true" align=middle width=133.86404295pt height=20.7921813pt/></p>
<p align="center"><img src="/docs/Tuple/tex/b5c1239bff09265ac536ff7335a16cc0.svg?invert_in_darkmode&sanitize=true" align=middle width=147.8995452pt height=21.68956845pt/></p>

Produces

<img class="doc-img-half" src="{{site.docs_img_dir}}/tuple/DiscreteLogLog.png" alt="DiscreteLogLog.png" />

Note that these plots are symmetric about the faint 45 degree line.

The points on this graph represent the intermediate data used to generate the final data fed to the sketches.

