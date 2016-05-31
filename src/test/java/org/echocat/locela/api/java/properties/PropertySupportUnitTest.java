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

package org.echocat.locela.api.java.properties;

import org.echocat.locela.api.java.annotations.Annotation;
import org.echocat.locela.api.java.annotations.BarAnnotation;
import org.echocat.locela.api.java.annotations.CommentAnnotation;
import org.echocat.locela.api.java.annotations.FooAnnotation;
import org.junit.Test;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

import static org.echocat.locela.api.java.utils.CollectionUtils.addAll;
import static org.echocat.locela.api.java.testing.Assert.assertThat;
import static org.echocat.locela.api.java.testing.BaseMatchers.is;
import static org.echocat.locela.api.java.testing.BaseMatchers.isNot;

public class PropertySupportUnitTest {

    protected static final FooAnnotation FOO_ANNOTATION1 = new FooAnnotation(true, 1, null);
    protected static final FooAnnotation FOO_ANNOTATION2 = new FooAnnotation(true, 2, null);
    protected static final FooAnnotation FOO_ANNOTATION3 = new FooAnnotation(true, 3, null);

    protected static final BarAnnotation BAR_ANNOTATION1 = new BarAnnotation();
    protected static final BarAnnotation BAR_ANNOTATION2 = new BarAnnotation();
    protected static final BarAnnotation BAR_ANNOTATION3 = new BarAnnotation();

    protected static final CommentAnnotation COMMENT_ANNOTATION1 = new CommentAnnotation("1");
    protected static final CommentAnnotation COMMENT_ANNOTATION2 = new CommentAnnotation("2");
    protected static final CommentAnnotation COMMENT_ANNOTATION3 = new CommentAnnotation("3");

    @Test
    public void testToString() throws Exception {
        assertThat(new PropertyImpl("a", "1").toString(), is("a: 1"));
    }

    @Test
    public void testEqualsOnSame() throws Exception {
        final PropertyImpl property = new PropertyImpl("a", "1");
        //noinspection EqualsWithItself
        assertThat(property.equals(property), is(true));
    }

    @Test
    public void testEqualsOnNull() throws Exception {
        //noinspection ObjectEqualsNull
        assertThat(new PropertyImpl("a", "1").equals(null), is(false));
    }

    @Test
    public void testEqualsOnAnotherObject() throws Exception {
        assertThat(new PropertyImpl("a", "1").equals(new Object()), is(false));
    }

    @Test
    public void testEquals() throws Exception {
        assertThat(new PropertyImpl("a", "1").equals(new PropertyImpl("a", "1")), is(true));

        assertThat(new PropertyImpl("a", "1").equals(new PropertyImpl("b", "1")), is(false));
        assertThat(new PropertyImpl("a", "1").equals(new PropertyImpl("a", "2")), is(false));
    }

    @Test
    public void testHashcode() throws Exception {
        assertThat(new PropertyImpl("a", "1").hashCode(), is(new PropertyImpl("a", "1").hashCode()));

        assertThat(new PropertyImpl("a", "1").hashCode(), isNot(new PropertyImpl("a", "2").hashCode()));
    }


    protected static class PropertyImpl extends PropertySupport<String> {

        private final List<Annotation> _annotations = new ArrayList<>();
        @Nonnull
        private final String _id;
        @Nullable
        private final String _content;

        public PropertyImpl(@Nonnull String id, @Nullable String content, @Nullable Annotation... annotations) {
            _id = id;
            _content = content;
            if (annotations != null) {
                addAll(_annotations, annotations);
            }
        }

        @Nonnull
        @Override
        public String getId() {
            return _id;
        }

        @Nullable
        @Override
        public String get() {
            return _content;
        }

        @Override
        public Class<String> getType() {
            return String.class;
        }

        @Override
        public PropertyImpl set(@Nullable String content) {
            throw new UnsupportedOperationException();
        }

    }

}