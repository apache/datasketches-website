---
sectionid: docs
layout: doc_page
---

#Size in Bytes, Compact Form
These tables compute the size of a sketch after it has been converted into Compact Form. 
The size of a sketch during the build phase is more complex as the sketch starts small and 
resizes by the configurable <i>Resize Factor</i> up to the in-memory size of <i>2k*8</i> bytes plus
java overhead and a few primitives.

Note: a sketch entry = 8 bytes.

##Quick Select Sketch (Default)
The number of valid entries in the Quick Select Sketch after it enters estimation mode
statistically varies from <i>k</i> to <i>15k/8</i> with an average of about <i>3k/2</i>. 
It is a user option to force a rebuild() prior to compacting the sketch in which case the 
number of valid entries is always <i>k</i>.


&nbsp;  | Empty | After Rebuild() | Estimating Avg | Estimating Max
Nominal Entries (k) : Formula -> | 8 | k*8 +24 | k*12 + 24 | k*15 + 24
----------------|-------------|-------------|------------|--------------
16 | 8 | 152 | 216 | 264
32 | 8 | 280 | 408 | 504
64 | 8 | 536 | 792 | 984
128 | 8 | 1,048 | 1,560 | 1,944
256 | 8 | 2,072 | 3,096 | 3,864
512 | 8 | 4,120 | 6,168 | 7,704
1,024 | 8 | 8,216 | 12,312 | 15,384
2,048 | 8 | 16,408 | 24,600 | 30,744
4,096 | 8 | 32,792 | 49,176 | 61,464
8,192 | 8 | 65,560 | 98,328 | 122,904
16,384 | 8 | 131,096 | 196,632 | 245,784
32,768 | 8 | 262,168 | 393,240 | 491,544
65,536 | 8 | 524,312 | 786,456 | 983,064
131,072 | 8 | 1,048,600 | 1,572,888 | 1,966,104
262,144 | 8 | 2,097,176 | 3,145,752 | 3,932,184
524,288 | 8 | 4,194,328 | 6,291,480 | 7,864,344
1,048,576 | 8 | 8,388,632 | 12,582,936 | 15,728,664

##Alpha Sketch
The number of valid entries in the Alpha Sketch after it enters estimation mode 
is a random variable that has a probability distribution with a mean of <i>k</i>
and a standard deviation of <i>sqrt(k)</i>. 
The last column computes the maximum size with a confidence of 99.997% representing
plus 4 standard deviations.


&nbsp;  | Empty | Estimating Avg | Std Dev | Max @ 99.997% 
Nominal Entries (k) : Formula -> | 8 | k*8 + 24 | sqrt(k) | (k+4SD)*8 +24
----------------|-------------|-------------|------------|----------
512 | 8 | 4,120 | 23 | 4,844
1,024 | 9 | 8,216 | 32 | 9,240
2,048 | 10 | 16,408 | 45 | 17,856
4,096 | 11 | 32,792 | 64 | 34,840
8,192 | 12 | 65,560 | 91 | 68,456
16,384 | 13 | 131,096 | 128 | 135,192
32,768 | 14 | 262,168 | 181 | 267,961
65,536 | 15 | 524,312 | 256 | 532,504
131,072 | 16 | 1,048,600 | 362 | 1,060,185
262,144 | 17 | 2,097,176 | 512 | 2,113,560
524,288 | 18 | 4,194,328 | 724 | 4,217,498
1,048,576 | 19 | 8,388,632 | 1,024 | 8,421,400