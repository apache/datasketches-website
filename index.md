---
published: true
title: Data Sketches
layout: html_page
id: home
---

<link rel="stylesheet" type="text/css" href="css/index.css">
<link rel="stylesheet" type="text/css" href="css/header.css">

<main class="ds-masthead">
  <div class="container">
  <div class="row">
    <div class="col-md-8 col-md-offset-2 text-center">
      <span class="ds-bootlogo"></span>
      <p class="lead" style="font-size: 28px; margin-bottom: 10px">Sketches Library from <span class="y-bootlogo"></span></p>
      <p class="lead" style="font-size: 16px; line-height: 0.5; margin-bottom: 15px">A Java software library of 
        <a href="https://en.wikipedia.org/wiki/Stochastic" style="color: #EDE379"><i>stochastic</i></a> 
        <a href="https://en.wikipedia.org/wiki/Streaming_algorithm" style="color: #EDE379"><i>streaming algorithms</i></a></p>
      <p>
        <a class="btn btn-lg btn-outline-inverse" href="docs/KeyFeatures.html"><span class="fa fa-info-circle"></span> Overview</a>
        <a class="btn btn-lg btn-outline-inverse" href="downloads.html"><span class="fa fa-download"></span> Download</a>
        <a class="btn btn-lg btn-outline-inverse" href="https://github.com/datasketches"><span class="fa fa-github"></span> GitHub</a>
      </p>
    </div>
  </div>
  </div>
</main>

<div class="container">
  <div class="row">
    <div class="col-md-8 col-md-offset-2 text-center">
    </div>
  </div>
  <div class="row text-center main-marketing">
    <div class="col-md-4">
      <p><a href="/docs/KeyFeatures.html">
        <span class="fa fa-fighter-jet fa-4x"></span><br>
        <h2>Fast</h2>
      </a></p>
      <p class="text-justify"><a href="/docs/SketchOrigins.html">Sketches</a> are <i>fast</i>. 
      The sketch algorithms in this library process data in a single pass and are suitable for 
      both real-time and batch. 
      Sketches enable processing unique identifiers in an "additive" way that streamlines system's 
      architecture and enable fast queries of heretofore difficult metrics such as unique user counts.</p>
    </div>

    <div class="col-md-4">
      <a href="/docs/KeyFeatures.html">
        <span class="fa fa-database fa-4x"></span>
        <h2>Big Data</h2>
      </a>
      <p class="text-justify">This library has been specifically designed for Big Data systems: 
      Hadoop, Druid, and Hive sketch adaptors, 
      a Memory package for managing large off-heap memory data structures, 
      additional protection of sensitive user identifiers by special handling of hash seeds, 
      additional reduction of memory consumption with a front-end sampling, and compact binary storage.</p>
    </div>

    <div class="col-md-4">
      <p><a href="/docs/KeyFeatures.html">
        <span class="fa fa-bar-chart-o fa-4x"></span><br>
        <h2>Analysis</h2>
      </a></p>
      <p class="text-justify">Built-in set operators (Union, Intersection, Difference) 
      produce sketches as a result (and not just a number) enabling full set expressions, 
      such as ((A &#8746; B) &#8745; (C &#8746; D)) \ (E &#8746; F).  
      This capability along with predictable and superior accuracy 
      (compared with <i>Include/Exclude</i> approaches) enable unprecedented analysis capabilities 
      for fast queries. </p>
    </div>
  </div>
</div>

<div class="ds-panel">
  <div class="container">
    <div class="row-fluid text-center">
      <div class="col-sm-4 col-sm-offset-2">
        <h3>Get Started</h3>
        <p><a href="/docs/architecture.html">Architecture</a></p> <!-- add {{site.stable_version}} -->
        <p><a href="/docs/tutorials.html">Tutorials</a></p> <!-- add {{site.stable_version}} 
        <p style="color:rgba(177,186,198,0.7)">Latest Stable Release</p> --> <!-- add {{site.stable_version}} -->
      </div>
      <div class="col-sm-4">
        <h3>Learn More</h3>
        <p><a href="/docs/KeyFeatures.html">Key Features</a></p>
        <p><a href="/docs/theChallenge.html">The Challenge</a></p>
        <p><a href="/docs/whoUses.html">Who uses DataSketches?</a></p>
      </div>
    </div>
  </div>
</div>
