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
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

class MavenReportTest {

    private static final Locale LOCALE = Locale.ENGLISH;

    /** Records which generate method the implementation received. */
    private abstract static class RecordingReport implements MavenReport {
        String called;
        Sink sink;
        SinkFactory sinkFactory;
        Locale locale;

        @Override
        public String getOutputPath() {
            return "report";
        }

        @Override
        public String getCategoryName() {
            return CATEGORY_PROJECT_REPORTS;
        }

        @Override
        public String getName(Locale locale) {
            return "report";
        }

        @Override
        public String getDescription(Locale locale) {
            return "report";
        }

        @Override
        public void setReportOutputDirectory(File outputDirectory) {}

        @Override
        public File getReportOutputDirectory() {
            return null;
        }

        @Override
        public boolean isExternalReport() {
            return false;
        }

        @Override
        public boolean canGenerateReport() {
            return true;
        }
    }

    /** A report written before 4.1.0: implements only the single-page method. */
    private static class SinglePageReport extends RecordingReport {
        @Override
        @SuppressWarnings("deprecation")
        public void generate(Sink sink, Locale locale) {
            called = "single";
            this.sink = sink;
            this.locale = locale;
        }
    }

    /** A report written against 4.1.0: implements only the multi-page method. */
    private static class MultiPageReport extends RecordingReport {
        @Override
        public void generate(Sink sink, SinkFactory sinkFactory, Locale locale) {
            called = "multi";
            this.sink = sink;
            this.sinkFactory = sinkFactory;
            this.locale = locale;
        }
    }

    @Test
    void multiPageCallReachesSinglePageImplementation() throws MavenReportException {
        SinglePageReport report = new SinglePageReport();

        report.generate(null, null, LOCALE);

        assertEquals("single", report.called);
        assertSame(LOCALE, report.locale);
    }

    @Test
    @SuppressWarnings("deprecation")
    void singlePageCallReachesMultiPageImplementationWithoutFactory() throws MavenReportException {
        MultiPageReport report = new MultiPageReport();

        report.generate(null, LOCALE);

        assertEquals("multi", report.called);
        assertNull(report.sinkFactory);
        assertSame(LOCALE, report.locale);
    }
}
