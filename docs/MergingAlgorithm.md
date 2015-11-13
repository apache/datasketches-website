---
layout: doc_page
---

<h3>Theta Sketch Merging (Union) Algorithm Notes</h3>
There is a fairly simple argument for the correctness of our
space-limited multiway merging algorithm.

1. Let R be the result of the theta union algorithm as described in the 
<a href="https://github.com/DataSketches/DataSketches.github.io/blob/master/docs/ThetaSketchFramework.pdf">Theta Sketch Framework</a> paper. 
It is the set of all hashes less than the minimum of the
thetas of all of the input sketches.  Recall that there is no size
limit on this "full-size" answer R.

2. We will spell out the official definition of "theta union with cutback", which 
establishes the 1-Goodness of its implicit TCF, and hence its unbiasedness.

3. We will prove that the cut-back answer according to this official
definition can be computed from the X smallest elements of the 
full-sized result R, for some value of X that doesn't need to
be spelled out in this high-level overview of the argument.

4. We assume the existence of a gadget G that keeps at least the X
smallest elements of any set that you feed into it.

5. We point out that if A and B are any two sets such that each element
of B is bigger than any element of A, and if (A U B) is fed into G,
then G's output, which by definition contains at least the X smallest
elements of (A U B), also contains at least the X smallest elements of A.

6. Finally, we point out that our main merging loop feeds all elements of
R into G, and also some other elements, each bigger than any element
of R. [These larger elements are inserted because for a while we only have
an upper bound on the final value of theta.] By (5), the output of G 
contains at least the X smallest elements of R. By (3), we are able to
construct the answer conforming to (2).

----------------------------------------------------------------------

Note: to avoid extreme verbosity in the above, the phrase 
"the X smallest elements of S" 
is used as an abbreviation for the more accurate phrase 
"the X smallest elements of S if |S| >= X, otherwise all of S".

