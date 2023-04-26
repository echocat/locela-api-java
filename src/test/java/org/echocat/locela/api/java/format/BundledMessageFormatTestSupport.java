package org.echocat.locela.api.java.format;

import org.echocat.locela.api.java.properties.Properties;
import org.echocat.locela.api.java.properties.Property;
import org.junit.Test;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Locale;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Locale.US;
import static org.echocat.locela.api.java.properties.StandardPropertiesReader.propertiesReader;
import static org.echocat.locela.api.java.testing.BaseMatchers.is;
import static org.echocat.locela.api.java.utils.CollectionUtils.asImmutableList;
import static org.hamcrest.MatcherAssert.assertThat;

public abstract class BundledMessageFormatTestSupport {

    @Nonnull
    private final Locale _locale;
    @Nonnull
    private final String _pattern;
    @Nonnull
    private final String _expectedResult;

    protected static Collection<Object[]> createParameters(@Nonnull String name, @Nullable Locale... locales) throws Exception {
        final Collection<Object[]> results = new ArrayList<>();
        if (locales != null && locales.length > 0) {
            for (final Locale locale : locales) {
                final Properties<String> properties = getFormatToResultsFor(locale, name);
                for (final Property<String> property : properties) {
                    results.add(new Object[]{locale, property.getId(), property.get()});
                }
            }
        } else {
            final Properties<String> properties = getFormatToResultsFor(null, name);
            for (final Property<String> property : properties) {
                results.add(new Object[]{US, property.getId(), property.get()});
            }
        }
        return asImmutableList(results);
    }

    @Nonnull
    protected static Properties<String> getFormatToResultsFor(@Nullable Locale locale, @Nonnull String name) throws IOException {
        final String filename = "expectedFormatResults." + name + (locale != null ? "_" + locale : "") + ".properties";
        try (final InputStream is = BundledMessageFormatTestSupport.class.getResourceAsStream(filename)) {
            if (is == null) {
                throw new AssertionError("The expected file '" + filename + "' could not be found.");
            }
            try (final Reader reader = new InputStreamReader(is, UTF_8)) {
                return propertiesReader().read(reader);
            }
        }
    }

    protected BundledMessageFormatTestSupport(@Nonnull Locale locale, @Nonnull String pattern, @Nonnull String expectedResult) {
        _locale = locale;
        _pattern = pattern;
        _expectedResult = expectedResult;
    }

    @Nonnull
    public Locale getLocale() {
        return _locale;
    }

    @Nonnull
    public String getPattern() {
        return _pattern;
    }

    @Nonnull
    public String getExpectedResult() {
        return _expectedResult;
    }

    @Test
    public void testFormat() throws Exception {
        assertThat(executeFormat(_locale, _pattern), is(_expectedResult));
    }

    protected abstract String executeFormat(@Nonnull Locale locale, @Nonnull String pattern) throws Exception;

}
