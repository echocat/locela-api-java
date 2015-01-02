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

package org.echocat.locela.api.java.format;

import org.echocat.jomon.testing.CollectionMatchers;
import org.echocat.locela.api.java.format.MessageFormatter.ParameterAwareFormatter;
import org.echocat.locela.api.java.format.MessageFormatter.PassThruFormatter;
import org.echocat.locela.api.java.format.MessageFormatter.StaticFormatter;
import org.junit.Test;

import java.io.StringWriter;

import static org.echocat.locela.api.java.format.MessageFormatter.format;
import static java.util.Locale.US;
import static org.echocat.jomon.runtime.CollectionUtils.asList;
import static org.echocat.jomon.runtime.CollectionUtils.asMap;
import static org.echocat.jomon.testing.Assert.assertThat;
import static org.echocat.jomon.testing.BaseMatchers.is;
import static org.echocat.jomon.testing.CollectionMatchers.isEqualTo;
import static org.echocat.locela.api.java.format.MessageFormatterFactory.messageFormatterFactory;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

public class MessageFormatterUnitTest {

    @Test
    public void testVararg() throws Exception {
        assertThat(format(US, "Hello {0}!", "Klaus"), is("Hello Klaus!"));
        assertThat(format(US, "Hello {0}}!", "Klaus"), is("Hello Klaus}!"));
        assertThat(format(US, "Hello {0}!", 6666), is("Hello 6666!"));
        assertThat(format(US, "Hello {0,number}!", 6666), is("Hello 6,666!"));
        assertThat(format(US, "Hello {0,number,integer}!", 6666), is("Hello 6,666!"));
        assertThat(format(US, "Hello {0,number,0}!", 6666), is("Hello 6666!"));
        assertThat(format(US, "Hello {0,number,0.0}!", 6666), is("Hello 6666.0!"));
    }

    @Test
    public void testMap() throws  Exception {
        assertThat(format(US, "Hello {0,number,0.0}!", asMap("0", 6666)), is("Hello 6666.0!"));
        assertThat(format(US, "Hello {0,number,0.0}!", asMap(0, 6666)), is("Hello 6666.0!"));
        assertThat(format(US, "Hello {aNumber,number,0.0}!", asMap("aNumber", 6666)), is("Hello 6666.0!"));
        assertThat(format(US, "Hello {aNumbe,number,0.0}!", asMap("aNumber", 6666)), is("Hello 0.0!"));
    }

    @Test
    public void testList() throws  Exception {
        assertThat(format(US, "Hello {0,number,0.0}!", asList(6666)), is("Hello 6666.0!"));
        assertThat(format(US, "Hello {1,number,0.0}!", asList(6666)), is("Hello 0.0!"));
    }

    @Test
    public void testUnknownParameterType() throws  Exception {
        final Object moo = new Object() {
            @Override
            public String toString() {
                return "moo";
            }
        };
        assertThat(new MessageFormatter(US, "The cow says {0}.").formatInternal(moo), is("The cow says moo."));
        assertThat(new MessageFormatter(US, "The cow says {1}.").formatInternal(moo), is("The cow says ."));
    }

    @Test
    public void testNullParameter() throws  Exception {
        assertThat(new MessageFormatter(US, "The cow says {0}.").formatInternal(null), is("The cow says ."));
        assertThat(new MessageFormatter(US, "The cow says {1}.").formatInternal(null), is("The cow says ."));
    }

    @Test
    public void testEscape() throws  Exception {
        assertThat(format(US, "'{0}'", 666), is("{0}"));
        assertThat(format(US, "''{0}''", 666), is("'666'"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalDepth() throws  Exception {
        format(US, "Hello {{}");
    }

    @Test
    public void testDepth() throws  Exception {
        assertThat(format(US, "Hello {{0}}", asMap("{0}", 1)), is("Hello 1"));
    }

    @Test
    public void convenienceMethodsWithWriter() throws Exception {
        final StringWriter writerA = new StringWriter();
        format(US, "a{0}b", writerA, 1);
        assertThat(writerA.toString(), is("a1b"));

        final StringWriter writerB = new StringWriter();
        format(US, "a{0}b", writerB, asList(2));
        assertThat(writerB.toString(), is("a2b"));

        final StringWriter writerC = new StringWriter();
        format(US, "a{0}b", writerC, asMap("0", 3));
        assertThat(writerC.toString(), is("a3b"));

        final StringWriter writerD = new StringWriter();
        new MessageFormatter(US, "a{0}b").format(writerD, 4);
        assertThat(writerD.toString(), is("a4b"));

        final StringWriter writerE = new StringWriter();
        new MessageFormatter(US, "a{0}b").format(writerE, asList(5));
        assertThat(writerE.toString(), is("a5b"));

        final StringWriter writerF = new StringWriter();
        new MessageFormatter(US, "a{0}b").format(writerF, asMap("0", 6));
        assertThat(writerF.toString(), is("a6b"));
    }

    @Test
    public void convenienceMethods() throws Exception {
        assertThat(format(US, "a{0}b", 1), is("a1b"));
        assertThat(format(US, "a{0}b", asList(2)), is("a2b"));
        assertThat(format(US, "a{0}b", asMap("0", 3)), is("a3b"));

        assertThat(new MessageFormatter(US, "a{0}b").format(1), is("a1b"));
        assertThat(new MessageFormatter(US, "a{0}b").format(asList(2)), is("a2b"));
        assertThat(new MessageFormatter(US, "a{0}b").format(asMap("0", 3)), is("a3b"));
    }

    @Test
    public void plainConstructor() throws Exception {
        assertThat(new MessageFormatter(US, "a{0}b").getSubFormatter(), CollectionMatchers.<Formatter>isEqualTo(
            new StaticFormatter(US, "a"),
            new ParameterAwareFormatter(US, "0", new PassThruFormatter(US)),
            new StaticFormatter(US, "b")
        ));
        assertThat(new MessageFormatter(US, "a{0}b", messageFormatterFactory()).getSubFormatter(), CollectionMatchers.<Formatter>isEqualTo(
            new StaticFormatter(US, "a"),
            new ParameterAwareFormatter(US, "0", new PassThruFormatter(US)),
            new StaticFormatter(US, "b")
        ));
    }

    @Test
    public void constructorWithFactories() throws Exception {
        final Formatter formatter = mock(Formatter.class);
        final FormatterFactory<?> formatterFactory = mock(FormatterFactory.class);
        doReturn("foo").when(formatterFactory).getId();
        doReturn(formatter).when(formatterFactory).createBy(eq(US), eq((String) null), any(FormatterFactory.class));
        assertThat(new MessageFormatter(US, "a{0,foo}b", formatterFactory).getSubFormatter(), CollectionMatchers.<Formatter>isEqualTo(
            new StaticFormatter(US, "a"),
            new ParameterAwareFormatter(US, "0", formatter),
            new StaticFormatter(US, "b")
        ));
    }

    @Test
    public void constructorForOverride() throws Exception {
        final Formatter formatter = mock(Formatter.class);

        assertThat(new MessageFormatter(US, asList(formatter)).getSubFormatter(), isEqualTo(formatter));
    }

    @Test
    public void testToString() throws Exception {
        assertThat(new MessageFormatter(US, "a{0,number}b").toString(), is("a{0,number,default}b"));
    }

    @Test
    public void testEquals() throws Exception {
        final Formatter delegate = mock(Formatter.class);
        assertThat(new MessageFormatter(US, (String) null).equals(new MessageFormatter(US, (String) null)), is(true));
        assertThat(new PassThruFormatter(US).equals(new PassThruFormatter(US)), is(true));
        assertThat(new StaticFormatter(US, "foo").equals(new StaticFormatter(US, "foo")), is(true));
        assertThat(new ParameterAwareFormatter(US, "foo", delegate).equals(new ParameterAwareFormatter(US, "foo", delegate)), is(true));
    }

    @Test
    public void testEqualsSame() throws Exception {
        final MessageFormatter messageFormatter = new MessageFormatter(US, (String) null);
        //noinspection EqualsWithItself
        assertThat(messageFormatter.equals(messageFormatter), is(true));

        final PassThruFormatter passThruFormatter = new PassThruFormatter(US);
        //noinspection EqualsWithItself
        assertThat(passThruFormatter.equals(passThruFormatter), is(true));

        final StaticFormatter staticFormatter = new StaticFormatter(US, "foo");
        //noinspection EqualsWithItself
        assertThat(staticFormatter.equals(staticFormatter), is(true));

        final ParameterAwareFormatter parameterAwareFormatter = new ParameterAwareFormatter(US, "foo", mock(Formatter.class));
        //noinspection EqualsWithItself
        assertThat(parameterAwareFormatter.equals(parameterAwareFormatter), is(true));
    }

    @Test
    public void testEqualsOtherObject() throws Exception {
        final MessageFormatter messageFormatter = new MessageFormatter(US, (String) null);
        assertThat(messageFormatter.equals(new Object()), is(false));

        final PassThruFormatter passThruFormatter = new PassThruFormatter(US);
        assertThat(passThruFormatter.equals(new Object()), is(false));

        final StaticFormatter staticFormatter = new StaticFormatter(US, "foo");
        assertThat(staticFormatter.equals(new Object()), is(false));

        final ParameterAwareFormatter parameterAwareFormatter = new ParameterAwareFormatter(US, "foo", mock(Formatter.class));
        assertThat(parameterAwareFormatter.equals(new Object()), is(false));
    }

    @Test
    public void testHashCodes() throws Exception {
        final MessageFormatter messageFormatter = new MessageFormatter(US, (String) null);
        assertThat(messageFormatter.hashCode(), is(messageFormatter.getSubFormatter().hashCode()));

        final PassThruFormatter passThruFormatter = new PassThruFormatter(US);
        assertThat(passThruFormatter.hashCode(), is(0));

        final StaticFormatter staticFormatter = new StaticFormatter(US, "foo");
        assertThat(staticFormatter.hashCode(), is("foo".hashCode()));

        final String parameter = "foo";
        final Formatter delegate = mock(Formatter.class);
        final ParameterAwareFormatter parameterAwareFormatter = new ParameterAwareFormatter(US, parameter, delegate);
        assertThat(parameterAwareFormatter.hashCode(), is(31 * parameter.hashCode() + delegate.hashCode()));
    }
}