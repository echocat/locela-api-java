package org.echocat.locela.api.java.messages;

import org.echocat.locela.api.java.format.FormatterFactory;
import org.echocat.locela.api.java.properties.Properties;
import org.echocat.locela.api.java.properties.Property;
import org.echocat.locela.api.java.utils.ConvertingIterator;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Iterator;
import java.util.Locale;

public class PropertiesMessages extends MessagesSupport {

    @Nonnull
    public static PropertiesMessages messagesFor(@Nullable Locale locale, @Nonnull Properties<?> properties) {
        return messagesFor(locale, properties, null);
    }

    @Nonnull
    public static PropertiesMessages messagesFor(@Nullable Locale locale, @Nonnull Properties<?> properties, @Nullable FormatterFactory<?> formatterFactory) {
        return new PropertiesMessages(locale, properties, formatterFactory);
    }

    @Nullable
    private final Locale _locale;
    @Nonnull
    private final Properties<?> _properties;
    @Nullable
    private final FormatterFactory<?> _formatterFactory;

    public PropertiesMessages(@Nullable Locale locale, @Nonnull Properties<?> properties, @Nullable FormatterFactory<?> formatterFactory) {
        _locale = locale;
        _properties = properties;
        _formatterFactory = formatterFactory;
    }

    @Nonnull
    @Override
    public Message find(@Nonnull String id) {
        final Property<?> property = properties().get(id);
        return property != null ? propertyMessageFor(property) : null;
    }

    @Nonnull
    protected PropertyMessage propertyMessageFor(@Nonnull Property<?> property) {
        return new PropertyMessage(locale(), property, formatterFactory());
    }

    @Override
    public boolean contains(@Nonnull String id) {
        return properties().contains(id);
    }

    @Override
    public Iterator<Message> iterator() {
        // noinspection unchecked
        return new ConvertingIterator<Property<?>, Message>((Iterator)properties().iterator()) { @Override protected Message convert(@Nonnull Property<?> input) {
            return propertyMessageFor(input);
        }};
    }

    @Nonnull
    protected Properties<?> properties() {
        return _properties;
    }

    @Nullable
    protected FormatterFactory<?> formatterFactory() {
        return _formatterFactory;
    }

    @Nullable
    protected Locale locale() {
        return _locale;
    }

}
