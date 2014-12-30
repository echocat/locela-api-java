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

import org.echocat.locela.api.java.properties.Line.CommentLine;
import org.echocat.locela.api.java.properties.Line.EmptyLine;
import org.echocat.locela.api.java.properties.Line.PropertyLine;
import org.junit.Test;

import static org.echocat.jomon.testing.Assert.assertThat;
import static org.echocat.jomon.testing.BaseMatchers.is;

public class LineUnitTest {

    @Test
    public void propertyLine() throws Exception {
        assertThat(new PropertyLine("abc\ndef").toString(), is("abc\ndef\n"));
        assertThat(new PropertyLine("abc\r\ndef").toString(), is("abc\r\ndef\n"));
    }

    @Test
    public void commentLine() throws Exception {
        assertThat(new CommentLine("abc\ndef\n").toString(), is("# abc\n# def\n"));
        assertThat(new CommentLine("").toString(), is(""));
        assertThat(new CommentLine(".").toString(), is("# .\n"));
        assertThat(new CommentLine("\n").toString(), is(""));
        assertThat(new CommentLine("a\n\nb\n").toString(), is("# a\n# \n# b\n"));
        assertThat(new CommentLine("a\r\n\r\nb\r\n").toString(), is("# a\r\n# \r\n# b\n"));
    }

    @Test
    public void emptyLine() throws Exception {
        assertThat(new EmptyLine().toString(), is(""));
    }

}