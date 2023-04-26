package org.echocat.locela.api.java.properties;

import org.echocat.locela.api.java.annotations.Annotation;
import org.echocat.locela.api.java.annotations.FooAnnotation;
import org.junit.Test;

import static org.echocat.locela.api.java.utils.CollectionUtils.asList;
import static org.echocat.locela.api.java.testing.BaseMatchers.is;
import static org.echocat.locela.api.java.testing.IterableMatchers.isEqualTo;
import static org.echocat.locela.api.java.properties.StandardProperty.property;
import static org.hamcrest.MatcherAssert.assertThat;

public class StandardPropertiesUnitTest {

    protected static final Property<String> PROP1 = property("1", String.class).set("a");
    protected static final Property<String> PROP2 = property("2", String.class).set("b");
    protected static final Property<String> PROP3 = property("3", String.class).set("c");

    protected static final Annotation ANNOTATION1 = new FooAnnotation(true, 1, null);
    protected static final Annotation ANNOTATION2 = new FooAnnotation(true, 2, null);
    protected static final Annotation ANNOTATION3 = new FooAnnotation(true, 3, null);

    @Test
    public void testAdd() throws Exception {
        final Properties<String> properties = StandardProperties.<String>properties().withProperties(
            PROP1,
            PROP2
        );
        assertThat(properties, isEqualTo(
            PROP1,
            PROP2
        ));
        properties.add(PROP3);
        assertThat(properties, isEqualTo(
            PROP1,
            PROP2,
            PROP3
        ));
    }

    @Test
    public void testWithProperties() throws Exception {
        final StandardProperties<String> properties = StandardProperties.<String>properties().withProperties(
            PROP1
        );
        assertThat(properties, isEqualTo(
            PROP1
        ));
        properties.withProperties(asList(PROP2, PROP3));
        assertThat(properties, isEqualTo(
            PROP1,
            PROP2,
            PROP3
        ));
    }

    @Test
    public void testWithAnnotations() throws Exception {
        final StandardProperties<String> properties = StandardProperties.<String>properties().withAnnotations(
            ANNOTATION1
        );
        assertThat(properties.annotations(), isEqualTo(
            ANNOTATION1
        ));
        properties.withAnnotations(asList(ANNOTATION2, ANNOTATION3));
        assertThat(properties.annotations(), isEqualTo(
            ANNOTATION1,
            ANNOTATION2,
            ANNOTATION3
        ));
    }

    @Test
    public void testGet() throws Exception {
        final Properties<String> properties = StandardProperties.<String>properties().withProperties(
            PROP1,
            PROP2
        );
        assertThat(properties.get("1"), is(PROP1));
        assertThat(properties.get("2"), is(PROP2));
        assertThat(properties.get("3"), is(null));

        properties.add(PROP3);
        assertThat(properties.get("1"), is(PROP1));
        assertThat(properties.get("2"), is(PROP2));
        assertThat(properties.get("3"), is(PROP3));
    }

    @Test
    public void testContains() throws Exception {
        final Properties<String> properties = StandardProperties.<String>properties().withProperties(
            PROP1,
            PROP2
        );
        assertThat(properties.contains("1"), is(true));
        assertThat(properties.contains("2"), is(true));
        assertThat(properties.contains("3"), is(false));

        properties.add(PROP3);
        assertThat(properties.contains("1"), is(true));
        assertThat(properties.contains("2"), is(true));
        assertThat(properties.contains("3"), is(true));
    }

    @Test
    public void testRemove() throws Exception {
        final Properties<String> properties = StandardProperties.<String>properties().withProperties(
            PROP1,
            PROP2,
            PROP3
        );
        assertThat(properties, isEqualTo(
            PROP1,
            PROP2,
            PROP3
        ));
        properties.remove("3");
        assertThat(properties, isEqualTo(
            PROP1,
            PROP2
        ));
    }

    @Test
    public void testGetProperties() throws Exception {
        final StandardProperties<String> properties = StandardProperties.<String>properties().withProperties(
            PROP1,
            PROP2,
            PROP3
        );
        // noinspection unchecked
        assertThat((Iterable<Property<String>>)properties.getProperties(), isEqualTo(
            PROP1,
            PROP2,
            PROP3
        ));
    }

}
