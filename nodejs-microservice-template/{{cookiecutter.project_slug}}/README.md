# {{ cookiecutter.project_name }}

## Getting Started

1. Copy `.env.example` to `.env` and update as needed.
2. Install dependencies:
   ```
   npm install
   ```
3. Start the service:
   ```
   npm run dev
   ```

## Health Check

- `GET /api/health` returns `{ status: 'ok' }` 