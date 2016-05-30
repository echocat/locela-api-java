/*****************************************************************************************
 * *** BEGIN LICENSE BLOCK *****
 *
 * Version: MPL 2.0
 *
 * echocat Locela - API for Java, Copyright (c) 2014 echocat
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 *
 * *** END LICENSE BLOCK *****
 ****************************************************************************************/

package org.echocat.locela.api.java.format;

import org.echocat.locela.api.java.format.ChoiceFormatter.Condition;
import org.echocat.locela.api.java.format.ChoiceFormatter.Extraction;
import org.junit.Test;

import static org.echocat.locela.api.java.format.ChoiceFormatter.Operator.equals;
import static org.echocat.locela.api.java.format.ChoiceFormatter.Operator.greaterThan;
import static java.util.Locale.US;
import static org.echocat.locela.api.java.support.CollectionUtils.asList;
import static org.echocat.locela.api.java.testing.Assert.assertThat;
import static org.echocat.locela.api.java.testing.BaseMatchers.is;
import static org.mockito.Mockito.mock;

public class ChoiceFormatterUnitTest {

    @Test
    public void testToString() throws Exception {
        final Formatter formatterA = mock(Formatter.class, "a");
        final Formatter formatterB = mock(Formatter.class, "b");
        final ChoiceFormatter choiceFormatter = new ChoiceFormatter(US, asList(
            new Condition("1", equals, formatterA),
            new Condition("2", greaterThan, formatterB)
        ));
        assertThat(choiceFormatter.toString(), is("choice,1#a|2<b"));
    }

    @Test
    public void testOperatorToString() throws Exception {
        assertThat(equals.toString(), is("#"));
        assertThat(greaterThan.toString(), is("<"));
    }



    @Test(expected = IllegalArgumentException.class)
    public void testUnsupportedOperator() throws Exception {
        new ChoiceFormatter(US, "1>b", mock(FormatterFactory.class));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUnexpectedEndOfCondition() throws Exception {
        new ChoiceFormatter(US, "1|", mock(FormatterFactory.class));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUnexpectedEndOfPattern() throws Exception {
        new ChoiceFormatter(US, "1<", mock(FormatterFactory.class));
    }

    @Test
    public void testExtractionCreation() throws Exception {
        final Extraction extraction = new Extraction("foo", 1, 2);
        assertThat(extraction.getContent(), is("foo"));
        assertThat(extraction.getBegin(), is(1));
        assertThat(extraction.getEnd(), is(2));
    }

    @Test
    public void testConditionCreation() throws Exception {
        final Formatter subFormat = mock(Formatter.class);
        final Condition condition = new Condition("foo", equals, subFormat);
        assertThat((String) condition.getTest(), is("foo"));
        assertThat(condition.getOperator(), is(equals));
        assertThat(condition.getSubFormat(), is(subFormat));
    }

}
