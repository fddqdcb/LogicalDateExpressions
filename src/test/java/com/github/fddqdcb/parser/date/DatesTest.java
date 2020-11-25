package com.github.fddqdcb.parser.date;

import com.github.fddqdcb.parser.date.Dates;
import java.time.LocalDate;
import org.junit.Assert;
import org.junit.Test;


/**
 *
 * @author
 */
public class DatesTest
{

    @Test
    public void getDateTest()
    {
        Assert.assertEquals(LocalDate.of(2010, 5, 17), Dates.getDate("2010", "5", "17", Dates.Bound.lower));
        Assert.assertEquals(LocalDate.of(2010, 5, 17), Dates.getDate("2010", "5", "17", Dates.Bound.upper));
        Assert.assertEquals(LocalDate.of(2010, 1, 1), Dates.getDate("2010", "1", "1", Dates.Bound.lower));
        Assert.assertEquals(LocalDate.of(2010, 1, 1), Dates.getDate("2010", "1", "1", Dates.Bound.upper));
        Assert.assertEquals(LocalDate.of(2010, 1, 1), Dates.getDate("10", "01", "01", Dates.Bound.lower));
        Assert.assertEquals(LocalDate.of(2010, 1, 1), Dates.getDate("10", "01", "01", Dates.Bound.upper));

        Assert.assertEquals(LocalDate.of(2010, 4, 1), Dates.getDate("10", "04", null, Dates.Bound.lower));
        Assert.assertEquals(LocalDate.of(2010, 4, 30), Dates.getDate("10", "04", null, Dates.Bound.upper));
        Assert.assertEquals(LocalDate.of(2010, 5, 1), Dates.getDate("10", "05", null, Dates.Bound.lower));
        Assert.assertEquals(LocalDate.of(2010, 5, 31), Dates.getDate("10", "05", null, Dates.Bound.upper));

        Assert.assertEquals(LocalDate.of(2010, 1, 1), Dates.getDate("10", null, null, Dates.Bound.lower));
        Assert.assertEquals(LocalDate.of(2010, 12, 31), Dates.getDate("10", null, null, Dates.Bound.upper));


    }


    // Test needs to be adjusted in year 2070
    @Test
    public void getFourDigitYearTest()
    {
        Assert.assertEquals(2000, Dates.getFourDigitYear(0));
        Assert.assertEquals(2007, Dates.getFourDigitYear(7));
        Assert.assertEquals(2015, Dates.getFourDigitYear(15));
        Assert.assertEquals(2020, Dates.getFourDigitYear(20));

        Assert.assertEquals(1970, Dates.getFourDigitYear(70));
        Assert.assertEquals(1999, Dates.getFourDigitYear(99));

        Assert.assertEquals(1970, Dates.getFourDigitYear(1970));
        Assert.assertEquals(1999, Dates.getFourDigitYear(1999));
        Assert.assertEquals(2000, Dates.getFourDigitYear(2000));
        Assert.assertEquals(2007, Dates.getFourDigitYear(2007));
        Assert.assertEquals(2020, Dates.getFourDigitYear(2020));
        Assert.assertEquals(2070, Dates.getFourDigitYear(2070));
        Assert.assertEquals(2099, Dates.getFourDigitYear(2099));
    }


    // Test neeed to be adjusted in year 2100
    @Test
    public void getCurrentCenturyTest()
    {
        Assert.assertEquals(2000, Dates.getCurrentCentury());
    }
}
