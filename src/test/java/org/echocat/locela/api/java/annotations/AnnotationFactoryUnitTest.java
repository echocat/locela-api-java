package org.echocat.locela.api.java.annotations;

import org.echocat.locela.api.java.annotations.Annotation.Factory.Provider.UnknownAnnotationException;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.echocat.locela.api.java.testing.BaseMatchers.is;

public class AnnotationFactoryUnitTest {

    @Test
    public void testUnknownAnnotationExceptionCreation() throws Exception {
        assertThat(new UnknownAnnotationException("foo").getMessage(), is("foo"));
    }

}
