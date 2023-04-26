package org.echocat.locela.api.java.annotations;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.ThreadSafe;

import static org.echocat.locela.api.java.utils.CollectionUtils.asList;

@ThreadSafe
public class AnnotationsFormatter {

    private static final AnnotationsFormatter INSTANCE = new AnnotationsFormatter();

    @Nonnull
    public static AnnotationsFormatter annotationsFormatter() {
        return INSTANCE;
    }

    @Nonnull
    private final AnnotationFormatter _annotationFormatter;

    public AnnotationsFormatter() {
        this(null);
    }

    public AnnotationsFormatter(@Nullable AnnotationFormatter annotationFormatter) {
        _annotationFormatter = annotationFormatter != null ? annotationFormatter : AnnotationFormatter.annotationFormatter();
    }

    @Nonnull
    public String format(@Nullable Annotation... annotations) {
        return format(asList(annotations));
    }

    @Nonnull
    public String format(@Nullable Iterable<Annotation> annotations) {
        final StringBuilder sb = new StringBuilder();
        for (final Annotation annotation : annotations) {
            if (annotation instanceof CommentAnnotation) {
                final String content = ((CommentAnnotation) annotation).getContent().trim();
                if (!content.isEmpty()) {
                    sb.append(content).append('\n');
                }
            } else {
                sb.append('@').append(annotationFormatter().format(annotation)).append('\n');
            }
        }
        return sb.toString();
    }

    @Nonnull
    protected AnnotationFormatter annotationFormatter() {
        return _annotationFormatter;
    }
}
