package de.dkutzer.cassandra;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.datastax.driver.core.*;
import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.datastax.driver.core.querybuilder.Select;
import com.datastax.driver.core.querybuilder.Select.Where;

public class CassandraDBPersonController {

	private static Session session;
	private static Cluster cluster;

	public static void init() {

		cluster = new Cluster.Builder().addContactPoints(
				"dku-dev-01.nbg.webtrekk.com").build();

		session = cluster.connect("demo1");

		Metadata metadata = cluster.getMetadata();
		System.out.println(String.format("Connected to cluster '%s' on %s.",
				metadata.getClusterName(), metadata.getAllHosts()));
		createTablePersons();

	}

	public static void close() {
		session.close();
	}

	public static void createTablePersons() {
		session.execute("DROP TABLE IF EXISTS persons");
		session.execute("CREATE TABLE  persons (id text PRIMARY KEY, name text, lastname text, age int)");
	}

	public static void insertPerson(PersonDTO person) {

		final PreparedStatement prepareStatement = session
				.prepare("insert into persons(id,name,lastname,age) values (?,?,?,?)");


		BoundStatement boundStatement = new BoundStatement(prepareStatement);
		boundStatement.bind(UUID.randomUUID().toString(), person.name,
				person.lastName, person.age);

		session.execute(boundStatement);
	}

	public static List<PersonDTO> findAllPersons() {

		List<PersonDTO> result = new ArrayList<>();
		final Select select = new QueryBuilder(cluster).select().all()
				.from("demo1", "persons");

		final ResultSet results = session.execute(select);

		for (Row row : results) {

			result.add(new PersonDTO(row.getString("name"), row
					.getString("lastname"), row.getInt("age")));

		}

		return result;
	}

	public static List<PersonDTO> findPersonByName(String name) {
		List<PersonDTO> result = new ArrayList<>();
		final Where where = new QueryBuilder(cluster).select().all()
				.from("demo1", "persons")
				.where((QueryBuilder.eq("name", name)));

		final ResultSet results = session.execute(where);

		for (Row row : results) {

			result.add(new PersonDTO(row.getString("name"), row
					.getString("lastname"), row.getInt("age")));

		}

		return result;
	}

}
