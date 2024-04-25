---
layout: doc_page
---
<!--
    Licensed to the Apache Software Foundation (ASF) under one
    or more contributor license agreements.  See the NOTICE file
    distributed with this work for additional information
    regarding copyright ownership.  The ASF licenses this file
    to you under the Apache License, Version 2.0 (the
    "License"); you may not use this file except in compliance
    with the License.  You may obtain a copy of the License at

      http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing,
    software distributed under the License is distributed on an
    "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
    KIND, either express or implied.  See the License for the
    specific language governing permissions and limitations
    under the License.
-->
### Apache Pinot Integration
[Apache Pinot](https://pinot.apache.org/) has built-in support for most major sketch families from Apache Datasketches as aggregation and transformation functions in its SQL dialect.

Example: 
```sql
select distinctCountThetaSketch(
  sketchCol,
  'nominalEntries=1024',
  'country'=''USA'' AND 'state'=''CA'', 'device'=''mobile'', 'SET_INTERSECT($1, $2)'
)
from table
where country = 'USA' or device = 'mobile...'
```

### Cardinality Estimation
* [DistinctCountThetaSketch](https://docs.pinot.apache.org/configuration-reference/functions/distinctcountthetasketch)
* [CPCSketch](https://docs.pinot.apache.org/users/user-guide-query/query-syntax/how-to-handle-unique-counting#compressed-probability-counting-cpc-sketches)
* [TupleSketches](https://docs.pinot.apache.org/users/user-guide-query/query-syntax/how-to-handle-unique-counting#tuple-sketches)

### Quantiles
* [PercentileKLL](https://docs.pinot.apache.org/configuration-reference/functions/percentilekll)

### Frequent Items
* [FrequentLongsSketch](https://docs.pinot.apache.org/configuration-reference/functions/frequentlongssketch)
* [FrequentStringsSketch](https://docs.pinot.apache.org/configuration-reference/functions/frequentstringssketch)

<hr>
### Advanced Integration
#### Raw Output Mode
Supported functions have 'raw' variants which can output binary representations of sketches for further processing.

Example:
```sql
select percentileRawKll(ArrDelayMinutes, 90) as sketch
from airlineStats
```
Returns Base64 encoded string: `BQEPC...`

Output can be processed as:

```java
byte[] decodedBytes = Base64.getDecoder().decode(encoded);
KllDoublesSketch sketch = KllDoublesSketch.wrap(Memory.wrap(decodedBytes));

System.out.println("Min, Median, Max values:");
System.out.println(Arrays.toString(sketch.getQuantiles(new double[]{0, 0.5, 1})));
```

#### Pre-built Sketch Ingestion
Apache Pinot can also ingest pre-built sketch objects both via Kafka (Realtime) or Spark (Batch) modes and merge(union) when running aggregations.
