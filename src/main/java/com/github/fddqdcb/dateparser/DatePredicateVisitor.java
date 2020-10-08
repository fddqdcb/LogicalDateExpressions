package com.github.fddqdcb.dateparser;

import com.github.fddqdcb.dateparser.Dates.Bound;
import java.time.LocalDate;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 *
 * @author
 */
public class DatePredicateVisitor extends LogicalDateExpressionsBaseVisitor<Predicate>
{

    private static final Logger LOG = LoggerFactory.getLogger(DatePredicateVisitor.class);

    private final CriteriaBuilder builder;
    private final Expression expression;


    public DatePredicateVisitor(CriteriaBuilder builder, Expression expression)
    {
        this.builder = builder;
        this.expression = expression;
    }


    @Override
    public Predicate visitNotExpr(LogicalDateExpressionsParser.NotExprContext ctx)
    {
        if(ctx.date() != null)
        {
            return visitChildren(ctx);
        }
        else
        {
            return builder.not(visitChildren(ctx.expr()));
        }
    }


    @Override
    public Predicate visitAndExpr(LogicalDateExpressionsParser.AndExprContext ctx)
    {
        if(ctx.parenExpr() != null)
        {
            return visitChildren(ctx);
        }
        else
        {
            return builder.and(
                    visitChildren(ctx.leftExpr),
                    visitChildren(ctx.rightExpr)
            );
        }
    }


    @Override
    public Predicate visitOrExpr(LogicalDateExpressionsParser.OrExprContext ctx)
    {
        if(ctx.andExpr() != null)
        {
            return visitChildren(ctx);
        }
        return builder.or(
                visitChildren(ctx.leftExpr),
                visitChildren(ctx.rightExpr)
        );
    }


    @Override
    public Predicate visitDate(LogicalDateExpressionsParser.DateContext ctx)
    {
        if(ctx.singleDateExpr() != null)
        {
            LogicalDateExpressionsParser.SingleDateExprContext cctx = ctx.singleDateExpr();
            // 20.05.2010 -> ge lower and lt upper+(1 day)
            // 05.2010    -> ge lower and lt upper+(1 day)
            // 2010       -> ge lower and lt upper+(1 day)
            LocalDate lower = getDate(cctx.regularDate(), Bound.lower);
            LocalDate upper = getDate(cctx.regularDate(), Bound.upper).plusDays(1);

            return builder.and(
                    builder.greaterThanOrEqualTo(expression, lower),
                    builder.lessThan(expression, upper)
            );
        }
        else if(ctx.compareDateExpr() != null)
        {
            LogicalDateExpressionsParser.CompareDateExprContext cctx = ctx.compareDateExpr();


            if(cctx.GT() != null)
            {
                // > 20.05.2010  -> gt upper
                // > 05.2010     -> gt upper
                // > 2010        -> gt upper
                LocalDate upper = getDate(cctx.regularDate(), Bound.upper);
                return builder.greaterThan(expression, upper);
            }
            else if(cctx.GE() != null)
            {
                // >= 20.05.2010 -> ge lower
                // >= 05.2010    -> ge lower
                // >= 2010       -> ge lower
                LocalDate lower = getDate(cctx.regularDate(), Bound.lower);
                return builder.greaterThanOrEqualTo(expression, lower);
            }
            else if(cctx.LT() != null)
            {
                // < 20.05.2010  -> lt lower
                // < 05.2010     -> lt lower
                // < 2010        -> lt lower
                LocalDate upper = getDate(cctx.regularDate(), Bound.lower);
                return builder.lessThan(expression, upper);
            }
            else if(cctx.LE() != null)
            {
                // <= 20.05.2010 -> lt upper+(1 day)
                // <= 05.2010    -> lt upper+(1 day)
                // <= 2010       -> lt upper+(1 day)
                LocalDate upper = getDate(cctx.regularDate(), Bound.upper).plusDays(1);
                return builder.lessThan(expression, upper);
            }
            throw new IllegalStateException(
                    String.format("compareDateExpr is set but no compare-expression is givens"));
        }
        else if(ctx.fromToDateExpr() != null)
        {
            LogicalDateExpressionsParser.FromToDateExprContext cctx = ctx.fromToDateExpr();
            // 05.2010 - 02.2012 -> (ge lower fromDate) and (lt upper toDate + (1 day))
            LocalDate fromDate = getDate(cctx.fromDate, Bound.lower);
            LocalDate toDate = getDate(cctx.toDate, Bound.upper).plusDays(1);
            return builder.and(
                    builder.greaterThanOrEqualTo(expression, fromDate),
                    builder.lessThan(expression, toDate)
            );
        }
        else
        {
            throw new IllegalStateException("date is neither singleDateExpr nor compareDateExpr nor fromToDateExpr");
        }
    }


    @Override
    protected Predicate aggregateResult(Predicate aggregate, Predicate nextResult)
    {
        return nextResult == null ? aggregate : nextResult;
    }


    private LocalDate getDate(LogicalDateExpressionsParser.RegularDateContext ctx, Bound bound)
    {
        String dayString = ctx.day() != null
                ? ctx.day().getText()
                : null;
        String monthString = ctx.month() != null
                ? ctx.month().getText()
                : null;
        String yearString = ctx.year().getText();
        LOG.debug("found regularDate: {}.{}.{}",
                dayString != null ? dayString : "-",
                monthString != null ? monthString : "-",
                yearString);
        return Dates.getDate(yearString, monthString, dayString, bound);
    }

}
