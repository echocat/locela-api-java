/*****************************************************************************************
 * *** BEGIN LICENSE BLOCK *****
 *
 * Version: MPL 2.0
 *
 * echocat Locela - API for Java, Copyright (c) 2014-2015 echocat
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 *
 * *** END LICENSE BLOCK *****
 ****************************************************************************************/

package org.echocat.locela.api.java.messages;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static org.echocat.locela.api.java.messages.StandardMessagesProvider.messagesProvider;

public class RecursiveMessagesProvider extends MessagesProviderSupport {

    private static final RecursiveMessagesProvider INSTANCE = new RecursiveMessagesProvider();

    @Nonnull
    public static RecursiveMessagesProvider recursiveMessagesProvider() {
        return INSTANCE;
    }

    @Nonnull
    private final MessagesProvider _delegate;

    public RecursiveMessagesProvider() {
        this(null);
    }

    public RecursiveMessagesProvider(@Nullable MessagesProvider delegate) {
        _delegate = delegate != null ? delegate : messagesProvider();
    }

    @Nullable
    @Override
    public Messages provideBy(@Nullable Locale locale, @Nonnull FileAccessor accessor, @Nonnull String baseFile) throws IOException {
        final List<Messages> stack = new ArrayList<>();
        String current = getParent(baseFile);
        addIfExists(locale, accessor, baseFile, stack);
        while (current != null) {
            addIfExists(locale, accessor, current + "/messages.properties", stack);
            current = getParent(current);
        }
        return new CombinedMessages(stack);
    }

    protected void addIfExists(@Nullable Locale locale, @Nonnull FileAccessor accessor, @Nonnull String currentBaseFile, @Nonnull List<Messages> stack) throws IOException {
        final Messages candidate = delegate().provideBy(locale, accessor, currentBaseFile);
        if (candidate != null) {
            stack.add(candidate);
        }
    }

    @Nullable
    protected String getParent(@Nullable String of) {
        final String result;
        if (of == null || of.isEmpty()) {
            result = null;
        } else {
            final int lastSlash = of.lastIndexOf('/');
            final int lastFileSeparator = of.lastIndexOf(systemFileSeparator());
            if (lastFileSeparator >= 0 && lastFileSeparator > lastSlash) {
                result = of.substring(0, lastFileSeparator);
            } else if (lastSlash >= 0) {
                result = of.substring(0, lastSlash);
            } else {
                result = null;
            }
        }
        return result;
    }

    protected char systemFileSeparator() {
        return File.separatorChar;
    }

    @Nonnull
    protected MessagesProvider delegate() {
        return _delegate;
    }

}
