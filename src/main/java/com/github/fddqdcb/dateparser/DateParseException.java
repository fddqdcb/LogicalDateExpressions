package com.github.fddqdcb.dateparser;


/**
 *
 * @author
 */
public class DateParseException extends RuntimeException
{

    public DateParseException()
    {
        super();
    }


    public DateParseException(String msg)
    {
        super(msg);
    }


    public DateParseException(Throwable t)
    {
        super(t);
    }


    public DateParseException(String msg, Throwable t)
    {
        super(msg, t);
    }

}
