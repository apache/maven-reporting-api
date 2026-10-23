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
package org.apache.maven.reporting;

import java.io.File;
import java.util.Locale;

import org.apache.maven.doxia.sink.Sink;
import org.apache.maven.doxia.sink.SinkFactory;

/**
 * The basis for a Maven report: a report that the Maven Site Plugin renders into the project site.
 * Report plugins usually extend {@code AbstractMavenReport} from Maven Reporting Implementation
 * instead of implementing this interface directly.
 *
 * @author Brett Porter
 * @author <a href="evenisse@apache.org">Emmanuel Venisse</a>
 * @author <a href="mailto:vincent.siveton@gmail.com">Vincent Siveton</a>
 * @since 2.0
 */
public interface MavenReport {
    /** Plexus lookup name */
    String ROLE = MavenReport.class.getName();

    /** Category for project information reports */
    String CATEGORY_PROJECT_INFORMATION = "Project Info";

    /** Category for project reports */
    String CATEGORY_PROJECT_REPORTS = "Project Reports";

    /**
     * Generate the report for the given locale.
     * <p>
     * A report that renders a single page implements only this method. A report that renders several
     * pages also overrides {@link #generate(Sink, SinkFactory, Locale)}.
     *
     * @param sink the sink to use for the generation.
     * @param locale the wanted locale to generate the report.
     * @throws MavenReportException if any
     */
    void generate(Sink sink, Locale locale) throws MavenReportException;

    /**
     * Generate the report for the given locale, possibly as several pages.
     * <p>
     * A report that renders a single page ignores {@code sinkFactory}. A report that renders several
     * pages uses {@code sinkFactory} to create the sinks for its sub-pages. By default, this method
     * renders a single page with {@link #generate(Sink, Locale)}.
     *
     * @param sink the sink to use for the main page.
     * @param sinkFactory the factory for sub-page sinks, or {@code null} when sub-pages cannot be created.
     * @param locale the wanted locale to generate the report.
     * @throws MavenReportException if any
     * @since 4.1.0
     */
    default void generate(Sink sink, SinkFactory sinkFactory, Locale locale) throws MavenReportException {
        generate(sink, locale);
    }

    /**
     * Get the path relative to {@link #getReportOutputDirectory()} where the report's main output
     * file will be written. The last component is the name of the file without any extension. The
     * actual output extension is added by the sink implementation.
     * <p>
     * Note: This method won't be {@code default} anymore when {@link #getOutputName()} is removed.
     * You are advised to implement it as soon as possible.
     *
     * @since 4.0.0
     * @return the relative output path of this report
     */
    default String getOutputPath() {
        return getOutputName();
    }

    /**
     * Get the name of the report's main output file, without any extension.
     *
     * @return the output name of this report
     * @deprecated Method name does not properly reflect its purpose. Implement and use
     * {@link #getOutputPath()} instead.
     */
    @Deprecated
    String getOutputName();

    /**
     * Get the category name for this report.
     *
     * @return the category name of this report. Should be {@link #CATEGORY_PROJECT_INFORMATION}
     * or {@link #CATEGORY_PROJECT_REPORTS}
     */
    String getCategoryName();

    /**
     * Get the localized report name.
     *
     * @param locale the wanted locale to return the report's name.
     * @return the name of this report
     */
    String getName(Locale locale);

    /**
     * Get the localized report description.
     *
     * @param locale the wanted locale to return the report's description.
     * @return the description of this report
     */
    String getDescription(Locale locale);

    /**
     * Set a new shared report output directory. This directory may contain the output of other
     * reports as well.
     *
     * @param outputDirectory the new shared report output directory
     */
    void setReportOutputDirectory(File outputDirectory);

    /**
     * Get the shared report output directory.
     *
     * @return the current shared report output directory
     */
    File getReportOutputDirectory();

    /**
     * Tell whether this report writes its own output files instead of rendering into the sink, as the
     * Javadoc report does when it calls the javadoc tool. The Maven Site Plugin does not decorate an
     * external report with the site skin.
     *
     * @return {@code true} if this report is external, {@code false} otherwise. Most reports return
     * {@code false}.
     */
    boolean isExternalReport();

    /**
     * Verify some conditions before generating the report, for example that there is something to
     * report on. The Maven Site Plugin skips a report that returns {@code false}.
     *
     * @return {@code true} if this report can be generated, {@code false} otherwise.
     * @throws MavenReportException if any
     */
    boolean canGenerateReport() throws MavenReportException;
}
