package org.echocat.locela.api.java.annotations;

import org.echocat.locela.api.java.utils.IterationUtils.RemoveHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.echocat.locela.api.java.utils.CollectionUtils.addAll;
import static org.echocat.locela.api.java.utils.IterationUtils.toIterator;

public abstract class AnnotationContainerSupport implements AnnotationContainer {

    @Nonnull
    private final List<Annotation> _annotations = new ArrayList<>();
    @Nonnull
    private final RemoveHandler<Annotation> _removeHandler = this::removeAnnotation;

    @Nonnull
    @Override
    public List<Annotation> annotations() {
        return _annotations;
    }

    @Nonnull
    @Override
    public <T extends Annotation> Iterable<T> annotations(@Nonnull final Class<? extends T> ofType) {
        return () -> {
            final List<T> result = new ArrayList<>();
            for (final Annotation annotation : annotations()) {
                if (ofType.isInstance(annotation)) {
                    result.add(ofType.cast(annotation));
                }
            }
            // noinspection unchecked
            return toIterator((RemoveHandler<T>)_removeHandler, result);
        };
    }

    protected void addAnnotations(@Nullable Iterable<? extends Annotation> annotations) {
        // noinspection unchecked
        addAll(annotations(), (Iterable<Annotation>) annotations);
    }

    protected void addAnnotations(@Nullable Annotation... annotations) {
        addAll(annotations(), annotations);
    }

    @Override
    public void addAnnotation(@Nonnull Annotation annotation) {
        if (!annotations().contains(annotation)) {
            annotations().add(annotation);
        }
    }

    @Override
    public void removeAnnotation(@Nonnull Annotation annotation) {
        annotations().remove(annotation);
    }


    @Override
    public void removeAnnotations(@Nonnull Class<? extends Annotation> ofType) {
        final Iterator<? extends Annotation> i = annotations(ofType).iterator();
        while (i.hasNext()) {
            i.next();
            i.remove();
        }
    }

}
