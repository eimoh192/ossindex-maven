/*
 * Copyright (c) 2018-present Sonatype, Inc. All rights reserved.
 *
 * This program is licensed to you under the Apache License Version 2.0,
 * and you may not use this file except in compliance with the Apache License Version 2.0.
 * You may obtain a copy of the Apache License Version 2.0 at http://www.apache.org/licenses/LICENSE-2.0.
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the Apache License Version 2.0 is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the Apache License Version 2.0 for the specific language governing permissions and limitations there under.
 */
package org.sonatype.ossindex.maven.plugin.export;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Locale;

import javax.inject.Named;
import javax.inject.Singleton;

import org.sonatype.ossindex.maven.common.ComponentReportResult;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Text report {@link Exporter}.
 *
 * @since 3.0.0
 */
@Named
@Singleton
public class TextExporter
  implements Exporter
{
  private static final Logger log = LoggerFactory.getLogger(TextExporter.class);

  @Override
  public boolean accept(final File file) {
    checkNotNull(file);
    return file.getName().toLowerCase(Locale.ENGLISH).endsWith(".txt");
  }

  @Override
  public void export(final ComponentReportResult result, final File file) throws IOException {
    checkNotNull(result);
    checkNotNull(file);

    log.debug("Exporting to: {}", file);

    Path path = file.getParentFile().toPath();
    Files.createDirectories(path);

    try (Writer writer = new BufferedWriter(new FileWriter(file))) {
      writer.write(result.explain());
    }
    catch (Exception e) {
      log.error("Export failed", e);
      throw new IOException("Failed to export", e);
    }
  }
}
