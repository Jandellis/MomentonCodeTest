package com.momenton;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

/**
 * Created by james_000 on 29/10/2017.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class EmployeeListTest {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Test
    public void populateTree() throws Exception {
        EmployeeList employeeList = new EmployeeList(jdbcTemplate);
        employeeList.setUpDB();

        Node<Employee> expected = new Node<>(new Employee("Jamie", 150L, null));
        Node<Employee> alan = new Node<>(new Employee("Alan", 100L, 150L));
        alan.addChild(new Employee("Martin", 220L, 100L));
        alan.addChild(new Employee("Alex", 275L, 100L));
        expected.addChild(alan);
        Node<Employee> steve = new Node<>(new Employee("Steve", 400L, 150L));
        steve.addChild(new Employee("David", 190L, 400L));
        expected.addChild(steve);

        assertThat(employeeList.populateTree(), is(expected));
    }

}