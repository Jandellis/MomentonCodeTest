package com.momenton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SpringBootApplication
public class Application implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(Application.class);

    public static void main(String args[]) {
        SpringApplication.run(Application.class, args);
    }

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public void run(String... strings) throws Exception {

        setUpDB();
    }

    private void setUpDB() {

        log.info("Creating table");

        jdbcTemplate.execute("DROP TABLE employees IF EXISTS");
        jdbcTemplate.execute("CREATE TABLE employees(" +
                "employee_name VARCHAR(255), id int, manager_id int, " +
                "PRIMARY KEY (id)," +
                "FOREIGN KEY(manager_id) REFERENCES employees(id))");

        List<Object[]> employeeList = Stream.of("Jamie,150,", "Alan,100,150", "Steve,400,150", "Martin,220,100", "Alex,275,100", "David,190,400")
                .map(name -> name.split(","))
                .collect(Collectors.toList());
        employeeList.forEach(info -> log.info(String.format("Inserting customer record for %s with id of %s and manager id of %s", info[0], info[1], info.length>2?info[2]:"")));

        // Uses JdbcTemplate's batchUpdate operation to bulk load data
        jdbcTemplate.batchUpdate("INSERT INTO employees(employee_name, id, manager_id) VALUES (?,?,?)", employeeList);

        jdbcTemplate.query(
                "SELECT employee_name, id, manager_id FROM employees WHERE employee_name = ?", new Object[] { "Alan" },
                (rs, rowNum) -> new Employee(rs.getString("employee_name"), rs.getLong("id"), rs.getLong("manager_id"))
        ).forEach(employee -> log.info(employee.toString()));
}
}