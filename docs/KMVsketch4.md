---
layout: doc_page
---
[Prev](/docs/KMVsketch3.html)
[Next](/docs/KMVsketch5.html)

#The KMV Sketch, Step 4
Not having to store hash values greater than <i>V(k<sup>th</sup>)</i> means we can automatically reject any incoming hash values greater than or equal to  <i>V(k<sup>th</sup>)</i> up front.

Additionally, we reject any incoming hash values that are duplicates of any of the hash values we already have in our <i>KMV</i> list.

<img class="ds-img" src="/docs/img/KMV4.png" alt="KMV4" />

[Prev](/docs/KMVsketch3.html)
[Next](/docs/KMVsketch5.html)

