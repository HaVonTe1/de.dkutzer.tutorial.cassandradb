package de.dkutzer.cassandra;

import java.util.List;

/**
 * Hello world!
 *
 */
public class App {
    public static void main(String[] args) {
        CassandraDBPersonController.init();
        CassandraDBPersonController.insertPerson(new PersonDTO("John", "Lennon", 40));
        CassandraDBPersonController.insertPerson(new PersonDTO("Paul", "McCartney", 73));
        CassandraDBPersonController.insertPerson(new PersonDTO("Geroge", "Harrison", 58));
        CassandraDBPersonController.insertPerson(new PersonDTO("Ringo", "Starr", 75));

        System.out.println("find all");
        final List<PersonDTO> allPersons = CassandraDBPersonController.findAllPersons();
        allPersons.forEach(p -> {
            System.out.println(p);
        });

        System.out.println("####");
        System.out.println("finding Ringo..");
        final List<PersonDTO> foundPersons = CassandraDBPersonController.findPersonByName("Starr");
        foundPersons.forEach(p -> {
            System.out.println(p);
        });

    }
}
