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


import org.echocat.locela.api.java.testing.IterableMatchers;
import org.junit.Test;

import javax.annotation.Nullable;
import java.util.Iterator;

import static org.echocat.locela.api.java.support.CollectionUtils.asList;
import static org.echocat.locela.api.java.testing.Assert.assertThat;
import static org.echocat.locela.api.java.testing.Assert.fail;
import static org.echocat.locela.api.java.testing.BaseMatchers.is;

public class AnnotationContainerSupportUnitTest {

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
    public void testFilter() throws Exception {
        final ContainerImpl container = new ContainerImpl(
            FOO_ANNOTATION1,
            FOO_ANNOTATION2,
            BAR_ANNOTATION1,
            BAR_ANNOTATION2,
            COMMENT_ANNOTATION1,
            COMMENT_ANNOTATION2
        );
        assertThat(container.annotations(Annotation.class), IterableMatchers.isEqualTo(
            FOO_ANNOTATION1,
            FOO_ANNOTATION2,
            BAR_ANNOTATION1,
            BAR_ANNOTATION2,
            COMMENT_ANNOTATION1,
            COMMENT_ANNOTATION2
        ));
        assertThat(container.annotations(FooAnnotation.class), IterableMatchers.isEqualTo(
            FOO_ANNOTATION1,
            FOO_ANNOTATION2
        ));
        // noinspection rawtypes
        assertThat(container.annotations(GenericAnnotation.class), IterableMatchers.<GenericAnnotation>isEqualTo());
    }

    @Test
    public void testAdd() throws Exception {
        final ContainerImpl container = new ContainerImpl();
        assertThat(container.annotations(), IterableMatchers.<Annotation>isEqualTo());

        container.addAnnotation(FOO_ANNOTATION1);
        container.addAnnotation(FOO_ANNOTATION2);
        container.addAnnotation(FOO_ANNOTATION3);
        assertThat(container.annotations(), IterableMatchers.<Annotation>isEqualTo(
            FOO_ANNOTATION1,
            FOO_ANNOTATION2,
            FOO_ANNOTATION3
        ));

        container.addAnnotations(BAR_ANNOTATION1, BAR_ANNOTATION2, BAR_ANNOTATION3);
        assertThat(container.annotations(), IterableMatchers.isEqualTo(
            FOO_ANNOTATION1,
            FOO_ANNOTATION2,
            FOO_ANNOTATION3,
            BAR_ANNOTATION1,
            BAR_ANNOTATION2,
            BAR_ANNOTATION3
        ));

        container.addAnnotations(asList(COMMENT_ANNOTATION1, COMMENT_ANNOTATION2, COMMENT_ANNOTATION3));
        assertThat(container.annotations(), IterableMatchers.isEqualTo(
            FOO_ANNOTATION1,
            FOO_ANNOTATION2,
            FOO_ANNOTATION3,
            BAR_ANNOTATION1,
            BAR_ANNOTATION2,
            BAR_ANNOTATION3,
            COMMENT_ANNOTATION1,
            COMMENT_ANNOTATION2,
            COMMENT_ANNOTATION3
        ));
    }

    @Test
    public void testRemoveOnFilter() throws Exception {
        final ContainerImpl container = new ContainerImpl(
            FOO_ANNOTATION1,
            FOO_ANNOTATION2,
            BAR_ANNOTATION1,
            BAR_ANNOTATION2,
            COMMENT_ANNOTATION1,
            COMMENT_ANNOTATION2
        );
        final Iterator<FooAnnotation> i = container.annotations(FooAnnotation.class).iterator();

        try {
            i.remove();
            fail("Expected exception missing.");
        } catch (final IllegalStateException ignored){}

        assertThat(i.hasNext(), is(true));
        assertThat(i.next(), is(FOO_ANNOTATION1));
        i.remove();
        assertThat(container.annotations(), IterableMatchers.isEqualTo(
            FOO_ANNOTATION2,
            BAR_ANNOTATION1,
            BAR_ANNOTATION2,
            COMMENT_ANNOTATION1,
            COMMENT_ANNOTATION2
        ));

        assertThat(i.hasNext(), is(true));
        assertThat(i.next(), is(FOO_ANNOTATION2));
        i.remove();
        assertThat(container.annotations(), IterableMatchers.isEqualTo(
            BAR_ANNOTATION1,
            BAR_ANNOTATION2,
            COMMENT_ANNOTATION1,
            COMMENT_ANNOTATION2
        ));

        assertThat(i.hasNext(), is(false));

    }

    @Test
    public void testRemoveByType() throws Exception {
        final ContainerImpl container = new ContainerImpl(
            FOO_ANNOTATION1,
            FOO_ANNOTATION2,
            FOO_ANNOTATION3,
            BAR_ANNOTATION1,
            BAR_ANNOTATION2,
            BAR_ANNOTATION3,
            COMMENT_ANNOTATION1,
            COMMENT_ANNOTATION2,
            COMMENT_ANNOTATION3
        );
        assertThat(container.annotations(), IterableMatchers.isEqualTo(
            FOO_ANNOTATION1,
            FOO_ANNOTATION2,
            FOO_ANNOTATION3,
            BAR_ANNOTATION1,
            BAR_ANNOTATION2,
            BAR_ANNOTATION3,
            COMMENT_ANNOTATION1,
            COMMENT_ANNOTATION2,
            COMMENT_ANNOTATION3
        ));

        container.removeAnnotations(BarAnnotation.class);
        assertThat(container.annotations(), IterableMatchers.isEqualTo(
            FOO_ANNOTATION1,
            FOO_ANNOTATION2,
            FOO_ANNOTATION3,
            COMMENT_ANNOTATION1,
            COMMENT_ANNOTATION2,
            COMMENT_ANNOTATION3
        ));

        container.removeAnnotations(FooAnnotation.class);
        assertThat(container.annotations(), IterableMatchers.<Annotation>isEqualTo(
            COMMENT_ANNOTATION1,
            COMMENT_ANNOTATION2,
            COMMENT_ANNOTATION3
        ));

        container.removeAnnotations(FooAnnotation.class);
        assertThat(container.annotations(), IterableMatchers.<Annotation>isEqualTo(
            COMMENT_ANNOTATION1,
            COMMENT_ANNOTATION2,
            COMMENT_ANNOTATION3
        ));

        container.removeAnnotations(CommentAnnotation.class);
        assertThat(container.annotations(), IterableMatchers.<Annotation>isEqualTo());

        container.removeAnnotations(CommentAnnotation.class);
        assertThat(container.annotations(), IterableMatchers.<Annotation>isEqualTo());
    }

    protected static class ContainerImpl extends AnnotationContainerSupport {

        public ContainerImpl(@Nullable Annotation... annotations) {
            addAnnotations(annotations);
        }
    }
}