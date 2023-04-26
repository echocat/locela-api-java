package org.echocat.locela.api.java.properties;

import org.echocat.locela.api.java.properties.Line.CommentLine;
import org.echocat.locela.api.java.properties.Line.EmptyLine;
import org.echocat.locela.api.java.properties.Line.LineWithContent;
import org.echocat.locela.api.java.properties.Line.PropertyLine;
import org.echocat.locela.api.java.annotations.Annotation;
import org.echocat.locela.api.java.annotations.AnnotationsParser;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.IOException;
import java.io.Reader;

public class StandardPropertiesReader implements PropertiesReader {

    private static final StandardPropertiesReader INSTANCE = new StandardPropertiesReader();

    @Nonnull
    public static StandardPropertiesReader propertiesReader() {
        return INSTANCE;
    }

    @Nonnull
    private final AnnotationsParser _annotationsParser;
    @Nonnull
    private final PropertyParser _propertyParser;

    public StandardPropertiesReader() {
        this(null, null);
    }

    public StandardPropertiesReader(@Nullable AnnotationsParser annotationsParser, @Nullable PropertyParser propertyParser) {
        _annotationsParser = annotationsParser != null ? annotationsParser : AnnotationsParser.annotationsParser();
        _propertyParser = propertyParser != null ? propertyParser : StandardPropertyParser.propertyParser();
    }

    @Nonnull
    @Override
    public Properties<String> read(@Nonnull Reader reader) throws IOException {
        return read(new StandardPropertiesLineReader(reader));
    }

    @Nonnull
    public Properties<String> read(@Nonnull PropertiesLineReader reader) throws IOException {
        final StandardProperties<String> properties = new StandardProperties<>();
        final StringBuilder commentLines = new StringBuilder();
        boolean commentFound = false;
        boolean propertyFound = false;
        boolean headCommentAdded = false;
        Line line = reader.read();
        while (line != null) {
            if (line instanceof CommentLine) {
                commentFound = true;
                commentLines.append(((LineWithContent) line).getContent()).append('\n');
            } else if (line instanceof PropertyLine) {
                propertyFound = true;
                final Iterable<Annotation> annotations = toAnnotations(commentLines.toString());
                commentLines.setLength(0);
                final String content = ((LineWithContent) line).getContent();
                final Property<String> property = propertyParser().parse(content, annotations);
                properties.add(property);
            } else if (line instanceof EmptyLine && commentFound && !propertyFound && !headCommentAdded) {
                headCommentAdded = true;
                final Iterable<Annotation> annotations = toAnnotations(commentLines.toString());
                commentLines.setLength(0);
                properties.withAnnotations(annotations);
            }
            line = reader.read();
        }
        if (commentFound && !propertyFound && !headCommentAdded) {
            final Iterable<Annotation> annotations = toAnnotations(commentLines.toString());
            commentLines.setLength(0);
            properties.withAnnotations(annotations);
        }
        return properties;
    }

    @Nonnull
    protected Iterable<Annotation> toAnnotations(@Nonnull String comment) {
        return annotationsParser().parse(comment);
    }

    @Nonnull
    protected AnnotationsParser annotationsParser() {
        return _annotationsParser;
    }

    @Nonnull
    protected PropertyParser propertyParser() {
        return _propertyParser;
    }
}
