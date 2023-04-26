package org.echocat.locela.api.java.properties;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.echocat.locela.api.java.testing.BaseMatchers.is;


public class StandardPropertyUnitTest {

    @Test
    public void testGetId() throws Exception {
        assertThat(new StandardProperty<>("foo", String.class).getId(), is("foo"));
    }

    @Test
    public void testGetAndSet() throws Exception {
        final StandardProperty<String> property = new StandardProperty<>("foo", String.class);
        assertThat(property.get(), is(null));

        property.set(null);
        assertThat(property.get(), is(null));

        property.set("bar");
        assertThat(property.get(), is("bar"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetOfIllegalValue() throws Exception {
        // noinspection unchecked,rawtypes
        new StandardProperty("foo", String.class).set(123.345D);
    }

    @Test
    public void testGetType() throws Exception {
        assertThat(new StandardProperty<>("foo", String.class).getType(), is(String.class));
    }

}
