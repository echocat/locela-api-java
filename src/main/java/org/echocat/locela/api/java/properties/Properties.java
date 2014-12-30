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

package org.echocat.locela.api.java.properties;

import org.echocat.locela.api.java.annotations.AnnotationContainer;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface Properties<V> extends Iterable<Property<V>>, AnnotationContainer {

    @Nullable
    public Property<V> get(@Nonnull String id);

    public boolean contains(@Nonnull String id);

    public void add(@Nonnull Property<? extends V> property);

    public void remove(@Nonnull String id);

}
