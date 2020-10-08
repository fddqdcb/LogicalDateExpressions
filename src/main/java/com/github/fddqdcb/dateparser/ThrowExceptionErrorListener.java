package com.github.fddqdcb.dateparser;

import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;
import org.antlr.v4.runtime.misc.ParseCancellationException;


/**
 *
 * @author
 */
public class ThrowExceptionErrorListener extends BaseErrorListener
{

    @Override
    public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line, int charPositionInLine,
            String msg, RecognitionException e) throws ParseCancellationException
    {
        throw new ParseCancellationException(
                String.format("error on position %s:%s (offending: %s); reason: %s",
                        line, charPositionInLine, offendingSymbol, msg));
    }
}
