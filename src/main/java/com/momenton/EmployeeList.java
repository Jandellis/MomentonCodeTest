package com.momenton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by james_000 on 29/10/2017.
 */
@Component
public class EmployeeList {

    private static final Logger log = LoggerFactory.getLogger(EmployeeList.class);

    JdbcTemplate jdbcTemplate;

    @Autowired
    public EmployeeList(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void init() {
        setUpDB();
        //dump table into tree structure
        //multiple trees if more than one person with out manager
        //once done work out max depth
        //depth = columns
        //then start drawing
    }


    public Node<Employee> populateTree() {
        Node<Employee> employees;
        List<Employee> headManagers = new ArrayList<>(jdbcTemplate.query(
                "SELECT employee_name, id, manager_id FROM employees WHERE manager_id is null",
                (rs, rowNum) -> new Employee(rs.getString("employee_name"), rs.getLong("id"), rs.getLong("manager_id"))
        ));

        for (Employee employee: headManagers) {
            employees = new Node<>(employee);
            employees.addChild(getChildren(employee.getId()));
        }


        return null;
    }

    private Node<Employee> getChildren(Long id) {

    }

    public void setUpDB() {

        log.info("Creating table");

        jdbcTemplate.execute("DROP TABLE employees IF EXISTS");
        jdbcTemplate.execute("CREATE TABLE employees(" +
                "employee_name VARCHAR(255), id int, manager_id int, " +
                "PRIMARY KEY (id)," +
                "FOREIGN KEY(manager_id) REFERENCES employees(id))");

        List<Object[]> employeeList = Stream.of("Jamie,150,", "Alan,100,150", "Steve,400,150", "Martin,220,100", "Alex,275,100", "David,190,400")
                .map(name -> name.split(","))
                .collect(Collectors.toList());
        employeeList.forEach(info -> log.info(String.format("Inserting customer record for %s with id of %s and manager id of %s", info[0], info[1], info.length > 2 ? info[2] : "")));

        // Uses JdbcTemplate's batchUpdate operation to bulk load data
        jdbcTemplate.batchUpdate("INSERT INTO employees(employee_name, id, manager_id) VALUES (?,?,?)", employeeList);

        jdbcTemplate.query(
                "SELECT employee_name, id, manager_id FROM employees WHERE employee_name = ?", new Object[]{"Alan"},
                (rs, rowNum) -> new Employee(rs.getString("employee_name"), rs.getLong("id"), rs.getLong("manager_id"))
        ).forEach(employee -> log.info(employee.toString()));
    }
}
