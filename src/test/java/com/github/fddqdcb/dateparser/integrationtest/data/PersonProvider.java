package com.github.fddqdcb.dateparser.integrationtest.data;

import java.time.LocalDate;
import java.util.List;
import org.junit.Assert;


/**
 *
 * @author
 */
public class PersonProvider
{

    public static final Person LINDA = new Person(1L, "linda", LocalDate.of(1965, 1, 20));
    public static final Person TOM = new Person(2L, "tom", LocalDate.of(1970, 9, 17));
    public static final Person MARY = new Person(3L, "mary", LocalDate.of(1975, 2, 1));
    public static final Person JOHN = new Person(4L, "john", LocalDate.of(1980, 6, 7));
    public static final Person SANDRA = new Person(5L, "sandra", LocalDate.of(1985, 8, 19));
    public static final Person SAM = new Person(6L, "sam", LocalDate.of(1990, 11, 21));
    public static final Person BARBARA = new Person(7L, "barbara", LocalDate.of(1995, 1, 25));
    public static final Person TIM = new Person(8L, "tim", LocalDate.of(2000, 3, 31));
    public static final Person HELEN = new Person(9L, "helen", LocalDate.of(2005, 9, 13));
    public static final Person FRANK = new Person(10L, "frank", LocalDate.of(2010, 5, 15));


    public static void assertContainsPerson(String input, List<Person> foundPersonsInDatabase, Person... expected)
    {
        if(expected == null)
        {
            Assert.assertTrue(
                    String.format("with input '%s' expected no results but found %s", input, foundPersonsInDatabase),
                    foundPersonsInDatabase.isEmpty()
            );
        }
        else
        {
            for(Person p : expected)
            {
                Assert.assertTrue(
                        String.format("with input '%s' expected '%s' to be in resultlist but it was not: %s",
                                input, p, foundPersonsInDatabase),
                        foundPersonsInDatabase.contains(p)
                );
            }
        }
    }
}
