package com.momenton;

        import org.slf4j.Logger;
        import org.slf4j.LoggerFactory;
        import org.springframework.beans.factory.annotation.Autowired;
        import org.springframework.boot.CommandLineRunner;
        import org.springframework.boot.SpringApplication;
        import org.springframework.boot.autoconfigure.SpringBootApplication;
        import org.springframework.context.ConfigurableApplicationContext;
        import org.springframework.jdbc.core.JdbcTemplate;

        import java.util.Arrays;
        import java.util.List;
        import java.util.stream.Collectors;
        import java.util.stream.Stream;

@SpringBootApplication
public class Application {

    private static final Logger log = LoggerFactory.getLogger(Application.class);

    public static void main(String args[]) {
        ConfigurableApplicationContext app = SpringApplication.run(Application.class, args);

        EmployeeList employeeList = (EmployeeList) app.getBean("employeeList");
        employeeList.init();

    }

}