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

import org.echocat.locela.api.java.annotations.AnnotationContainerSupport;

import javax.annotation.concurrent.NotThreadSafe;

@NotThreadSafe
public abstract class PropertySupport<V> extends AnnotationContainerSupport implements Property<V> {

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append(getId()).append(": ").append(get());
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        final boolean result;
        if (this == o) {
            result = true;
        } else if (o == null || !(o instanceof Property)) {
            result = false;
        } else {
            final Property<?> that = (Property) o;
            if (getId().equals(that.getId())) {
                final V content = get();
                result = content != null ? content.equals(that.get()) : that.get() == null;
            } else {
                result = false;
            }
        }
        return result;
    }

    @Override
    public int hashCode() {
        final V content = get();
        return 31
            * getId().hashCode()
            * (content != null ? content.hashCode() : 0);
    }

}
