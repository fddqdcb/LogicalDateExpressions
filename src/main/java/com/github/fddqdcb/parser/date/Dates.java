package com.github.fddqdcb.parser.date;

import com.github.fddqdcb.parser.date.DateParseException;
import java.time.LocalDate;
import java.time.YearMonth;


/**
 *
 * @author
 */
public final class Dates
{

    private static final int CENTURY = 100;


    private Dates()
    {
    }


    public static int getCurrentCentury()
    {
        int currentCentury = LocalDate.now().getYear() - (LocalDate.now().getYear() % CENTURY);
        return currentCentury;
    }


    public static int getFourDigitYear(int year)
    {
        final int FIRST_THREE_DIGIT_YEAR = 100;
        final int FIRST_FOUR_DIGIT_YEAR = 1000;
        if(year >= FIRST_THREE_DIGIT_YEAR && year < FIRST_FOUR_DIGIT_YEAR)
        {
            throw new IllegalArgumentException(String.format("unable to parse three digit year %s", year));
        }
        if(year >= FIRST_FOUR_DIGIT_YEAR)
        {
            return year;
        }
        int yearInCentury = year % CENTURY;
        int currentYearInCentury = LocalDate.now().getYear() % CENTURY;
        if(yearInCentury <= currentYearInCentury)
        {
            return getCurrentCentury() + yearInCentury;
        }
        else
        {
            return getCurrentCentury() - CENTURY + yearInCentury;
        }
    }


    public static LocalDate getDate(String yearString, String monthString, String dayString, Bound bound)
    {
        try
        {
            // year
            int year = Dates.getFourDigitYear(Integer.parseInt(yearString));

            // month
            if(monthString == null)
            {
                if(bound == Bound.upper)
                {
                    return LocalDate.of(year, 12, 31);
                }
                else
                {
                    return LocalDate.of(year, 1, 1);
                }
            }
            else
            {
                int month = Integer.parseInt(monthString);
                // day
                if(dayString == null)
                {
                    if(bound == Bound.upper)
                    {
                        return LocalDate.of(year, month, YearMonth.of(year, month).atEndOfMonth().getDayOfMonth());
                    }
                    else
                    {
                        return LocalDate.of(year, month, 1);
                    }
                }
                else
                {
                    int day = Integer.parseInt(dayString);
                    return LocalDate.of(year, month, day);
                }
            }
        }
        catch(Exception e)
        {
            throw new DateParseException(
                    String.format("unable to parse regularDate %s.%s.%s because %s: %s",
                            dayString, monthString, yearString, e.getClass().getSimpleName(), e.getMessage()));
        }
    }


    public static enum Bound
    {
        upper, lower;
    }
}
