# LogicalExpressions

## Overview

Project to parse user input of dates (in the european format DD.MM.YYYY like "24.03.2003") and numbers to convert it 
into JPA [Predicate](https://docs.oracle.com/javaee/7/api/javax/persistence/criteria/Predicate.html "Javadoc: javax.persistence.criteria.Predicate")(s) 
using [ANTLR](https://www.antlr.org/ "ANTLR project homepage") parser generator.

The project is intended to be used in a JavaEE environment using JSF [Primefaces](https://www.primefaces.org "Homepage of Primefaces library")' [lazy datatable](https://www.primefaces.org/showcase/ui/data/datatable/lazy.xhtml "Showcase: DataTable - Lazy").

## Usage

Key elements are the two classes `com.github.fddqdcb.parser.date.DatePredicates` for the use with dates and `com.github.fddqdcb.parser.number.NumberPredicates` for the use with numbers.

Each class has two static methods `createPredicateFor[Date|Number]Expression(String, CriteriaBuilder, Expression)` and `createFailsafePredicateFor[Date|Number]Expression(String , CriteriaBuilder, Expression)`. Each method returns a `Predicate` build on the inputstring and applied to the given Expression.

In the following code snippet is an example for the usage of the `DatePredicates`. The `em` variable is an instance of the `javax.persistence.EntityManager` which might be injected. The class `Person` is any entity which has an attribute called `birthdate` which is some kind of date datatype (e. g. `java.util.Date` or `java.time.LocalDate`). The result should be any Person instance whose birthdates are between the 1st of april and the 15th of august in 2010 (*>=04.2010 and <=15.08.2010*).

    CriteriaBuilder builder = em.getCriteriaBuilder();
    CriteriaQuery<Person> criteriaQuery = builder.createQuery(Person.class);
    Root<Person> root = criteriaQuery.from(Person.class);
    Predicate predicate = DatePredicates.createPredicateForDateExpression(
        ">=04.2010 and <=15.08.2010", builder, root.get("birthdate")
    );
    criteriaQuery.where(predicate);
    TypedQuery<Person> query = provider.em().createQuery(criteriaQuery);
    List<Person> result = query.getResultList();

## Inputs

### DatePredicates

Valid inputs are (full) dates in the european format DD.MM.YYYY (e. g. `14.07.2001` or `7.8.2001` or `14.7.01`), month/year combinations (e. g. `05.2002` for may of 2002 or `4.03` for april of 2003) and whole years (e. g. `2001` or `1957` or `16`). These dates can be expresed as simple dates (e. g. `15.12.2001`), as compare expressions of dates (e. g. `<15.03.2001` or `>=05.2005` or `<2015`), as logical expresions of dates (*NOTE: pay attention to the ***or***  which separates different examples and the ***or*** which is used as an alternative expression!* e. g. `15.03.2001 or 05.2001` or `(15.3.2001 or 17.4.2001) and <=03.2001`), as ranges of dates (e. g. `15.07.2003-23.07.2003` or `07.2001-11.2001` or `2003-20.05.2005` or `2004 to 2007`) or as negated expressions of all of these (e. g. `!03.2001` or `!>2003` or `!10.3.01-20.3.01`). Day and month can either be expressed as one- or two-digits, year can be expressed as one, two or four digits.

|input|result|
|-|-|
|07.05.2010|the exact day 7th of may 2010|
|04.2010|any day in april of 2010|
|2010|any day in the year 2010|
|>07.05.2010|any day greater than the 7th of may 2010|
|>=05.2010|any day greater than or equal to the 01.05.2010|
|>05.2010|any day greater than or eaual to the 01.06.2010|
|1.4.10 or 15.4.10|either the 1st or the 15th of april 2010|
|>=05.10 and <=08.2010|any day between the 1st of may and the 31th of august in 2010|
|5.10-8.10|any day between the 1st of may and the 31th of august in 2010|
|12.04.2010-15.04.2010|any day between the 12th and the 15th of april 2010|
|!05.2010|any day that is not in may 2010|
|!15.4.2010-05.2010|any day that is not between the 15th of april and the 31th of may in 2010|

For an complete syntax overview have a look at the ANTLR grammar (LogicalDateExpressions.g4) which has a pretty straitforward parser/lexer definition.

### NumberPredicates

Valid inputs are numbers (e. g. `5` or `201.5` or `-15.75`), compare expressions of numbers (e. g. `>4` or `<=5.5` or `<=-15.5`), logical expressions of numbers (*NOTE: pay attention to the ***or***  which separates different examples and the ***or*** which is used as an alternative expression!* e. g. `4 or 5` or `>=0 and <=10` or `(5 or 7) and >6`), ranges of numbers (e. g. `1-5` or `1.2-2.1` or `-10--7`) or negated expressions of all of these (e. g. `!7` or `!<18`, `!15-19`).

|input|result|
|-|-|
|201.5|the exact number 201.5|
|-13|the exact number -13|
|>17|any number that is greater than 17|
|<=6|any number that is less than or equal to  6|
|4 or 5|either the number 4 or the number 5|
|>=0 and <=15|any number between 0 and 15|
|0-15|any number between 0 and 15|
|!5|any number that is unequal to 5|
|!>10|any number that is less than or euqal to 10|
|!1-2 and !11-12 and !>=20|any number that is less than 20 and not equal to either of the four 1, 2, 11, 12|
|-10--8|either of the three -10, -9, -8|

For an complete syntax overview have a look at the ANTLR grammar (LogicalNumberExpressions.g4) which has a pretty straitforward parser/lexer definition.
