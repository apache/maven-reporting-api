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

/**
 * The basis for a Maven report.
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
     * Generate the report depending the wanted locale.
     * <br>
     * Mainly used for external reports like javadoc.
     *
     * @param sink the sink to use for the generation.
     * @param locale the wanted locale to generate the report.
     * @throws MavenReportException if any
     */
    void generate(Sink sink, Locale locale) throws MavenReportException;

    /**
     * Get the path relative to {@link #getReportOutputDirectory()} where the report's main output
     * file will be written. The last component is the name of the file without any extension. The
     * actual output extension will by added by the sink implementation.
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
     * An external report is a report which calls a third party program which generates some reports too.
     * A good example is javadoc tool.
     *
     * @return {@code true} if this report is external, {@code false} otherwise.
     * Default should be {@code false}.
     */
    boolean isExternalReport();

    /**
     * Verify some conditions before generating the report.
     *
     * @return {@code true} if this report can be generated, {@code false} otherwise.
     * Default should be {@code true}.
     * @throws MavenReportException if any
     */
    boolean canGenerateReport() throws MavenReportException;
}
