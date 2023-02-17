# merchant-alerting
Alerting service for Merchant Experience


Purpose:
Create a common cross-platform experience that will alert users to changing data, potential opportunities, and immediate access to pertinent information.

Description:
The alerting service is an application that will allow product teams to generate alerts, which can be stored and distributed to client applications. This includes common UI components that may be shared across client applications, which provides the display of alerts and allows for user-customized prioritization and organization.

Users:
Merchants, MPs, Pricing Managers, Online Merchants, SOMAS, etc.

## Local setup and run
### Running with `docker-compose up`

1. Install `docker`.
2. Generate [artifactory token](https://token-generator.artifactory.homedepot.com/home). Username must be lowercase ldap.
3. Add the following variables to the environment:

   ARTIFACTORY_USER - lowercase ldap

   ARTIFACTORY_TOKEN - value generated on step #2
4. Run `docker-compose up`
5. Visit http://localhost:8080/swagger-ui/index.html

If you have any issues with setup and run please refer to the **Troubleshooting** section.

## Troubleshooting
### Running with `docker-compose up`

1. 401 unauthorized connection issues while pulling dependencies through the `mvn`

Make sure that `ARTIFACTORY_USER` and `ARTIFACTORY_TOKEN` arguments are passed to the container.

Update the `docker-compose.yaml` configuration then re-bulid and run.
```diff
   merch_alerts:
-    build: .
+    build:
+      context: .
+      args:
+        - ARTIFACTORY_USER
+        - ARTIFACTORY_TOKEN
     ports:
       - "8080:8080"
     depends_on:
@@ -20,4 +24,6 @@ services:
       ENVIRONMENT: local
       VERSION: local
       POSTGRES_USER: postgres
-      POSTGRES_PASSWORD: password
\ No newline at end of file
+      POSTGRES_PASSWORD: password
+      ARTIFACTORY_USER: ${ARTIFACTORY_USER}
+      ARTIFACTORY_TOKEN: ${ARTIFACTORY_TOKEN}
\ No newline at end of file
```

2. DB connection issue `org.postgresql.util.PSQLException: Connection to 0.0.0.0:5432 refused.`

Update configuration of `/src/main/resources/application-local.yml`.

```diff
 spring:
   datasource:
-    url: jdbc:postgresql://0.0.0.0:5432/mpulse
+    url: jdbc:postgresql://postgresql:5432/mpulse
```

where `postgresql` is a service name from `docker-compose.yaml` file.