package no.kristiania.db.person;

import no.kristiania.TestData;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;


public class PersonDaoTest {

    private PersonDao dao = new PersonDao(TestData.testDataSource());

    @Test
    void shouldRetrieveSavedPerson() throws SQLException {
        dao.insert(examplePerson());
        dao.insert(examplePerson());

        Person person = examplePerson();
        person.setPersonId(dao.insert(person));

        assertThat(dao.retrieve(person.getPersonId()))
                .hasNoNullFieldsOrProperties()
                .usingRecursiveComparison()
                .isEqualTo(person)
        ;
    }

    @Test
    void shouldListAllPeople() throws SQLException {
        Person person1 = examplePerson();
        dao.insert(person1);

        Person anotherPerson = examplePerson();
        dao.insert(anotherPerson);

        Person person = examplePerson();
        person.setPersonId(dao.insert(person));

        assertThat(dao.listAll())
                .extracting((Person::getFirstName))
                .contains(person.getFirstName(), anotherPerson.getFirstName());
    }

    public static Person examplePerson() {
        Person person = new Person();
        person.setFirstName(TestData.pickOne("Johanne", "Jill", "Jane", "James", "Jacob", "Nora", "Emil", "Noah", "Emma", "Maja", "Oliver", "Filip", "Lukas","Liam", "Henrik", "Sofia", "Emilie"));
        person.setLastName(TestData.pickOne("Hansen", "Johansen", "Olsen", "Larsen", "Andersen", "Pedersen", "Nilsen", "Kristiansen", "Jensen", "Karlsen", "Johnsen"));
        person.setMailAddress(TestData.pickOne("romantic01@online.no", "crypt@gmail.com", "kramulous@comcast.net", "gomor@icloud.com", "dbrobins@att.net", "lampcht@online.net", "mleary@mac.com", "gward@verizon.net", "dexter@msn.com", "oiyou-47@mail.com", "essi389@mail.com", "brovade5@ymail.com", "tebei41@yopl.com", "pauda9@mail.com", "blomster@yahoo.com", "boot.32@gmail.com"));
        person.setProfessionId(TestData.pickOneInteger(1, 2, 2));
        person.setWorkplaceId(TestData.pickOneInteger(1, 2, 3));
        return person;
    }



}
