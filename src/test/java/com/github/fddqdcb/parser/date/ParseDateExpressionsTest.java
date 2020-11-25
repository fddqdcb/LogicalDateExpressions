package com.github.fddqdcb.parser.date;

import com.github.fddqdcb.parser.ThrowExceptionErrorListener;
import com.github.fddqdcb.parser.date.LogicalDateExpressionsParser.StmtContext;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 *
 * @author
 */
public class ParseDateExpressionsTest
{

    private static final Logger LOG = LoggerFactory.getLogger(ParseDateExpressionsTest.class);


    @Test
    public void recorgizeTest()
    {
        recognize("2010");
        recognize("10");
        recognize("05.2010");
        recognize("5.2010");
        recognize("5.10");
        recognize("07.05.2020");
        recognize("7.5.20");

        recognize(">2010");
        recognize(">=5.2010");
        recognize("<07.5.2010");
        recognize("<=7.5.10");

        recognize("2010 and 2012");
        recognize("2010 AnD 2012");
        recognize("20.1.65 and 17.09.1970");
        recognize("2010 AnD 2012 aNd 2014 and 2016");
        recognize("2010 & 2012");
        recognize("2010 && 2012");
        recognize("2010 or 2012");
        recognize("2010 OR 2012");
        recognize("2010 OR 2012 Or 2014 oR 2016");
        recognize("2010 | 2012");
        recognize("2010 || 2012");
        recognize("2010 or 2012 and 2014 or 2016 and 2018 or 2020");
        recognize("2010 and 2012 or 2014");
        recognize("1970 or 1975 and >1990 or 2000");

        recognize("2010-2012");
        recognize("2010 to 2012");
        recognize("2010 - 03.2012");
        recognize("2010 TO 03.2012");
        recognize("7.05.2010 - 2012");

        recognize("(2010)");
        recognize("(5.2010)");
        recognize("(12.12.2010)");
        recognize("(((2010)))");
        recognize("((12.12.2010) or (2012))");
        recognize("(2010 or 2012) and >05.2011");

        recognize("!24.05.2010");
        recognize("!05.2010");
        recognize("!2010");
        recognize("!>2010");
        recognize("!<=2010");
        recognize("!2010-05.2012");
        recognize("!(2010-05.2012 or 2014)");
        recognize("!2010-05.2012 or 2014");
    }


    @Test
    public void doNotRecognizeTest()
    {
        doNotRecognize("asdf");
        doNotRecognize("@34");
        doNotRecognize("2010>");
        doNotRecognize("2010 and ");
        doNotRecognize("2010 or or 2012");
        doNotRecognize("2010)");
        doNotRecognize("(2010");
        doNotRecognize("2010!");
        doNotRecognize("509");
        doNotRecognize("52010");
        doNotRecognize("50.2010");
        doNotRecognize("50.12.2010");
        doNotRecognize("05.50.2010");
        doNotRecognize("05.07.52010");
    }


    private void recognize(String input)
    {
        String message = String.format("recognize '%s'", input);
        try
        {
            LogicalDateExpressionsParser parser = parseExpression(input);

            StmtContext stmtCtx = parser.stmt();
            message += String.format("\ttree: %s", stmtCtx.toStringTree(parser));
        }
        catch(Exception e)
        {
            message += String.format("\tERROR: %s", e.getMessage());
            throw e;
        }
        finally
        {
            LOG.debug(message);
        }
    }


    private void doNotRecognize(String input)
    {
        String message = String.format("expecting '%s' to fail... ", input);
        try
        {
            LogicalDateExpressionsParser parser = parseExpression(input);

            StmtContext stmtCtx = parser.stmt();
            message += String.format("\tUps, that didn't fail... tree: %s", stmtCtx.toStringTree(parser));
            Assert.fail(String.format("expected input '%s' to fail!", input));
        }
        catch(Exception e)
        {
            message += "\tok";
        }
        finally
        {
            LOG.debug(message);
        }
    }


    private LogicalDateExpressionsParser parseExpression(String input)
    {
        LogicalDateExpressionsLexer lexer = new LogicalDateExpressionsLexer(CharStreams.fromString(input));
        lexer.removeErrorListeners();
        lexer.addErrorListener(new ThrowExceptionErrorListener());
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        LogicalDateExpressionsParser parser = new LogicalDateExpressionsParser(tokens);
        parser.removeErrorListeners();
        parser.addErrorListener(new ThrowExceptionErrorListener());
        return parser;
    }

}
