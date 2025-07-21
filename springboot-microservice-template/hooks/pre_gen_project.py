import re
import sys

# Validate project name
project_name = '{{ cookiecutter.project_name }}'
if not re.match(r'^[a-zA-Z0-9\s\-_]+$', project_name):
    print('ERROR: Project name should only contain letters, numbers, spaces, hyphens, and underscores!')
    sys.exit(1)

# Validate group_id
group_id = '{{ cookiecutter.group_id }}'
if not re.match(r'^[a-z0-9\.]+$', group_id):
    print('ERROR: Group ID should follow Java package naming conventions (lowercase, dots allowed)!')
    sys.exit(1)

# Validate entity name
entity_name = '{{ cookiecutter.entity_name }}'
if not re.match(r'^[A-Z][a-zA-Z0-9]*$', entity_name):
    print('ERROR: Entity name should start with uppercase letter and contain only letters and numbers!')
    sys.exit(1)

# Validate Java version
java_version = '{{ cookiecutter.java_version }}'
if java_version not in ['11', '17', '21']:
    print('ERROR: Java version must be 11, 17, or 21!')
    sys.exit(1)

# Validate port
port = '{{ cookiecutter.port }}'
try:
    port_int = int(port)
    if port_int < 1024 or port_int > 65535:
        print('ERROR: Port must be between 1024 and 65535!')
        sys.exit(1)
except ValueError:
    print('ERROR: Port must be a valid number!')
    sys.exit(1)

print('✅ All validations passed!')