/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

/**
 * The API between the Maven Site Plugin and the reports it renders.
 * <p>
 * A report implements {@link org.apache.maven.reporting.MavenReport}. Report plugins usually extend
 * {@code AbstractMavenReport} from Maven Reporting Implementation, which also lets the report run as
 * a standalone goal. For each configured report, the Maven Site Plugin:
 * <ol>
 * <li>calls {@link org.apache.maven.reporting.MavenReport#setReportOutputDirectory(java.io.File)
 * setReportOutputDirectory} with the site output directory;</li>
 * <li>skips the report if {@link org.apache.maven.reporting.MavenReport#canGenerateReport()
 * canGenerateReport} returns {@code false};</li>
 * <li>calls {@link org.apache.maven.reporting.MavenReport#generate(org.apache.maven.doxia.sink.Sink,
 * org.apache.maven.doxia.sink.SinkFactory, java.util.Locale) generate} with a sink for the main page
 * and a factory for any sub-pages;</li>
 * <li>decorates the pages with the site skin, unless
 * {@link org.apache.maven.reporting.MavenReport#isExternalReport() isExternalReport} returns
 * {@code true}.</li>
 * </ol>
 * <p>
 * Every report plugin implements this API, so a method added to {@code MavenReport} must be a
 * {@code default} method, and two default methods must never call each other.
 */
package org.apache.maven.reporting;
