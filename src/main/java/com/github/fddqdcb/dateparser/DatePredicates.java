package com.github.fddqdcb.dateparser;

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


    public static Predicate createPredicateForLogicalDateExpression(String input, CriteriaBuilder builder,
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
}
