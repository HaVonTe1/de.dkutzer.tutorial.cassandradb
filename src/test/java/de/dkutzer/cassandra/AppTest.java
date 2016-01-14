package de.dkutzer.cassandra;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import junit.framework.TestCase;
import mockit.Mock;
import mockit.MockUp;

/**
 * Unit test for simple App.
 */
public class AppTest extends TestCase {

    @SuppressWarnings({ "static-method", "unused" })
    @org.junit.Test
    public void test() throws Exception {
        List<PersonDTO> mockedPersonsTable = new ArrayList<>();

        new MockUp<CassandraDBPersonController>() {
            @Mock
            void init() { /* do nothing */
            }

            @Mock
            void close() {/* do nothing */
            }

            @Mock
            void insertPerson(PersonDTO person) {

                mockedPersonsTable.add(person);
            }

            @Mock
            List<PersonDTO> findAllPersons() {

                return mockedPersonsTable;
            }

            @Mock
            List<PersonDTO> findPersonByName(String name) {
                List<PersonDTO> result = mockedPersonsTable.stream().filter(p -> {
                    return p.name.equals(name);
                }).collect(Collectors.toList());
                return result;
            }

        };

        List<PersonDTO> allPersons = CassandraDBPersonController.findAllPersons();
        assertThat(allPersons).isEmpty();

        CassandraDBPersonController.insertPerson(new PersonDTO("Achim", "Menzel", 1));
        allPersons = CassandraDBPersonController.findAllPersons();
        assertThat(allPersons).hasSize(1);

        CassandraDBPersonController.insertPerson(new PersonDTO("Dietmar", "Wischmeyer", 2));
        allPersons = CassandraDBPersonController.findAllPersons();
        assertThat(allPersons).hasSize(2);

        List<PersonDTO> findPersonByName = CassandraDBPersonController.findPersonByName("Achim");
        assertThat(findPersonByName).hasSize(1);
        final PersonDTO personDTO = findPersonByName.get(0);
        assertThat(personDTO.lastName).isEqualTo("Menzel");
        assertThat(personDTO.name).isEqualTo("Achim");
        assertThat(personDTO.age).isEqualTo(1);

    }
}
