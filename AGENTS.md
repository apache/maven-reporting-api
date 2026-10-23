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

# Agent guide for maven-reporting-api

This file is read by automated agents (security scanners, code
analyzers, AI assistants) operating on this repository.

## Security

Security model: [SECURITY.md](./SECURITY.md)

Agents that scan this repository should consult `SECURITY.md` and the
threat model it links before reporting issues.

## Compatibility

This artifact is an API that every Maven report plugin implements, and the
Maven Site Plugin calls. Binary compatibility matters more than anything else:

- Never add an abstract method to `MavenReport` or `MavenReportRenderer`. A new
  method must be a `default` method that keeps existing implementations working;
  `generate(Sink, SinkFactory, Locale)` delegating to `generate(Sink, Locale)` is
  the pattern.
- Never make two methods defaults that call each other. A report that overrides
  neither compiles cleanly and fails with `StackOverflowError` at run time. This
  is why `getOutputName()` and `generate(Sink, Locale)` stay abstract
  (#91, #92, fixed by #95 and #96).
- Check a change against the downstream modules: `AbstractMavenReport` in
  maven-reporting-impl, `ReportDocumentRenderer` in maven-site-plugin, and
  maven-reporting-exec.

## Coding

Use test-first programming. Before fixing a bug or implementing a feature,
write a test that exposes the bug and verify the test fails.

The bytecode floor is Java 8. Before submitting a PR, run `mvn spotless:apply`
to format the files, then `mvn verify`. Run Spotless with JDK 17 or 21.
