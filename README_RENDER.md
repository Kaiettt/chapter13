Render deployment notes for chapter13 webapp

Summary

This repository is a Java webapp (WAR) using JPA/Hibernate and PostgreSQL. It has a Dockerfile in the project root so it can be deployed to Render as a Docker Web Service. DB configuration is read from environment variables — the app accepts Render's `DATABASE_URL` (postgres:// or postgresql://) or the separate `DB_URL`, `DB_USER`, `DB_PASSWORD` vars.

Important: DO NOT commit secrets to the repo. Use Render's Environment / Secrets settings to store database credentials.

Environment variables (exact values you provided)

Recommended single variable (Render):
- Name: DATABASE_URL
- Value:
  postgresql://murach_k9kh_user:oPOLKZIAoIkd5k9BxZ2WfnwCqpD9s9FZ@dpg-d3ae2n2dbo4c738oi9a0-a/murach_k9kh

Alternate (three separate vars):
- DB_URL = jdbc:postgresql://dpg-d3ae2n2dbo4c738oi9a0-a:5432/murach_k9kh
- DB_USER = murach_k9kh_user
- DB_PASSWORD = oPOLKZIAoIkd5k9BxZ2WfnwCqpD9s9FZ

(If your provider requires SSL for Postgres, append `?sslmode=require` to the URL or to the JDBC URL: e.g. `jdbc:postgresql://host:5432/dbname?sslmode=require`.)

Quick Render deployment steps

1) Push this repository to a Git host (GitHub/GitLab) if not already.

2) In Render dashboard, create a new service:
   - Type: Web Service (select "Docker" or "Dockerfile")
   - Connect the repo and branch containing this project.
   - For Dockerfile path use the repository root (the Dockerfile in the project root is used).
   - Set the port if asked (default Tomcat inside the container usually uses 8080; Render provides $PORT; check your Dockerfile if it reads $PORT).

3) Add environment variables (Dashboard → Service → Environment):
   - Add `DATABASE_URL` and paste the value shown above (recommended). Or add `DB_URL`, `DB_USER`, `DB_PASSWORD` if you prefer separate vars.
   - Do not commit these to the repo.

4) Deploy. Render will build the Docker image and start the container.

Verify after deploy

- Check the Render service logs for JPA / Hibernate startup messages and any errors (Dashboard → Logs).
- The app's root endpoint (index.jsp) should load; try submitting the email form to confirm DB writes.
- If you see DB connection errors, confirm:
  - `DATABASE_URL` is set and correctly formatted, or
  - `DB_URL`/`DB_USER`/`DB_PASSWORD` are correct, and
  - SSL mode if required (`?sslmode=require`).

Useful local commands

Build the WAR locally (PowerShell):

```powershell
mvn -f "d:\LapTrinhWeb\chapter13-week6\pom.xml" -DskipTests clean package
```

This produces `target/chapter13_1-0.0.1-SNAPSHOT.war`.

If you want to test the app against Render Postgres from your local machine, export the same env var and run the app locally (examples depend on your local runtime).

Troubleshooting

- NoClassDefFoundError / ExceptionInInitializerError at startup: check logs for the cause (missing JDBC driver or invalid URL). This project includes the PostgreSQL JDBC driver in `pom.xml`.
- If Render requires SSL for the DB, add `?sslmode=require` to the JDBC URL or DATABASE_URL query string.
- If you used `DATABASE_URL` and it doesn't parse properly, try setting `DB_URL`, `DB_USER`, `DB_PASSWORD` separately.

Contact / Notes

If you want, I can:
- Add a Render-specific `render.yaml` for reproducible deploys.
- Update DBUtil to explicitly preserve URL query params (for SSL) if Render requires them.
- Walk through the Render UI with screenshots or exact clicks.

