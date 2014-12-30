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

import javax.annotation.Nonnull;

public interface AnnotationContainer {

    @Nonnull
    public Iterable<Annotation> annotations();

    @Nonnull
    public <T extends Annotation> Iterable<T> annotations(@Nonnull Class<? extends T> ofType);

    public void addAnnotation(@Nonnull Annotation annotation);

    public void removeAnnotation(@Nonnull Annotation annotation);

    public void removeAnnotations(@Nonnull Class<? extends Annotation> ofType);

}
