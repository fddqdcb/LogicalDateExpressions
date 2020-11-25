package com.github.fddqdcb.parser.date.integration;

import com.github.fddqdcb.parser.data.Person;
import com.github.fddqdcb.parser.data.PersonProvider;
import com.github.fddqdcb.parser.util.EntityManagerProvider;
import com.github.fddqdcb.parser.date.DatePredicates;
import java.util.List;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 *
 * @author
 */
public class DateParserIntegrationTest
{

    private static final Logger LOG = LoggerFactory.getLogger(DateParserIntegrationTest.class);

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
//    @Ignore
    public void test()
    {
        t("1.1.1915", (Person[]) null);
        t("17.9.70", PersonProvider.TOM);
        t("9.70", PersonProvider.TOM);
        t("1970", PersonProvider.TOM);
        t("<15.09.1967", PersonProvider.LINDA);
        t("<15.09.1972", PersonProvider.LINDA, PersonProvider.TOM);
        t("20.1.65 or 17.09.1970", PersonProvider.LINDA, PersonProvider.TOM);
        t("20.1.65 and 17.09.1970", (Person[]) null);
        t(">20.1.65 and <17.09.1970", (Person[]) null);
        t(">20.1.65 and <=17.09.1970", PersonProvider.TOM);
        t("65 or 1970 and >2009", PersonProvider.LINDA);
        t("(65 or 1970) and >2009", (Person[]) null);
        t("1970 or 1975 and >1990 or 2000", PersonProvider.TOM, PersonProvider.TIM);
        t("1965 or >2000 and 2005 or 2010", PersonProvider.LINDA, PersonProvider.HELEN, PersonProvider.FRANK);
        t("(1965 or >2000) and (2005 or 2010)", PersonProvider.HELEN, PersonProvider.FRANK);
        t("!>1965", PersonProvider.LINDA);
        t("!>=1965", (Person[]) null);
        t("!(>1965)", PersonProvider.LINDA);
        t("1970 - 1985", PersonProvider.TOM, PersonProvider.MARY, PersonProvider.JOHN, PersonProvider.SANDRA);
        t("!1970 - 1985", PersonProvider.LINDA, PersonProvider.SAM, PersonProvider.BARBARA, PersonProvider.TIM, PersonProvider.HELEN, PersonProvider.FRANK);
    }


    private void t(String input, Person... expected)
    {
        CriteriaBuilder builder = provider.em().getCriteriaBuilder();

        CriteriaQuery<Person> criteriaQuery = builder.createQuery(Person.class);
        Root<Person> root = criteriaQuery.from(Person.class);
        Predicate predicate = DatePredicates
                .createPredicateForDateExpression(input, builder, root.get("birthdate"));
        criteriaQuery.where(predicate);
        TypedQuery<Person> query = provider.em().createQuery(criteriaQuery);
        List<Person> result = query.getResultList();

        PersonProvider.assertContainsPerson(input, result, expected);
    }

}
