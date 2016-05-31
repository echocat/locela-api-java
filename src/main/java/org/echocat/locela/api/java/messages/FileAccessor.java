/*****************************************************************************************
 * *** BEGIN LICENSE BLOCK *****
 *
 * Version: MPL 2.0
 *
 * echocat Locela - API for Java, Copyright (c) 2014-2015 echocat
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 *
 * *** END LICENSE BLOCK *****
 ****************************************************************************************/

package org.echocat.locela.api.java.messages;

import org.echocat.locela.api.java.properties.Properties;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.*;
import java.nio.charset.Charset;

import static java.lang.System.getProperty;
import static java.nio.charset.Charset.forName;
import static org.echocat.locela.api.java.utils.ResourceUtils.closeQuietly;

public interface FileAccessor {

    @Nullable
    public Reader open(@Nonnull String file) throws IOException;

    public abstract static class InputStreamBased implements FileAccessor {

        public static final Charset CHARSET = forName(getProperty(Properties.class.getName() + ".defaultCharset", "UTF-8"));

        @Override
        @Nullable
        public Reader open(@Nonnull String file) throws IOException {
            boolean success = false;
            final InputStream is = openStream(file);
            try {
                final Reader reader = is != null ? new InputStreamReader(is, CHARSET) : null;
                success = true;
                return reader;
            } finally {
                if (!success) {
                    closeQuietly(is);
                }
            }
        }

        @Nullable
        protected abstract InputStream openStream(@Nonnull String file) throws IOException;

    }

    public static class ClassLoaderBased extends InputStreamBased {

        @Nonnull
        private static final ClassLoaderBased INSTANCE = new ClassLoaderBased(Thread.currentThread().getContextClassLoader());

        @Nonnull
        public static ClassLoaderBased classPathFileAccessor() {
            return INSTANCE;
        }

        @Nonnull
        private final ClassLoader _classLoader;

        public ClassLoaderBased(@Nonnull ClassLoader classLoader) {
            _classLoader = classLoader;
        }

        @Nullable
        @Override
        protected InputStream openStream(@Nonnull String file) throws IOException {
            final String targetFile;
            if (file.startsWith("/")) {
                targetFile = file.substring(1);
            } else {
                targetFile = file;
            }
            return _classLoader.getResourceAsStream(targetFile);
        }

        @Override
        public boolean equals(Object o) {
            final boolean result;
            if (this == o) {
                result = true;
            } else if (o == null || getClass() != o.getClass()) {
                result = false;
            } else {
                final ClassLoaderBased that = (ClassLoaderBased) o;
                result = _classLoader.equals(that._classLoader);
            }
            return result;
        }

        @Override
        public int hashCode() {
            return _classLoader.hashCode();
        }

    }

    public static class FileSystemBased extends InputStreamBased {

        @Nonnull
        private static final FileSystemBased INSTANCE = new FileSystemBased();

        @Nonnull
        public static FileSystemBased fileSystemFileAccessor() {
            return INSTANCE;
        }

        @Nullable
        @Override
        protected InputStream openStream(@Nonnull String filename) throws IOException {
            final File file = new File(filename);
            InputStream is;
            if (file.isFile()) {
                try {
                    is = new FileInputStream(file);
                } catch (final FileNotFoundException ignored) {
                    is = null;
                }
            } else {
                is = null;
            }
            return is;
        }

        @Override
        public boolean equals(Object o) {
            final boolean result;
            if (this == o) {
                result = true;
            } else {
                result = !(o == null || getClass() != o.getClass());
            }
            return result;
        }

        @Override
        public int hashCode() {
            return 0;
        }

    }

}
