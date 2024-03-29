package org.echocat.locela.api.java.annotations;

import org.echocat.locela.api.java.annotations.Annotation.Factory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.ThreadSafe;
import java.util.*;

import static org.echocat.locela.api.java.utils.CollectionUtils.asImmutableSet;
import static org.echocat.locela.api.java.utils.CollectionUtils.asList;

@ThreadSafe
public class StandardAnnotationFactoryProvider implements Annotation.Factory.Provider {

    private static final Annotation.Factory.Provider INSTANCE = new StandardAnnotationFactoryProvider(findDefaultFactories());

    @Nonnull
    public static Annotation.Factory.Provider annotaionFactoryProvider() {
        return INSTANCE;
    }

    @Nonnull
    private final Map<String, Factory<? extends Annotation>> _idToFactory;
    @Nonnull
    private final Map<Class<? extends Annotation>, Factory<? extends Annotation>> _typeToFactory;

    @SafeVarargs
    public StandardAnnotationFactoryProvider(@Nullable Factory<? extends Annotation>... providers) {
        this(asList(providers));
    }

    public StandardAnnotationFactoryProvider(@Nullable Iterable<Factory<? extends Annotation>> providers) {
        _idToFactory = new LinkedHashMap<>();
        _typeToFactory = new LinkedHashMap<>();
        if (providers != null) {
            for (final Factory<? extends Annotation> provider : providers) {
                _idToFactory.put(provider.getId(), provider);
                _typeToFactory.put(provider.getResponsibleFor(), provider);
            }
        }
    }

    @Nonnull
    @Override
    public <T extends Annotation> Factory<T> provideBy(@Nonnull Class<T> type) throws UnknownAnnotationException {
        // noinspection unchecked
        final Factory<T> result = (Factory<T>) _typeToFactory.get(type);
        if (result == null) {
            throw new UnknownAnnotationException("Could not find any factory that could handle annotation of type '" + type.getName() + "'.");
        }
        return result;
    }

    @Nonnull
    @Override
    public Factory<? extends Annotation> provideBy(@Nonnull String id) throws UnknownAnnotationException {
        final Factory<? extends Annotation> result = _idToFactory.get(id);
        if (result == null) {
            throw new UnknownAnnotationException("Could not find any factory that could handle annotation of id '" + id + "'.");
        }
        return result;
    }

    @Override
    public Iterator<Factory<? extends Annotation>> iterator() {
        return asImmutableSet(_idToFactory.values()).iterator();
    }

    @Nonnull
    protected static Iterable<Factory<? extends Annotation>> findDefaultFactories() {
        // noinspection unchecked
        return (Iterable<Factory<? extends Annotation>>) (Iterable) ServiceLoader.load(Factory.class);
    }

}
