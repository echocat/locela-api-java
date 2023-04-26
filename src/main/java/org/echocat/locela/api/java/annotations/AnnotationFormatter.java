package org.echocat.locela.api.java.annotations;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.ThreadSafe;

@ThreadSafe
public class AnnotationFormatter {

    private static final AnnotationFormatter INSTANCE = new AnnotationFormatter();

    @Nonnull
    public static AnnotationFormatter annotationFormatter() {
        return INSTANCE;
    }

    @Nonnull
    public String format(@Nonnull Annotation annotation) {
        final StringBuilder sb = new StringBuilder();
        sb.append(annotation.getId()).append('(');
        boolean first = true;
        for (final Object argument : annotation.getArguments()) {
            if (first) {
                first = false;
            } else {
                sb.append(',');
            }
            formatArgument(annotation, argument, sb);
        }
        sb.append(')');
        return sb.toString();
    }

    protected void formatArgument(@Nonnull Annotation annotation, @Nonnull Object argument, @Nonnull StringBuilder to) {
        if (argument == null) {
            to.append("null");
        } else if (argument instanceof String) {
            formatString(((String) argument), to);
        } else if (argument instanceof Boolean) {
            to.append(argument);
        } else if (argument instanceof Number) {
            to.append(((Number) argument).doubleValue());
        } else {
            throw new IllegalArgumentException("Could not format arguments of type " + argument.getClass().getName() + " for annotation " + annotation.getId() + ".");
        }
    }

    protected void formatString(@Nonnull String argument, @Nonnull StringBuilder to) {
        final char[] chars = argument.toCharArray();
        to.append('"');
        for (final char c : chars) {
            if (c == '\n') {
                to.append("\\n");
            } else if (c == '\r') {
                to.append("\\r");
            } else if (c == '\t') {
                to.append("\\t");
            } else if (c == '\\') {
                to.append("\\\\");
            } else if (c == '"') {
                to.append("\\\"");
            } else {
                to.append(c);
            }
        }
        to.append('"');
    }

}
