---
layout: doc_page
---
[Prev](/docs/KMVbetterEst.html)<br>
[Next](/docs/KMVupdateVkth.html)

#The KMV Sketch, Rejection Rules
Not needing to store hash values greater than <i>V(k<sup>th</sup>)</i> means we can automatically reject any incoming hash values greater than or equal to <i>V(k<sup>th</sup>)</i> up front.

Additionally, we can reject any incoming hash values that are duplicates of any of the hash values we already have in our <i>KMV</i> list.

<img class="ds-img" src="/docs/img/KMV4.png" alt="KMV4" />

[Prev](/docs/KMVbetterEst.html)<br>
[Next](/docs/KMVupdateVkth.html)

