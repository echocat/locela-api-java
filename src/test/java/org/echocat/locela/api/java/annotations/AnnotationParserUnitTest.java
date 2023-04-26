package org.echocat.locela.api.java.annotations;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.echocat.locela.api.java.testing.BaseMatchers.is;
import static org.echocat.locela.api.java.testing.BaseMatchers.isInstanceOf;
import static org.echocat.locela.api.java.annotations.AnnotationParser.annotationParser;

public class AnnotationParserUnitTest {

    protected static final AnnotationParser PARSER = annotationParser();

    @Test
    public void knowsCommentAnnotation() throws Exception {
        assertThat(((CommentAnnotation) PARSER.parse("comment(\"foo bar\")")).getContent(), is("foo bar"));
    }

    @Test
    public void whiteSpaces() throws Exception {
        final FooAnnotation annotation1 = (FooAnnotation) PARSER.parse(" foo (   true  ,\t3.4 ,\"a string\"\t)");
        assertThat(annotation1.isABoolean(), is(true));
        assertThat(annotation1.getADouble(), is(3.4D));
        assertThat(annotation1.getAString(), is("a string"));

        final FooAnnotation annotation2 = (FooAnnotation) PARSER.parse("\tfoo            (   false,\t-3.4 , \" ddd\")\t\t");
        assertThat(annotation2.isABoolean(), is(false));
        assertThat(annotation2.getADouble(), is(-3.4D));
        assertThat(annotation2.getAString(), is(" ddd"));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void booleanValues() throws Exception {
        assertThat(((GenericAnnotation<Boolean>) PARSER.parse("boolean(true)")).getValue(), is(true));
        assertThat(((GenericAnnotation<Boolean>) PARSER.parse("boolean(false)")).getValue(), is(false));

        assertThat(((GenericAnnotation<Boolean>) PARSER.parse("boolean(yes)")).getValue(), is(true));
        assertThat(((GenericAnnotation<Boolean>) PARSER.parse("boolean(no)")).getValue(), is(false));

        assertThat(((GenericAnnotation<Boolean>) PARSER.parse("boolean(on)")).getValue(), is(true));
        assertThat(((GenericAnnotation<Boolean>) PARSER.parse("boolean(off)")).getValue(), is(false));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void doubleValues() throws Exception {
        assertThat(((GenericAnnotation<Double>) PARSER.parse("double(1)")).getValue(), is(1D));
        assertThat(((GenericAnnotation<Double>) PARSER.parse("double(2)")).getValue(), is(2D));

        assertThat(((GenericAnnotation<Double>) PARSER.parse("double(-1)")).getValue(), is(-1D));
        assertThat(((GenericAnnotation<Double>) PARSER.parse("double(-2)")).getValue(), is(-2D));

        assertThat(((GenericAnnotation<Double>) PARSER.parse("double(12345.6789)")).getValue(), is(12345.6789D));
        assertThat(((GenericAnnotation<Double>) PARSER.parse("double(-9876.54321)")).getValue(), is(-9876.54321D));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void stringValues() throws Exception {
        assertThat(((GenericAnnotation<String>) PARSER.parse("string(\"a\")")).getValue(), is("a"));
        assertThat(((GenericAnnotation<String>) PARSER.parse("string(\" b \")")).getValue(), is(" b "));
        assertThat(((GenericAnnotation<String>) PARSER.parse("string(\" c\\\" \")")).getValue(), is(" c\" "));
        assertThat(((GenericAnnotation<String>) PARSER.parse("string(\" d\\n \")")).getValue(), is(" d\n "));
        assertThat(((GenericAnnotation<String>) PARSER.parse("string(\" e\\r \")")).getValue(), is(" e\r "));
        assertThat(((GenericAnnotation<String>) PARSER.parse("string(\" f\\t \")")).getValue(), is(" f\t "));
        assertThat(((GenericAnnotation<String>) PARSER.parse("string(\" g\\\\ \")")).getValue(), is(" g\\ "));
    }

    @Test
    public void nullValues() throws Exception {
        assertThat(((GenericAnnotation<?>) PARSER.parse("boolean(null)")).getValue(), is(null));
        assertThat(((GenericAnnotation<?>) PARSER.parse("double(null)")).getValue(), is(null));
        assertThat(((GenericAnnotation<?>) PARSER.parse("string(null)")).getValue(), is(null));
    }

    @Test(expected = IllegalArgumentException.class)
    public void unexpectedCharacterAfterBracket() throws Exception {
        PARSER.parse("foo(null) a");
    }

    @Test(expected = IllegalArgumentException.class)
    public void unexpectedEndAfterEscape() throws Exception {
        PARSER.parse("foo(\\");
    }

    @Test(expected = IllegalArgumentException.class)
    public void unexpectedExtraCharacterAfterFinishedLiteral() throws Exception {
        PARSER.parse("foo(a b)");
    }

    @Test(expected = IllegalArgumentException.class)
    public void unexpectedExtraStringAfterFinishedLiteral() throws Exception {
        PARSER.parse("foo(a \"b\")");
    }

    @Test(expected = IllegalArgumentException.class)
    public void missingClosingBracket() throws Exception {
        PARSER.parse("foo(a");
    }

    @Test(expected = IllegalArgumentException.class)
    public void illegalLiteral() throws Exception {
        PARSER.parse("foo(moo)");
    }

    @Test(expected = IllegalArgumentException.class)
    public void missingLiteralAfterComma() throws Exception {
        PARSER.parse("foo(1,)");
    }

    @Test(expected = IllegalArgumentException.class)
    public void missingBracketAfterId() throws Exception {
        PARSER.parse("foo");
    }

    @Test(expected = IllegalArgumentException.class)
    public void extraCharacterAfterFinishedId() throws Exception {
        PARSER.parse("foo b()");
    }

    @Test(expected = IllegalArgumentException.class)
    public void unsupportedCharacterInId() throws Exception {
        PARSER.parse("#()");
    }

    @Test(expected = IllegalArgumentException.class)
    public void emptyId() throws Exception {
        PARSER.parse("()");
    }

    @Test
    public void noParameters() throws Exception {
        assertThat(PARSER.parse("bar()"), isInstanceOf(BarAnnotation.class));
        assertThat(PARSER.parse("bar(   )"), isInstanceOf(BarAnnotation.class));
    }


}
