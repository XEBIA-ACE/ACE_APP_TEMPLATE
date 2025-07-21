package {{ cookiecutter.package_name }};

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = {{ cookiecutter.main_class_name }}.class)
public class {{ cookiecutter.main_class_name }} {
      public static void main(String[] args) {
          SpringApplication.run({{ cookiecutter.main_class_name }}.class, args);
      }
  }