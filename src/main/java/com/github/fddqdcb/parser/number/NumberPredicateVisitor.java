package com.github.fddqdcb.parser.number;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 *
 * @author
 */
public class NumberPredicateVisitor extends LogicalNumberExpressionsBaseVisitor<Predicate>
{

    private static final Logger LOG = LoggerFactory.getLogger(NumberPredicateVisitor.class);

    private final CriteriaBuilder builder;
    private final Expression expression;


    public NumberPredicateVisitor(CriteriaBuilder builder, Expression expression)
    {
        this.builder = builder;
        this.expression = expression;
    }


    @Override
    public Predicate visitNotExpr(LogicalNumberExpressionsParser.NotExprContext ctx)
    {
        return builder.not(visit(ctx.expr()));
    }


    @Override
    public Predicate visitAndExpr(LogicalNumberExpressionsParser.AndExprContext ctx)
    {
        return builder.and(
                visit(ctx.leftExpr),
                visit(ctx.rightExpr)
        );
    }


    @Override
    public Predicate visitOrExpr(LogicalNumberExpressionsParser.OrExprContext ctx)
    {
        return builder.or(
                visit(ctx.leftExpr),
                visit(ctx.rightExpr)
        );
    }


    @Override
    public Predicate visitSingleNumberExpr(LogicalNumberExpressionsParser.SingleNumberExprContext ctx)
    {
        return builder.equal(expression, getNumber(ctx.getText()));
    }


    @Override
    public Predicate visitCompareNumberExpr(LogicalNumberExpressionsParser.CompareNumberExprContext ctx)
    {
        if(ctx.GT() != null)
        {
            return builder.greaterThan(expression, getNumber(ctx.number().getText()));
        }
        else if(ctx.GE() != null)
        {
            return builder.greaterThanOrEqualTo(expression, getNumber(ctx.number().getText()));
        }
        else if(ctx.LT() != null)
        {
            return builder.lessThan(expression, getNumber(ctx.number().getText()));
        }
        else if(ctx.LE() != null)
        {
            return builder.lessThanOrEqualTo(expression, getNumber(ctx.number().getText()));
        }
        throw new IllegalStateException("compareNumberExpr is set but no compare-expression is givens");
    }


    @Override
    public Predicate visitFromToNumberExpr(LogicalNumberExpressionsParser.FromToNumberExprContext ctx)
    {
        // 05.2010 - 02.2012 -> (ge lower fromDate) and (lt upper toDate + (1 day))
        Comparable fromDate = getNumber(ctx.fromNumber.getText());
        Comparable toDate = getNumber(ctx.toNumber.getText());
        return builder.and(
                builder.greaterThanOrEqualTo(expression, fromDate),
                builder.lessThanOrEqualTo(expression, toDate)
        );
    }


    @Override
    protected Predicate aggregateResult(Predicate aggregate, Predicate nextResult)
    {
        return nextResult == null ? aggregate : nextResult;
    }


    private Comparable getNumber(String input)
    {
        return Double.parseDouble(input);
    }
}
