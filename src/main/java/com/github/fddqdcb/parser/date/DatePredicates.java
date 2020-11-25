package com.github.fddqdcb.parser.date;

import com.github.fddqdcb.parser.ThrowExceptionErrorListener;
import com.github.fddqdcb.parser.number.NumberPredicates;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.TokenStream;


/**
 *
 * @author
 */
public final class DatePredicates
{

    private DatePredicates()
    {
    }


    /**
     * Creates a {@link Predicate} for a given {@link Expression} based on a user input. If the user input can not be
     * interpreded a a valid input, a {@link RecognitionException} is thrown. <br>
     * Valid inputs are (full) dates in the european format DD.MM.YYYY (e. g. 14.07.2001, 7.8.2001, 14.7.01), month/year
     * combinations (e. g. 05.2002 for may of 2002, 4.03 for april of 2003) and whole years (e. g. 2001, 1957, 16).
     * These dates can be expresed as simple dates (e. g. 15.12.2001), as compare expressions of dates (e. g.
     * &lt;15.03.2001, &gt;=05.2005, &lt;2015), as logical expresions of dates (e. g. 15.03.2001 or 05.2001, (15.3.2001
     * or 17.4.2001) and &lt;=03.2001), as ranges of dates (e. g. 15.07.2003-23.07.2003, 07.2001-11.2001,
     * 2003-20.05.2005) or as negated expressions of all of these (e. g. !03.2001, !&gt;2003, !10.3.01-20.3.01).
     *
     * @param input user input
     * @param builder {@link CriteriaBuilder} on which the {@link Predicate}s should be created
     * @param expression databasefield on which the expression should be applied
     * @return {@link Predicate} expressing the conditions of the user input
     * @throws RecognitionException if user input is not parsable, if no exception should be thrown use
     * {@link NumberPredicates#createFailsafePredicateForNumberExpression}
     */
    public static Predicate createPredicateForDateExpression(String input, CriteriaBuilder builder,
            Expression expression) throws RecognitionException
    {
        CharStream charStream = CharStreams.fromString(input);
        LogicalDateExpressionsLexer lexer = new LogicalDateExpressionsLexer(charStream);
        lexer.removeErrorListeners();
        lexer.addErrorListener(new ThrowExceptionErrorListener());
        TokenStream tokens = new CommonTokenStream(lexer);
        LogicalDateExpressionsParser parser = new LogicalDateExpressionsParser(tokens);
        parser.removeErrorListeners();
        parser.addErrorListener(new ThrowExceptionErrorListener());
        DatePredicateVisitor visitor = new DatePredicateVisitor(builder, expression);
        Predicate pred = visitor.visit(parser.stmt());
        return pred;
    }


    /**
     * Creates a {@link Predicate} for a given {@link Expression} based on a user input. If the user input can not be
     * interpreded a a valid input, a {@link CriteriaBuilder#disjunction()} is returned, so that the predicate returns
     * false. <br>
     * Valid inputs are (full) dates in the european format DD.MM.YYYY (e. g. 14.07.2001, 7.8.2001, 14.7.01), month/year
     * combinations (e. g. 05.2002 for may of 2002, 4.03 for april of 2003) and whole years (e. g. 2001, 1957, 16).
     * These dates can be expresed as simple dates (e. g. 15.12.2001), as compare expressions of dates (e. g.
     * &lt;15.03.2001, &gt;=05.2005, &lt;2015), as logical expresions of dates (e. g. 15.03.2001 or 05.2001, (15.3.2001
     * or 17.4.2001) and &lt;=03.2001), as ranges of dates (e. g. 15.07.2003-23.07.2003, 07.2001-11.2001,
     * 2003-20.05.2005) or as negated expressions of all of these (e. g. !03.2001, !&gt;2003, !10.3.01-20.3.01).
     *
     * @param input user input
     * @param builder {@link CriteriaBuilder} on which the {@link Predicate}s should be created
     * @param expression databasefield on which the expression should be applied
     * @return {@link Predicate} expressing the conditions of the user input
     * @throws RecognitionException if user input is not parsable, if no exception should be thrown use
     * {@link NumberPredicates#createFailsafePredicateForNumberExpression}
     */
    public static Predicate createFailsafePredicateForDateExpression(String input, CriteriaBuilder builder,
            Expression expression) throws RecognitionException
    {
        try
        {
            return createPredicateForDateExpression(input, builder, expression);
        }
        catch(RecognitionException e)
        {
            return builder.disjunction();
        }
    }
}
