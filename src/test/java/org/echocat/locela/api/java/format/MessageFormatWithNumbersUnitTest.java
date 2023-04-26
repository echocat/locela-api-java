package org.echocat.locela.api.java.format;

import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.Locale;

import static org.echocat.locela.api.java.format.MessageFormatter.format;
import static java.util.Locale.GERMANY;
import static java.util.Locale.US;

@RunWith(Parameterized.class)
public class MessageFormatWithNumbersUnitTest extends BundledMessageFormatTestSupport {

    protected static final Double NUMBER1 = 1234.56789D;
    protected static final Double NUMBER2 = -6.54321D;

    @Parameters(name = "{0}:{1}")
    public static Collection<Object[]> getTests() throws Exception {
        return createParameters("numbers", US, GERMANY);
    }

    public MessageFormatWithNumbersUnitTest(@Nonnull Locale locale, @Nonnull String pattern, @Nonnull String expectedResult) {
        super(locale, pattern, expectedResult);
    }

    @Override
    protected String executeFormat(@Nonnull Locale locale, @Nonnull String pattern) throws Exception {
        return format(locale, pattern, NUMBER1, NUMBER2);
    }

}
