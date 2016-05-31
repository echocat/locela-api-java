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

import org.echocat.locela.api.java.utils.CollectionUtils;
import org.junit.Test;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.echocat.locela.api.java.testing.Assert.assertThat;
import static org.echocat.locela.api.java.testing.Assert.fail;
import static org.echocat.locela.api.java.testing.BaseMatchers.is;
import static org.echocat.locela.api.java.testing.IterableMatchers.isEqualTo;

public class PropertiesSupportUnitTest {

    @Test
    public void testToString() throws Exception {
        assertThat(new PropertiesImpl(
            "a", "1",
            "b", "2"
        ).toString(), is("{a: 1,b: 2}"));
    }

    @Test
    public void testEqualsOnSame() throws Exception {
        final PropertiesImpl properties = new PropertiesImpl(
            "a", "1",
            "b", "2"
        );
        //noinspection EqualsWithItself
        assertThat(properties.equals(properties), is(true));
    }

    @Test
    public void testEqualsOnNull() throws Exception {
        //noinspection ObjectEqualsNull
        assertThat(new PropertiesImpl(
            "a", "1",
            "b", "2"
        ).equals(null), is(false));
    }

    @Test
    public void testEqualsOnAnotherObject() throws Exception {
        assertThat(new PropertiesImpl(
            "a", "1",
            "b", "2"
        ).equals(new Object()), is(false));
    }


    @Test
    public void testEquals() throws Exception {
        assertThat(new PropertiesImpl(
            "a", "1",
            "b", "2"
        ).equals(new PropertiesImpl(
            "a", "1",
            "b", "2"
        )), is(true));

        assertThat(new PropertiesImpl(
            "a", "1",
            "b", "2"
        ).equals(new PropertiesImpl(
            "a", "1",
            "b", "3"
        )), is(false));

        assertThat(new PropertiesImpl(
            "a", "1",
            "b", "2"
        ).equals(new PropertiesImpl(
            "a", "1",
            "c", "2"
        )), is(false));
    }

    @Test
    public void testHashcode() throws Exception {
        assertThat(new PropertiesImpl(
            "a", "1",
            "b", "2"
        ).hashCode(), is(new PropertiesImpl(
            "a", "1",
            "b", "2"
        ).hashCode()));
    }

    @Test
    public void removeOnIterator() throws Exception {
        final Properties<String> reference = new PropertiesImpl(
            "a", "1",
            "b", "2",
            "c", "c"
        );
        final Properties<String> properties = new PropertiesImpl(
            "a", "1",
            "b", "2",
            "c", "c"
        );

        assertThat(properties, isEqualTo(reference));
        final Iterator<Property<String>> i = properties.iterator();

        try {
            i.remove();
            fail("Expected exception missing.");
        } catch (final IllegalStateException ignored){}

        assertThat(i.hasNext(), is(true));
        assertThat(i.next().getId(), is("a"));
        i.remove();
        reference.remove("a");
        assertThat(properties, isEqualTo(reference));

        assertThat(i.hasNext(), is(true));
        assertThat(i.next().getId(), is("b"));
        i.remove();
        reference.remove("b");
        assertThat(properties, isEqualTo(reference));

        assertThat(i.hasNext(), is(true));
        assertThat(i.next().getId(), is("c"));
        i.remove();
        reference.remove("c");
        assertThat(properties, isEqualTo(reference));

        assertThat(i.hasNext(), is(false));
    }

    protected static class PropertiesImpl extends PropertiesSupport<String> {

        @Nonnull
        private final Map<String, Property<String>> _properties;

        public PropertiesImpl(@Nullable String... idToProperty) {
            _properties = new LinkedHashMap<>();
            //noinspection ConfusingArgumentToVarargsMethod
            for (final Map.Entry<String, String> idAndProperty : CollectionUtils.<String, String>asImmutableMap(idToProperty).entrySet()) {
                final Property<String> property = new StandardProperty<>(idAndProperty.getKey(), String.class);
                property.set(idAndProperty.getValue());
                _properties.put(property.getId(), property);
            }
        }

        @Nullable
        @Override
        public Property<String> get(@Nonnull String id) {
            return _properties.get(id);
        }

        @Nonnull
        @Override
        protected Iterable<? extends Property<String>> getProperties() {
            return _properties.values();
        }

        @Override
        public boolean contains(@Nonnull String id) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void add(@Nonnull Property<? extends String> property) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void remove(@Nonnull String id) {
            _properties.remove(id);
        }

    }
}