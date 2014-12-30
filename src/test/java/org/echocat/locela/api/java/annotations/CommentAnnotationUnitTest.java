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

package org.echocat.locela.api.java.annotations;

import org.junit.Test;

import static org.echocat.jomon.testing.Assert.assertThat;
import static org.echocat.jomon.testing.BaseMatchers.is;
import static org.echocat.jomon.testing.BaseMatchers.isNot;

public class CommentAnnotationUnitTest {

    @Test
    public void testEqualsOnSame() throws Exception {
        final CommentAnnotation annotation = new CommentAnnotation("abc");
        //noinspection EqualsWithItself
        assertThat(annotation.equals(annotation), is(true));
    }

    @Test
    public void testEqualsOnOtherObject() throws Exception {
        assertThat(new CommentAnnotation("abc").equals(new Object()), is(false));
    }

    @Test
    public void testEqualsOnNull() throws Exception {
        //noinspection ObjectEqualsNull
        assertThat(new CommentAnnotation("abc").equals(null), is(false));
    }

    @Test
    public void testEquals() throws Exception {
        assertThat(new CommentAnnotation("abc").equals(new CommentAnnotation("abc")), is(true));
        assertThat(new CommentAnnotation("abc").equals(new CommentAnnotation("abcd")), is(false));
    }

    @Test
    public void testHashCode() throws Exception {
        assertThat(new CommentAnnotation("abc").hashCode(), is(new CommentAnnotation("abc").hashCode()));
        assertThat(new CommentAnnotation("abc").hashCode(), isNot(new CommentAnnotation("abcd").hashCode()));
    }

    @Test
    public void testToString() throws Exception {
        assertThat(new CommentAnnotation("abc").toString(), is("Comment: abc"));
    }

    @Test
    public void factoryCreate() throws Exception {
        assertThat(new CommentAnnotation.Factory().createBy("abc").getContent(), is("abc"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void factoryCreateWithNoParameters() throws Exception {
        new CommentAnnotation.Factory().createBy();
    }

    @Test(expected = IllegalArgumentException.class)
    public void factoryCreateWithTooManyParameters() throws Exception {
        new CommentAnnotation.Factory().createBy("abc", "def");
    }

    @Test(expected = IllegalArgumentException.class)
    public void factoryCreateWithWrongParameter() throws Exception {
        new CommentAnnotation.Factory().createBy(123);
    }
}