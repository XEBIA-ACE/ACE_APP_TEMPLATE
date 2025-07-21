import os
import shutil

# Remove files based on user choices
def remove_file(filepath):
    if os.path.exists(filepath):
        if os.path.isfile(filepath):
            os.remove(filepath)
        else:
            shutil.rmtree(filepath)

# Remove Docker files if not needed
if '{{ cookiecutter.use_docker }}' == 'false':
    remove_file('Dockerfile')
    remove_file('docker-compose.yml')

# Remove Flyway migration if not needed
if '{{ cookiecutter.use_flyway }}' == 'false':
    remove_file('src/main/resources/db')

# Remove TestContainers dependency if not needed
if '{{ cookiecutter.use_testcontainers }}' == 'false':
    # This would be handled in the pom.xml template
    pass

# Create initial commit message
commit_message = f"""
🎉 Initial commit for {{ cookiecutter.project_name }}

Generated with cookiecutter-spring-boot template:
- Java {{ cookiecutter.java_version }}
- Spring Boot {{ cookiecutter.spring_boot_version }}
- Database: {{ cookiecutter.database }}
- Security: {{ cookiecutter.use_security }}
- Swagger: {{ cookiecutter.use_swagger }}
- Docker: {{ cookiecutter.use_docker }}
"""

print('✅ Project generated successfully!')
print(f'📁 Project location: {{ cookiecutter.project_slug }}')
print('🚀 Next steps:')
print('   1. cd {{ cookiecutter.project_slug }}')
print('   2. mvn clean install')
print('   3. mvn spring-boot:run')
print(f'   4. Open http://localhost:{{ cookiecutter.port }}{{ cookiecutter.context_path }}/health')
{% if cookiecutter.use_swagger == 'true' %}
print(f'   5. API docs: http://localhost:{{ cookiecutter.port }}{{ cookiecutter.context_path }}/swagger-ui.html')
{% endif %}