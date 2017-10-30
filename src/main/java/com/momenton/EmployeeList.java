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
    List<Integer> size = jdbcTemplate.query(
        "SELECT employee_name, length(employee_name) as length FROM employees ORDER BY length DESC LIMIT 1",
        (rs, rowNum) -> rs.getInt("length"));

    Node<Employee> employees = populateTree();
    print(employees, employees.getDepth(), size.get(0));
    printStartEndLine(employees.getDepth(), size.get(0));


  }

  public void printStartEndLine(int columns, int maxSize) {
    StringBuilder builder = new StringBuilder();
    builder.append("+");
    for (int i = 0; i < columns; i++) {
      for (int j = 0; j < maxSize - 1; j++) {
        builder.append("-");
      }
    }
    builder.append("+");
    log.info(builder.toString());
  }

  public void print(Node<Employee> employees, int columns, int maxSize) {
    if (employees.getData() == null) {
      printStartEndLine(columns, maxSize);
    } else {
      int depth = employees.getDepth();
      int difference = columns - depth;
      StringBuilder line = new StringBuilder();
      //print columns before name
      for (int i = 0; i < difference - 1; i++) {
        line.append("|");
        for (int j = 0; j < maxSize; j++) {
          line.append(" ");
        }
      }
      line.append("|");

      //print name
      line.append(employees.getData().getEmployeeName());
      for (int j = 0; j < maxSize - employees.getData().getEmployeeName().length(); j++) {
        line.append(" ");
      }

      //print columns after name
      for (int i = 0; i < depth - 1; i++) {
        line.append("|");
        for (int j = 0; j < maxSize; j++) {
          line.append(" ");
        }
      }
      line.append("|");
      log.info(line.toString());
    }
    for (Node<Employee> employee : employees.getChildren()) {
      print(employee, columns, maxSize);
    }

  }


  public Node<Employee> populateTree() {
    Node<Employee> employees = new Node<>(null);
    List<Employee> headManagers = new ArrayList<>(jdbcTemplate.query(
        "SELECT employee_name, id, manager_id FROM employees WHERE manager_id is null",
        (rs, rowNum) -> new Employee(rs.getString("employee_name"), rs.getLong("id"), rs.getLong("manager_id"))
    ));

    for (Employee employee : headManagers) {
      //employees = new Node<>(employee);
      employees.addChild(getChildren(employee));
    }

    return employees;
  }

  private Node<Employee> getChildren(Employee manager) {
    List<Employee> reports = new ArrayList<>(jdbcTemplate.query(
        "SELECT employee_name, id, manager_id FROM employees WHERE manager_id = ?", new Object[]{manager.getId()},
        (rs, rowNum) -> new Employee(rs.getString("employee_name"), rs.getLong("id"), rs.getLong("manager_id"))
    ));
    Node<Employee> employeeNode = new Node<>(manager);
    for (Employee employee : reports) {
      employeeNode.addChild(getChildren(employee));
    }
    return employeeNode;
  }

  public void setUpDB() {

    log.info("Creating table");

    jdbcTemplate.execute("DROP TABLE employees IF EXISTS");
    jdbcTemplate.execute("CREATE TABLE employees(" +
        "employee_name VARCHAR(255), id int, manager_id int, " +
        "PRIMARY KEY (id)," +
        "FOREIGN KEY(manager_id) REFERENCES employees(id))");

    List<Object[]> employeeList = Stream
        .of("Jamie,150,", "Alan,100,150", "Steve,400,150", "Martin,220,100", "Alex,275,100", "David,190,400")
        .map(name -> name.split(","))
        .collect(Collectors.toList());

    // Uses JdbcTemplate's batchUpdate operation to bulk load data
    jdbcTemplate.batchUpdate("INSERT INTO employees(employee_name, id, manager_id) VALUES (?,?,?)", employeeList);

  }
}
