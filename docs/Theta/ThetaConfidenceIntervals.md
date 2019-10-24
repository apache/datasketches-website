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

<h3>Theta Sketch Confidence Intervals, Notes</h3>
1. The Theta Sketches within the DataSketches Library provide Frequentist Confidence Intervals 
based on the tails of the Binomial Distribution.

2. These bounds are only approximate, but we provide a guarantee
   (backed by experiments, not by proofs) that neither bound is off
   by more than 1 percent of the estimate.

3. To avoid numerical issues and high computational cost, the implementation
   carves up the input space (|S|, p) into multiple regions, and uses
   different approximations on each. 

4. At a high level, there is a two-way split, between large |S|, and small |S|.
   When |S| > 120, the continuity-corrected Gaussian approximation to the Binomial
   Distribution satisfies the 1 percent guarantee, so we just use that.

5. For small |S| <= 120, the main idea is to fall back to the exact Binomial
   Distribution. However, that is expensive, and worse yet, subject to
   numerical issues. These issues are serious, and occur when p is very 
   close to 1.0 or 0.0.  Therefore we immediately split off two more cases. 

6. When p is very close to 1.0, we already know (because |S| is small)
   that the answer is [|S|-1, |S|+1], so we just return that instead
   of trying to evaluate the incomplete beta function with difficult inputs.

7. When p is very close to 0.0 (say 1e-6), one could avoid the numerical issues by 
   exploiting the fact that the LB and the UB are very close to being
   constant multiples of the estimate. The exact multiples would depend on 
   |S|, and delta, but could be pre-computed and stored in tables.

8. It turns that a slightly fancier version of the idea (7) allows a 
   satisfactory (i.e. 1 percent) approximation to the bounds to be 
   obtained not only for small p, but also for moderate p [but not for large p].
   This version of the idea uses the continuity-corrected Gaussian approximation
   to the Binomial Distribution, but with an "adjusted" number of standard 
   deviations, that is different for the LB and for the UB, and for each value 
   of |S| and of delta. However, these adjusted numbers can be precomputed
   and stored in a table. This is what the library does.

9. We have now carved away enough cases so that we only need to compute
   exact bounds for 0 <= |S| <= 120, for p values that are large (although
   not super-close to 1.0).
	 
10. The switchover between scheme (8) and scheme (9) is determined by a threshold
    on p which depends on the value of |S|. The library's specific threshold |S|/360
    was empirically chosen to support the promise of 1 percent accuracy.
		
    1. The exact bounds needed by scheme (9) could be computed using
     binary search on the tails of the Binomial Distribution, with the
     latter computed using a high-quality implementation of the
     Incomplete Beta Function obtained from a Scientific Computing
     library.  

    2. We have written and heavily used some auxiliary code
     which does exactly that: binary search using the Incomplete Beta
     Function. It was used to pre-compute the tables mentioned in
     point (7); to choose values for the various empirically determined
     thresholds such as 120 and \|S\|/360; and finally in an extensive grid
     search covering the input parameter space to verify that our overall 
     approximation scheme satisfies the 1 percent accuracy guarantee.

    3. However, it was impractical for the Theta Sketch Library as such to include
     a high quality implementation of the Incomplete Beta Function; writing one
     is a task best left to experts, and we didn't want the Theta Sketch Library
     to have an external dependence on a Scientific library.

    4. Therefore we developed a more direct approach the computing the exact LB and UB.
     It is based on an interesting combinatorial identity and exploits the facts
     that p >= |S|/360 and |S| <= 120. It employs a simple linear search,
     so there is no tricky bracketing and binary search code, and it takes at 
     most 30 microseconds to compute [the slowest input is |S|=1, p = 1/360].

12. We mention that UB=LB=\|S\| when p=1, and LB=0 when \|S\|=0. Also, the library
    uses a closed-form expression for the UB when |S|=0, and for the LB when |S|=1.

13. So far, we have been discussing the Frequentist Confidence Interval based on the
    tails of the Binomial Distribution. In some cases (for example when p is very close
    to 1.0) the LB can be smaller than |S|. However, it is obvious that n >= |S|, so the
    library returns |S| instead of the official Binomial LB in that case.

14. In addition, just in case it is possible (we are not sure) for the
    library to compute a confidence interval that doesn't cover the
    estimate, we shield the user from these hypothetical outputs by
    by always checking and if necessary growing the interval to cover
    the estimate.

Also see the <a href="{{site.docs_pdf_dir}}/ThetaSketchFramework.pdf">Theta Sketch Framework</a> paper.
