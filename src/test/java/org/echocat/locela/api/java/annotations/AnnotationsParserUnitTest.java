package org.echocat.locela.api.java.annotations;

import org.echocat.locela.api.java.testing.IterableMatchers;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.echocat.locela.api.java.annotations.AnnotationsParser.annotationsParser;

public class AnnotationsParserUnitTest {

    protected static final AnnotationsParser PARSER = annotationsParser();

    @Test
    public void comment() throws Exception {
        assertThat(PARSER.parse("foo bar"), IterableMatchers.isEqualTo(
            new CommentAnnotation("foo bar")
        ));
        assertThat(PARSER.parse("foo\nbar"), IterableMatchers.isEqualTo(
            new CommentAnnotation("foo\nbar")
        ));
    }

    @Test
    public void foo() throws Exception {
        assertThat(PARSER.parse("  @foo(true,-123.456,\"bar\")"), IterableMatchers.isEqualTo(
            new FooAnnotation(true, -123.456D, "bar")
        ));
    }

    @Test
    public void bar() throws Exception {
        assertThat(PARSER.parse("  @bar()"), IterableMatchers.isEqualTo(
            new BarAnnotation()
        ));
    }

    @Test
    public void mixed() throws Exception {
        assertThat(PARSER.parse(
            "foo bar\n" +
                "@foo(false,1,\"bar\")\n" +
                "@bar()\n" +
                "@string(\"a string\")\n" +
                "@boolean(true)\n" +
                "@double(1.2)\n" +
                "another comment"
        ), IterableMatchers.isEqualTo(
            new CommentAnnotation("foo bar"),
            new FooAnnotation(false, 1D, "bar"),
            new BarAnnotation(),
            new GenericAnnotation<>("string", "a string"),
            new GenericAnnotation<>("boolean", true),
            new GenericAnnotation<>("double", 1.2D),
            new CommentAnnotation("another comment")
        ));
    }

}
