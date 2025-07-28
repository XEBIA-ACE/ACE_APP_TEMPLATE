# {{ cookiecutter.project_name }}

{% if cookiecutter.project_short_description %}
{{ cookiecutter.project_short_description }}
{% else %}
This is a Node.js microservice named **{{ cookiecutter.project_slug }}**.
{% endif %}

## Repository

This project's repository can be found at:
`{{ cookiecutter.repository_name }}`

## Getting Started

1.  **Environment Variables:** Copy `.env.example` to `.env` and update the variables for your environment:
    ```bash
    cp .env.example .env
    ```
    Ensure you configure:
    * `PORT` (default: `{{ cookiecutter.default_port }}`)
    * `MONGODB_URI` (if `use_mongoose` is `y`, default: `{{ cookiecutter.mongodb_uri }}`)
    * `REDIS_URI` (if `use_redis` is `y`, default: `{{ cookiecutter.redis_uri }}`)
    * Add any other required secrets or configurations.

2.  **Install Dependencies:**
    ```bash
    npm install
    ```

3.  **Start the Service:**
    ```bash
    npm run dev
    ```
    The service should now be running on `http://localhost:{{ cookiecutter.default_port }}`.

## API Documentation

{% if cookiecutter.include_swagger_ui == "y" %}
The API documentation (Swagger UI/OpenAPI) is available at `http://localhost:{{ cookiecutter.default_port }}/api-docs`.
{% endif %}

## Health Check

-   `GET /api/health` returns `{ status: 'ok' }`

## Technologies Used

* **Node.js**: Version `{{ cookiecutter.node_version }}`
* **Express**: Version `{{ cookiecutter.express_version }}`
{% if cookiecutter.use_mongoose == "y" %}
* **MongoDB**: Connected via Mongoose
{% endif %}
{% if cookiecutter.use_redis == "y" %}
* **Redis**
{% endif %}
{% if cookiecutter.authentication_method == "jwt" %}
* **Authentication**: JWT (JSON Web Tokens)
{% elif cookiecutter.authentication_method == "session_based" %}
* **Authentication**: Session-Based
{% endif %}

## Development Tools

{% if cookiecutter.use_jest == "y" %}
* **Testing**: Jest
{% endif %}
{% if cookiecutter.use_eslint == "y" %}
* **Linting**: ESLint
{% endif %}
{% if cookiecutter.use_prettier == "y" %}
* **Formatting**: Prettier
{% endif %}
{% if cookiecutter.use_docker == "y" %}
* **Containerization**: Docker
{% if cookiecutter.include_docker_compose == "y" %}
* **Orchestration**: Docker Compose
{% endif %}
{% endif %}

## Contributing

{% if cookiecutter.include_contributing == "y" %}
Please see our [CONTRIBUTING.md](CONTRIBUTING.md) for guidelines on how to contribute.
{% else %}
Contributions are welcome!
{% endif %}

## Code of Conduct

{% if cookiecutter.include_code_of_conduct == "y" %}
Please review our [CODE_OF_CONDUCT.md](CODE_OF_CONDUCT.md).
{% else %}
Please adhere to a respectful and inclusive code of conduct.
{% endif %}

## Security

{% if cookiecutter.include_security_md == "y" %}
Refer to [SECURITY.md](SECURITY.md) for information on reporting security vulnerabilities.
{% endif %}

## License

This project is licensed under the **{{ cookiecutter.license }} License** - see the [LICENSE](LICENSE) file for details.

## Author

**{{ cookiecutter.author_name }}** <{{ cookiecutter.author_email }}>
