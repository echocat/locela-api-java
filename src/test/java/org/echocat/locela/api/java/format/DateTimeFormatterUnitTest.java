package org.echocat.locela.api.java.format;

import org.junit.Test;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import static org.echocat.locela.api.java.format.DateTimeFormatter.Pattern.DEFAULT;
import static org.echocat.locela.api.java.format.DateTimeFormatter.Pattern.FULL;
import static org.echocat.locela.api.java.format.DateTimeFormatter.Pattern.LONG;
import static org.echocat.locela.api.java.format.DateTimeFormatter.Pattern.MEDIUM;
import static org.echocat.locela.api.java.format.DateTimeFormatter.Pattern.SHORT;
import static java.util.Locale.US;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.echocat.locela.api.java.testing.BaseMatchers.is;

public class DateTimeFormatterUnitTest {

    protected static final DateFormat NULL_FORMAT = null;
    protected static final DateFormat DEFAULT_FORMAT = DEFAULT.toFormat(US);
    protected static final DateFormat SHORT_FORMAT = SHORT.toFormat(US);
    protected static final DateFormat MEDIUM_FORMAT = MEDIUM.toFormat(US);
    protected static final DateFormat LONG_FORMAT = LONG.toFormat(US);
    protected static final DateFormat FULL_FORMAT = FULL.toFormat(US);

    @Test
    public void testAlternativeConstructors() throws Exception {
        assertThat(new DateTimeFormatter(US, NULL_FORMAT).getFormat(), is(DEFAULT_FORMAT));
        assertThat(new DateTimeFormatter(US, NULL_FORMAT).getLocale(), is(US));

        assertThat(new DateTimeFormatter(US, DEFAULT).getFormat(), is(DEFAULT_FORMAT));
        assertThat(new DateTimeFormatter(US, SHORT).getFormat(), is(SHORT_FORMAT));
        assertThat(new DateTimeFormatter(US, MEDIUM).getFormat(), is(MEDIUM_FORMAT));
        assertThat(new DateTimeFormatter(US, LONG).getFormat(), is(LONG_FORMAT));
        assertThat(new DateTimeFormatter(US, FULL).getFormat(), is(FULL_FORMAT));
    }

    @Test
    public void testToString() throws Exception {
        assertThat(new DateTimeFormatter(US, DEFAULT).toString(), is("datetime,default"));
        assertThat(new DateTimeFormatter(US, "default").toString(), is("datetime,default"));
        assertThat(new DateTimeFormatter(US, "yyyy-MM-dd").toString(), is("datetime,yyyy-MM-dd"));
        assertThat(new DateTimeFormatter(US, new SimpleDateFormat("yyyy-MM-dd")).toString(), is("datetime,<raw>"));
    }

    @Test
    public void testToStringOfPattern() throws Exception {
        assertThat(DEFAULT.toString(), is("default"));
        assertThat(SHORT.toString(), is("short"));
        assertThat(MEDIUM.toString(), is("medium"));
        assertThat(LONG.toString(), is("long"));
        assertThat(FULL.toString(), is("full"));
    }


}
