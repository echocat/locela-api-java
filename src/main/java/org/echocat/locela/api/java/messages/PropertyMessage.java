package org.echocat.locela.api.java.messages;

import org.echocat.locela.api.java.format.FormatterFactory;
import org.echocat.locela.api.java.properties.Property;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Locale;

public class PropertyMessage extends FormattingMessageSupport {

    @Nonnull
    public static PropertyMessage messageFor(@Nullable Locale locale, @Nonnull Property<?> property) {
        return messageFor(locale, property, null);
    }

    @Nonnull
    public static PropertyMessage messageFor(@Nullable Locale locale, @Nonnull Property<?> property, @Nullable FormatterFactory<?> formatterFactory) {
        return new PropertyMessage(locale, property, formatterFactory);
    }

    @Nullable
    private final Locale _locale;
    @Nonnull
    private final Property<?> _property;

    public PropertyMessage(@Nullable Locale locale, @Nonnull Property<?> property, @Nullable FormatterFactory<?> formatterFactory) {
        super(formatterFactory);
        _locale = locale;
        _property = property;
    }

    @Nullable
    @Override
    public String getId() {
        return property().getId();
    }

    @Nonnull
    @Override
    public String get() {
        final Object plain = property().get();
        return plain != null ? plain.toString() : "";
    }

    @Nullable
    @Override
    public Locale getLocale() {
        return _locale;
    }

    @Nonnull
    public Property<?> property() {
        return _property;
    }

}
