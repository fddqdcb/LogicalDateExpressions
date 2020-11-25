package com.github.fddqdcb.parser.number;

import com.github.fddqdcb.parser.ThrowExceptionErrorListener;
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
public final class NumberPredicates
{

    private NumberPredicates()
    {
    }


    /**
     * Creates a {@link Predicate} for a given {@link Expression} based on a user input. If the user input can not be
     * interpreded a a valid input, a {@link RecognitionException} is thrown. <br>
     * Valid inputs are numbers (e. g. 5, 201.5, -15.75), compare expressions of numbers (e. g. &gt;4, &lt;=5.5,
     * &lt;=-15.5), logical expressions of numbers (e. g. 4 or 5, &gt;=0 and &lt;=10, (5 or 7) and &gt; 6), ranges of
     * numbers (e. g. 1-5, 1.2-2.1, -10--7) or negated expressions of all of these (e. g. !7, !&lt;18, !15-19).
     *
     * @param input user input
     * @param builder {@link CriteriaBuilder} on which the {@link Predicate}s should be created
     * @param expression databasefield on which the expression should be applied
     * @return {@link Predicate} expressing the conditions of the user input
     * @throws RecognitionException if user input is not parsable, if no exception should be thrown use
     * {@link NumberPredicates#createFailsafePredicateForNumberExpression}
     */
    public static Predicate createPredicateForNumberExpression(String input, CriteriaBuilder builder,
            Expression expression) throws RecognitionException
    {
        CharStream charStream = CharStreams.fromString(input);
        LogicalNumberExpressionsLexer lexer = new LogicalNumberExpressionsLexer(charStream);
        lexer.removeErrorListeners();
        lexer.addErrorListener(new ThrowExceptionErrorListener());
        TokenStream tokens = new CommonTokenStream(lexer);
        LogicalNumberExpressionsParser parser = new LogicalNumberExpressionsParser(tokens);
        parser.removeErrorListeners();
        parser.addErrorListener(new ThrowExceptionErrorListener());
        NumberPredicateVisitor visitor = new NumberPredicateVisitor(builder, expression);
        Predicate pred = visitor.visit(parser.stmt());
        return pred;
    }


    /**
     * Creates a {@link Predicate} for a given {@link Expression} based on a user input. If the user input can not be
     * interpreded a a valid input, a {@link CriteriaBuilder#disjunction()} is returned, so that the predicate returns
     * false. <br>
     * Valid inputs are numbers (e. g. 5, 201.5, -15.75), compare expressions of numbers (e. g. &gt;4, &lt;=5.5,
     * &lt;=-15.5), logical expressions of numbers (e. g. 4 or 5, &gt;=0 and &lt;=10, (5 or 7) and &gt; 6), ranges of
     * numbers (e. g. 1-5, 1.2-2.1, -10--7) or negated expressions of all of these (e. g. !7, !&lt;18, !15-19).
     *
     * @param input user input
     * @param builder {@link CriteriaBuilder} on which the {@link Predicate}s should be created
     * @param expression databasefield on which the expression should be applied
     * @return {@link Predicate} expressing the conditions of the user input
     * @see {@link NumberPredicates#createPredicateForNumberExpression}
     */
    public static Predicate createFailsafePredicateForNumberExpression(String input, CriteriaBuilder builder,
            Expression expression)
    {
        try
        {
            return createPredicateForNumberExpression(input, builder, expression);
        }
        catch(RecognitionException e)
        {
            return builder.disjunction();
        }
    }
}
