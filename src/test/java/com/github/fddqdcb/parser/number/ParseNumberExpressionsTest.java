package com.github.fddqdcb.parser.number;

import com.github.fddqdcb.parser.ThrowExceptionErrorListener;
import com.github.fddqdcb.parser.number.LogicalNumberExpressionsParser.StmtContext;
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
public class ParseNumberExpressionsTest
{

    private static final Logger LOG = LoggerFactory.getLogger(ParseNumberExpressionsTest.class);


    @Test
    public void recorgizeTest()
    {
        recognize("-13");
        recognize("-13.4");
        recognize("-.4");
        recognize("-11.");
        recognize("2");
        recognize("509");
        recognize("5.1");
        recognize("5.");
        recognize(".12");

        recognize(">2");
        recognize(">-2");
        recognize(">=5.2");
        recognize(">=-5.2");
        recognize("<.5175");
        recognize("<-.5175");
        recognize("<=2.");
        recognize("<=-2.");

        recognize("2 and 5");
        recognize("2 AnD 5");
        recognize("-2 AnD -5");
        recognize("2.1 and .175");
        recognize("2 AnD 5 aNd .5 and 2. and 2.75");
        recognize("2 & 5");
        recognize("2 && 5");
        recognize("2 or 5");
        recognize("2 OR 5");
        recognize("2 OR 5 Or 7.8 oR .8");
        recognize("2 | 5");
        recognize("2 || 5");
        recognize("2 or 2.2 and .5 or 7. and 9.877or 20.113");
        recognize("<=2 or 5. and >7 or 9.12");
        recognize("<=-2 or -5. and >-7 or -9.12");

        recognize("2-5");
        recognize("2--5");
        recognize("2 to 5");
        recognize("-2 to -5");
        recognize("2 - 5.2");
        recognize("-2 - -5.2");
        recognize("2 TO 3.");
        recognize("-2 TO -3.");
        recognize("7.201 - 13.5");
        recognize("-7.201 - -13.5");

        recognize("(2)");
        recognize("(-2)");
        recognize("(5.2)");
        recognize("(12.)");
        recognize("(((4)))");
        recognize("(((-4.44)))");
        recognize("((12.2) or (5))");
        recognize("(2 or 5) and >5.75");

        recognize("!2");
        recognize("!-2");
        recognize("!5.3");
        recognize("!-5.3");
        recognize("!>2");
        recognize("!>-2");
        recognize("!<=5");
        recognize("!<=-5");
        recognize("!2-5.75");
        recognize("!-2--1.75");
        recognize("!(.2-1.3 or 4.5)");
        recognize("!(-.2--1.3 or -4.5)");
        recognize("!2-5 or 3.33");
        recognize("!-2- -5 or -3.33");
    }


    @Test
    public void doNotRecognizeTest()
    {
        doNotRecognize("asdf");
        doNotRecognize("@34");
        doNotRecognize("2>");
        doNotRecognize("2 and ");
        doNotRecognize("2 or or 5");
        doNotRecognize("2)");
        doNotRecognize("(2");
        doNotRecognize("2!");
    }


    private void recognize(String input)
    {
        String message = String.format("recognize '%s'", input);
        try
        {
            LogicalNumberExpressionsParser parser = parseExpression(input);

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
            LogicalNumberExpressionsParser parser = parseExpression(input);

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


    private LogicalNumberExpressionsParser parseExpression(String input)
    {
        LogicalNumberExpressionsLexer lexer = new LogicalNumberExpressionsLexer(CharStreams.fromString(input));
        lexer.removeErrorListeners();
        lexer.addErrorListener(new ThrowExceptionErrorListener());
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        LogicalNumberExpressionsParser parser = new LogicalNumberExpressionsParser(tokens);
        parser.removeErrorListeners();
        parser.addErrorListener(new ThrowExceptionErrorListener());
        return parser;
    }

}
