<!---
 Licensed to the Apache Software Foundation (ASF) under one or more
 contributor license agreements.  See the NOTICE file distributed with
 this work for additional information regarding copyright ownership.
 The ASF licenses this file to You under the Apache License, Version 2.0
 (the "License"); you may not use this file except in compliance with
 the License.  You may obtain a copy of the License at

      http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
-->

# Apache Maven Reporting API

[![Apache License, Version 2.0, January 2004](https://img.shields.io/github/license/apache/maven.svg?label=License)](https://www.apache.org/licenses/LICENSE-2.0)
[![Maven Central](https://img.shields.io/maven-central/v/org.apache.maven.reporting/maven-reporting-api.svg?label=Maven%20Central)](https://central.sonatype.com/artifact/org.apache.maven.reporting/maven-reporting-api)
[![Reproducible Builds](https://img.shields.io/endpoint?url=https://raw.githubusercontent.com/jvm-repo-rebuild/reproducible-central/master/content/org/apache/maven/reporting/maven-reporting-api/badge.json)](https://github.com/jvm-repo-rebuild/reproducible-central/blob/master/content/org/apache/maven/reporting/maven-reporting-api/README.md)
[![Verify](https://github.com/apache/maven-reporting-api/actions/workflows/maven-verify.yml/badge.svg)](https://github.com/apache/maven-reporting-api/actions/workflows/maven-verify.yml)

The contract between the [Maven Site Plugin](https://maven.apache.org/plugins/maven-site-plugin/)
and every report plugin. A report implements `MavenReport` to declare its name,
category and output path, and to render itself into a Doxia `Sink`; the site
plugin calls it and decorates the result with the site skin.

Report plugins usually extend `AbstractMavenReport` from
[Maven Reporting Implementation](https://maven.apache.org/shared/maven-reporting-impl/)
instead of implementing this API directly.

- Documentation: <https://maven.apache.org/shared/maven-reporting-api/>
- Issues: <https://github.com/apache/maven-reporting-api/issues>
- Security reports: see [SECURITY.md](SECURITY.md); never a public issue
- Contributing: see [CONTRIBUTING.md](CONTRIBUTING.md)

## What this is

The artifact holds four types and depends only on `doxia-sink-api`:

| Type | Role |
|---|---|
| `MavenReport` | The report contract the site plugin calls |
| `MavenReportRenderer` | Renders one report page |
| `MavenReportException` | The checked exception a report throws |
| `MavenMultiPageReport` | Deprecated since 4.1.0; use `MavenReport.generate(Sink, SinkFactory, Locale)` |

Where it sits:

```text
doxia-sink-api
  └─ maven-reporting-api         (this repository)
       ├─ maven-reporting-impl   AbstractMavenReport, which report plugins extend
       ├─ maven-reporting-exec   finds and configures report plugins for the site
       └─ maven-site-plugin      calls MavenReport and renders the pages
```

Every report plugin implements this API, so a new method must be a `default`
method, never abstract. Releases follow Doxia and come before
[Maven Reporting Exec](https://maven.apache.org/shared/maven-reporting-exec/),
Maven Reporting Implementation and the Maven Site Plugin.

## Build

```shell
mvn verify
```
