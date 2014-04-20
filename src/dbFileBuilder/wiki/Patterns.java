package dbFileBuilder.wiki;

import java.util.regex.Pattern;

/**
 * The <tt>Patterns</tt> class contains couple of
 * {@link  java.util.regex.Pattern Patterns} which are all static and final.<br>
 * The purpose of this class is to be used as utility class for {@link Wiki}
 */
public final class Patterns {

    /**
     * Don't let anyone instantiate this class.
     */
    private Patterns() {
    }
    /**
     * Regular Expression to match <tt>;</tt> (once or more) at the start of
     * line.<br>
     * Can be precede by <tt>:</tt> <br>
     * Note:<br>
     * <blockquote>
     * To achieve full and more accurate(too much accurate) match should use:
     * (\;+)([^:]*)(:+).
     * </blockquote> (^;{1,})
     */
    protected static final Pattern DEFINITION_LIST = Pattern.compile("(^;{1,})[^:]*:{1,}", Pattern.UNICODE_CASE);
    /**
     * Regular Expression to match <tt>:</tt> (once or more) at the start of
     * line.
     */
    protected static final Pattern INDENT = Pattern.compile("^(:+)", Pattern.UNICODE_CASE);
    /**
     * Regular Expression to match <tt>*</tt> (once or more) at the start of
     * line.
     */
    protected static final Pattern BULLET = Pattern.compile("(\\*+)", Pattern.UNICODE_CASE);
    /**
     * Regular Expression to match <tt>#</tt> (once or more) at the start of
     * line.
     */
    protected static final Pattern ORDERED_LIST = Pattern.compile("(#+)", Pattern.UNICODE_CASE);
    /**
     * Regular Expression to match <tt>----</tt> (four dashes or more) at the
     * start of line.
     */
    protected static final Pattern LINE = Pattern.compile("^-{4,}", Pattern.UNICODE_CASE);
    /**
     * Regular Expression to match(and replace) the inside of the <i>Table Of
     * Contents</i>
     *
     * @see <a
     * href="Processors#processHeading(java.lang.String)">Processors.RemoveAnotherInterpretation()</a>
     */
    protected static final Pattern TOK = Pattern.compile("\\{\\{משני\\|.{1}\\}\\}", Pattern.UNICODE_CASE);
    /**
     * Use by:
     * {@link Processors#processRegister(java.lang.String) processRegister}
     */
    protected static final Pattern REGISTER = Pattern.compile("משלב[^|]*\\|", Pattern.UNICODE_CASE);
}
