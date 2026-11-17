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

# Apache Maven Reporting API

The Reporting API is the contract between the [Maven Site Plugin](/plugins/maven-site-plugin/) and every report
plugin. A report implements [`MavenReport`](./apidocs/org/apache/maven/reporting/MavenReport.html) to declare its
name, category and output path, and to render itself into a Doxia [`Sink`](/doxia/doxia/doxia-sink-api/); the site
plugin calls it and decorates the result with the site skin.

To write a report plugin, extend `AbstractMavenReport` from
[Maven Reporting Implementation](../maven-reporting-impl/) instead of implementing this API directly: it implements
both `MavenReport` and `Mojo`, so the report also runs as a standalone goal. The
[guide to developing a report plugin](/guides/plugin/guide-java-report-plugin-development.html) walks through a
complete example.

## Types

The API holds four types and depends only on `doxia-sink-api`.

| Type | Role |
|---|---|
| [`MavenReport`](./apidocs/org/apache/maven/reporting/MavenReport.html) | The report contract the site plugin calls. |
| [`MavenReportRenderer`](./apidocs/org/apache/maven/reporting/MavenReportRenderer.html) | Renders one report page. |
| [`MavenReportException`](./apidocs/org/apache/maven/reporting/MavenReportException.html) | The checked exception a report throws. |
| [`MavenMultiPageReport`](./apidocs/org/apache/maven/reporting/MavenMultiPageReport.html) | Deprecated since 4.1.0: use `MavenReport.generate(Sink, SinkFactory, Locale)`. |

A report that writes more than one page receives a `SinkFactory` in `generate(Sink, SinkFactory, Locale)` and creates
one sink per extra page. The default implementation ignores the factory and calls `generate(Sink, Locale)`.

## Compatibility notes for report authors

During site generation, report plugins run with the Reporting API and Doxia classes that the Maven Site Plugin
supplies, not with the versions they were compiled against. Two consequences for 4.1.0:

- Keep implementing `MavenMultiPageReport` for now. Maven Site Plugin 3.22.0 and older load Reporting API 4.0.0 and
  call the multi-page `generate` only through that interface. To keep the deprecation warning in one place,
  implement it through a package-private interface annotated with `@SuppressWarnings("deprecation")`, as
  `AbstractMavenReport` in Maven Reporting Implementation does.
- Keep using `org.apache.maven.doxia.sink.impl.SinkEventAttributeSet`, although Doxia 2.2.0 deprecates it. Its
  replacement in `doxia-sink-api` 2.2.0 is missing under Maven Site Plugin 3.22.0 and older, and the report is
  skipped with a `LinkageError`.

## History

| Release date | Reporting API | [Doxia](/doxia/) |
|:---:|:---:|:---:|
| 2026-10 | 4.1.0 | 2.2.0 |
| 2024-10 | [4.0.0](/shared-archives/maven-reporting-api-4.0.0/) | **2.0.0** |
| 2022-07 | [3.1.1](/shared-archives/maven-reporting-api-3.1.1/) | 1.11.1 |
