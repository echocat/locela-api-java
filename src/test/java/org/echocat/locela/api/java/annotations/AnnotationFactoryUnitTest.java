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

package org.echocat.locela.api.java.annotations;

import org.echocat.locela.api.java.annotations.Annotation.Factory.Provider.UnknownAnnotationException;
import org.junit.Test;

import static org.echocat.jomon.testing.Assert.assertThat;
import static org.echocat.jomon.testing.BaseMatchers.is;

public class AnnotationFactoryUnitTest {

    @Test
    public void testUnknownAnnotationExceptionCreation() throws Exception {
        assertThat(new UnknownAnnotationException("foo").getMessage(), is("foo"));
    }

}