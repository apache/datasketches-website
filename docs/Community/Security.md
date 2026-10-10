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
# Security

## Reporting a Vulnerability

Please do not report security vulnerabilities through public GitHub issues, pull requests or mailing lists.

Report them privately to the Apache Security Team at <security@apache.org>, or to the Apache DataSketches PMC at <private@datasketches.apache.org>. Apache DataSketches follows the [Apache vulnerability handling process](https://www.apache.org/security/committers.html#vulnerability-handling).

Please include the affected component and version, a description of the issue and, if possible, a way to reproduce it (for example, a serialized sketch that triggers it).

## Supported Versions

The Apache DataSketches PMC supports only the latest release of each component. Security fixes are made in the next release and are not backported. Users should upgrade to the latest release to receive fixes.

## Published Vulnerabilities

### DataSketches C++

All of the following affect only applications that deserialize sketches from untrusted sources. They are fixed in [DataSketches C++ 5.3.0](https://github.com/apache/datasketches-cpp/releases/tag/5.3.0).

| CVE | Severity | Description | Affected versions |
|-----|----------|-------------|-------------------|
| [CVE-2026-103501](https://www.cve.org/CVERecord?id=CVE-2026-103501) | moderate | [Heap buffer overflow in HLL sketch deserialization](https://lists.apache.org/thread/n2oc2j841dzsv4239jjp08of66bo79c5) | 1.0.0-incubating through 5.2.0 |
| [CVE-2026-103513](https://www.cve.org/CVERecord?id=CVE-2026-103513) | moderate | [Out-of-bounds read and write in CPC sketch deserialization](https://lists.apache.org/thread/s7wyoqyf120dl5ys8jtr26dskd6m485h) | 2.0.0-incubating through 5.2.0 |
| [CVE-2026-103634](https://www.cve.org/CVERecord?id=CVE-2026-103634) | moderate | [Out-of-bounds read and write in Count-Min sketch deserialization](https://lists.apache.org/thread/rkx9oh06g5cjc6vmmpqloxwfz0gzz01h) | 4.1.0 through 5.2.0 |
| [CVE-2026-103635](https://www.cve.org/CVERecord?id=CVE-2026-103635) | low | [Out-of-bounds read in compact Theta sketch deserialization](https://lists.apache.org/thread/15b86y9jstco39kzppjw7l5kvhjvwqvv) | 3.1.0 through 5.2.0 |
| [CVE-2026-103636](https://www.cve.org/CVERecord?id=CVE-2026-103636) | low | [Out-of-bounds read in VarOpt union deserialization](https://lists.apache.org/thread/znn94s463w7wy125khcywxsp82y2qkq5) | 2.0.0-incubating through 5.2.0 |
