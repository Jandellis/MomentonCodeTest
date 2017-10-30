package com.momenton;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

import org.junit.Test;

/**
 * Please fill me out with a bit of info about this file
 */
public class NodeTest {

  @Test
  public void getDepth() throws Exception {
    Node<Employee> employeeNode = new Node<>(null);
    Node<Employee> jamie = new Node<>(new Employee("Jamie", 150L, 0L));
    Node<Employee> alan = new Node<>(new Employee("Alan", 100L, 150L));
    alan.addChild(new Employee("Martin", 220L, 100L));
    alan.addChild(new Employee("Alex", 275L, 100L));
    jamie.addChild(alan);
    Node<Employee> steve = new Node<>(new Employee("Steve", 400L, 150L));
    steve.addChild(new Employee("David", 190L, 400L));
    jamie.addChild(steve);
    employeeNode.addChild(jamie);

    //one extra for empty parent node
    assertThat(employeeNode.getDepth(), is(4));
  }

}