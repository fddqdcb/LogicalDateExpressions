package com.github.fddqdcb.parser.number.integration;

import com.github.fddqdcb.parser.data.Person;
import com.github.fddqdcb.parser.data.PersonProvider;
import com.github.fddqdcb.parser.util.EntityManagerProvider;
import com.github.fddqdcb.parser.number.NumberPredicates;
import java.util.List;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 *
 * @author
 */
public class NumberParserIntegrationTest
{

    private static final Logger LOG = LoggerFactory.getLogger(NumberParserIntegrationTest.class);

    @Rule
    public EntityManagerProvider provider = EntityManagerProvider.withUnit("integration-test");


    @Before
    public void setUpDatabase()
    {
        provider.begin();
        provider.em().persist(PersonProvider.BARBARA);
        provider.em().persist(PersonProvider.FRANK);
        provider.em().persist(PersonProvider.HELEN);
        provider.em().persist(PersonProvider.JOHN);
        provider.em().persist(PersonProvider.LINDA);
        provider.em().persist(PersonProvider.MARY);
        provider.em().persist(PersonProvider.SAM);
        provider.em().persist(PersonProvider.SANDRA);
        provider.em().persist(PersonProvider.TIM);
        provider.em().persist(PersonProvider.TOM);
        provider.commit();
    }


    @After
    public void clearDatabase()
    {
        provider.begin();
        int count = provider.em().createQuery("delete from Person p", Person.class).executeUpdate();
        provider.commit();
        LOG.debug("{} Persons deleted", count);
    }


    @Test
    public void test()
    {
        t("233", (Person[]) null);
        t("-1.2", PersonProvider.MARY);
        t("<3", PersonProvider.LINDA, PersonProvider.TOM, PersonProvider.SANDRA, PersonProvider.JOHN, PersonProvider.MARY);
        t("-1.2 or 0", PersonProvider.MARY, PersonProvider.JOHN);
        t("-1.2 and 0", (Person[]) null);
        t(">-10 and <-5", PersonProvider.TOM);
        t(">-10 and <=-6.75", PersonProvider.TOM);
        t("<-30 or 4.5 and >160", PersonProvider.LINDA);
        t("(-33.3 or 160) and <=0", PersonProvider.LINDA);
        t("-33.3 and 160 or 160 and >=100", PersonProvider.TIM);
        t("(<-30 or >10000) and (>-50 or <1000000)", PersonProvider.LINDA, PersonProvider.FRANK);
        t("!<1000", PersonProvider.HELEN, PersonProvider.FRANK);
        t("!>=-50", (Person[]) null);
        t("!(>-30)", PersonProvider.LINDA);
        t("-7 to -6.75", PersonProvider.TOM);
        t("0-1", PersonProvider.JOHN, PersonProvider.SANDRA);
        t("0.-.5", PersonProvider.JOHN, PersonProvider.SANDRA);
        t("0.01-0.4999999", (Person[]) null);
        t("!-10 - 1000", PersonProvider.LINDA, PersonProvider.HELEN, PersonProvider.FRANK);
    }


    private void t(String input, Person... expected)
    {
        CriteriaBuilder builder = provider.em().getCriteriaBuilder();

        CriteriaQuery<Person> criteriaQuery = builder.createQuery(Person.class);
        Root<Person> root = criteriaQuery.from(Person.class);
        Predicate predicate = NumberPredicates
                .createPredicateForNumberExpression(input, builder, root.get("decimalNumber"));
        criteriaQuery.where(predicate);
        TypedQuery<Person> query = provider.em().createQuery(criteriaQuery);
        List<Person> result = query.getResultList();

        PersonProvider.assertContainsPerson(input, result, expected);
    }

}
