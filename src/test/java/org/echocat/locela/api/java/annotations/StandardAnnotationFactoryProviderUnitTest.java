package org.echocat.locela.api.java.annotations;

import org.echocat.locela.api.java.testing.BaseMatchers;
import org.echocat.locela.api.java.testing.IterableMatchers;
import org.echocat.locela.api.java.annotations.Annotation.Factory;
import org.echocat.locela.api.java.annotations.Annotation.Factory.Provider.UnknownAnnotationException;
import org.echocat.locela.api.java.annotations.GenericAnnotation.BooleanAnnotationFactory;
import org.echocat.locela.api.java.annotations.GenericAnnotation.DoubleAnnotationFactory;
import org.echocat.locela.api.java.annotations.GenericAnnotation.StringAnnotationFactory;
import org.junit.Test;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.echocat.locela.api.java.testing.Assert.fail;
import static org.echocat.locela.api.java.testing.IterableMatchers.isEqualTo;
import static org.echocat.locela.api.java.annotations.StandardAnnotationFactoryProvider.findDefaultFactories;
import static org.hamcrest.MatcherAssert.assertThat;

public class StandardAnnotationFactoryProviderUnitTest {

    public static final FooAnnotation.Factory FOO_FACTORY = new FooAnnotation.Factory();
    public static final BarAnnotation.Factory BAR_FACTORY = new BarAnnotation.Factory();

    @Test
    public void testFindDefaultFactories() throws Exception {
        final Collection<Class<?>> typesOfFactories = extractTypesOf(findDefaultFactories());
        assertThat(typesOfFactories, IterableMatchers.containsAllItemsOf(
            CommentAnnotation.Factory.class,
            FooAnnotation.Factory.class,
            BarAnnotation.Factory.class,
            StringAnnotationFactory.class,
            BooleanAnnotationFactory.class,
            DoubleAnnotationFactory.class
        ));
    }


    @Test
    public void testProvideById() throws Exception {
        final StandardAnnotationFactoryProvider provider = new StandardAnnotationFactoryProvider(FOO_FACTORY, BAR_FACTORY);
        assertThat(provider.provideBy("foo"), BaseMatchers.is(FOO_FACTORY));
        assertThat(provider.provideBy("bar"), BaseMatchers.is(BAR_FACTORY));
        try {
            provider.provideBy("xxx");
            fail("Expected exception missing.");
        } catch (final UnknownAnnotationException ignored) {}
    }

    @Test
    public void testProvideByType() throws Exception {
        final StandardAnnotationFactoryProvider provider = new StandardAnnotationFactoryProvider(FOO_FACTORY, BAR_FACTORY);
        assertThat(provider.provideBy(FooAnnotation.class), BaseMatchers.<Factory<?>>is(FOO_FACTORY));
        assertThat(provider.provideBy(BarAnnotation.class), BaseMatchers.<Factory<?>>is(BAR_FACTORY));
        try {
            provider.provideBy(GenericAnnotation.class);
            fail("Expected exception missing.");
        } catch (final UnknownAnnotationException ignored) {}
    }

    @Test
    public void testIterator() throws Exception {
        final StandardAnnotationFactoryProvider provider = new StandardAnnotationFactoryProvider(FOO_FACTORY, BAR_FACTORY);
        assertThat(provider, isEqualTo(FOO_FACTORY, BAR_FACTORY));
    }

    @Nonnull
    protected static Collection<Class<?>> extractTypesOf(@Nonnull Iterable<?> what) {
        final List<Class<?>> result = new ArrayList<>();
        for (final Object o : what) {
            result.add(o.getClass());
        }
        return result;
    }
}
