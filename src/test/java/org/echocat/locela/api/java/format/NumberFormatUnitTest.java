package org.echocat.locela.api.java.format;

import org.junit.Test;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import static org.echocat.locela.api.java.format.NumberFormatter.Pattern.CURRENCY;
import static org.echocat.locela.api.java.format.NumberFormatter.Pattern.DEFAULT;
import static org.echocat.locela.api.java.format.NumberFormatter.Pattern.INTEGER;
import static org.echocat.locela.api.java.format.NumberFormatter.Pattern.PERCENT;
import static java.util.Locale.US;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.echocat.locela.api.java.testing.BaseMatchers.is;

public class NumberFormatUnitTest {

    protected static final NumberFormat NULL_FORMAT = null;
    protected static final NumberFormat DEFAULT_FORMAT = DEFAULT.toFormat(US);
    protected static final NumberFormat INTEGER_FORMAT = INTEGER.toFormat(US);
    protected static final NumberFormat PERCENT_FORMAT = PERCENT.toFormat(US);
    protected static final NumberFormat CURRENCY_FORMAT = CURRENCY.toFormat(US);

    @Test
    public void testAlternativeConstructors() throws Exception {
        assertThat(new NumberFormatter(US, NULL_FORMAT).getFormat(), is(DEFAULT_FORMAT));
        assertThat(new NumberFormatter(US, NULL_FORMAT).getLocale(), is(US));

        assertThat(new NumberFormatter(US, DEFAULT).getFormat(), is(DEFAULT_FORMAT));
        assertThat(new NumberFormatter(US, INTEGER).getFormat(), is(INTEGER_FORMAT));
        assertThat(new NumberFormatter(US, PERCENT).getFormat(), is(PERCENT_FORMAT));
        assertThat(new NumberFormatter(US, CURRENCY).getFormat(), is(CURRENCY_FORMAT));
    }

    @Test
    public void testToString() throws Exception {
        assertThat(new NumberFormatter(US, DEFAULT).toString(), is("number,default"));
        assertThat(new NumberFormatter(US, "default").toString(), is("number,default"));
        assertThat(new NumberFormatter(US, "#").toString(), is("number,#"));
        assertThat(new NumberFormatter(US, new DecimalFormat("#")).toString(), is("number,<raw>"));
    }

}
