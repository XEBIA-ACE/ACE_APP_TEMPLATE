package {{ cookiecutter.package_name }};

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
{% if cookiecutter.use_flyway == 'true' -%}
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
{% endif %}

@SpringBootApplication
{% if cookiecutter.use_flyway == 'true' -%}
@EnableJpaAuditing
{% endif -%}
public class {{ cookiecutter.main_class_name }} {
    public static void main(String[] args) {
        SpringApplication.run({{ cookiecutter.main_class_name }}.class, args);
    }
}