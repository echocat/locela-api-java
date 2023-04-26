package org.echocat.locela.api.java.properties;

import org.echocat.locela.api.java.annotations.AnnotationContainer;
import org.echocat.locela.api.java.annotations.AnnotationsFormatter;
import org.echocat.locela.api.java.properties.Line.CommentLine;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.IOException;
import java.io.Writer;

public class StandardPropertiesWriter implements PropertiesWriter {

    private static final StandardPropertiesWriter INSTANCE = new StandardPropertiesWriter();

    @Nonnull
    public static StandardPropertiesWriter propertyWriter() {
        return INSTANCE;
    }

    @Nonnull
    private final PropertyFormatter _propertyFormatter;
    @Nonnull
    private final AnnotationsFormatter _annotationsFormatter;

    public StandardPropertiesWriter() {
        this(StandardPropertyFormatter.propertyFormatter(), AnnotationsFormatter.annotationsFormatter());
    }

    public StandardPropertiesWriter(@Nonnull PropertyFormatter propertyFormatter, @Nonnull AnnotationsFormatter annotationsFormatter) {
        _propertyFormatter = propertyFormatter;
        _annotationsFormatter = annotationsFormatter;
    }

    @Override
    public void write(@Nullable Properties<String> properties, @Nonnull Writer to) throws IOException {
        if (properties != null) {
            writeHead(properties, to);
            for (final Property<String> property : properties) {
                writeProperty(property, to);
            }
        }
    }

    protected void writeHead(@Nonnull Properties<String> of, @Nonnull Writer to) throws IOException {
        if (writeAnnotations(of, to)) {
            to.write('\n');
        }
    }

    protected void writeProperty(@Nonnull Property<String> property, @Nonnull Writer to) throws IOException {
        writeAnnotations(property, to);
        final String formatted = propertyFormatter().format(property);
        to.write(formatted);
        to.write('\n');
    }

    protected boolean writeAnnotations(@Nullable AnnotationContainer of, @Nonnull Writer to) throws IOException {
        final String formatted = annotationsFormatter().format(of.annotations());
        final boolean annotationsWritten;
        if (!formatted.isEmpty()) {
            final Line line = new CommentLine(formatted);
            line.write(to);
            annotationsWritten = true;
        } else {
            annotationsWritten = false;
        }
        return annotationsWritten;
    }

    @Nonnull
    public PropertyFormatter propertyFormatter() {
        return _propertyFormatter;
    }

    @Nonnull
    public AnnotationsFormatter annotationsFormatter() {
        return _annotationsFormatter;
    }

}
