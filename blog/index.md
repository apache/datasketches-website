---
title: DataSketches Blog
sectionid: blog
layout: html_page
---

<link rel="stylesheet" href="/css/blogs.css">

<div class="ds-header">
  <div class="container">
    <h1>DataSketches Blog</h1>
    <h4></h4>
  </div>
</div>
  
<div class="container blog">
  <div class="row">
    <div class="col-md-8 col-md-offset-2">
      {% for page in site.posts %}
      <div class="blog-listing">
        <h2><a href="{{ page.url }}">{{ page.title }}</a></h2>
        <p class="author text-uppercase text-muted">{% if page.author %}{{ page.author }} · {% endif %}{{ page.date | date: "%B %e, %Y" }}</p>
        <p>{{ page.excerpt }}</p>
        <a class="btn btn-default btn-xs" href="{{ page.url }}">Read More</a>
      </div>
      {% endfor %}
    </div>
  </div>
</div>