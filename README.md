# MoultDB API

MoultDB is a resource on moulting in arthropods (from paleontology to genomics).

This repository contains the API code to use the MoultDB data source (a MySQL database).
It is a Java/Spring Boot project.
This includes classes used to generate and insert data into MoultDB.
The war file generated from this module will be used to deploy the API on a server.

## How to run locally the API with IntelliJ
1. Configure a Spring Boot application:  
   Run > Edit Configurations > + > Spring Boot
    - name: moultdb-api
    - main class: org.moultdb.api.MoultdbApiApplication.java
    - use classpath module: moultdb-api

2. Set empty properties in `moultdb-api/src/main/resources/application.properties` such as `spring.datasource.username`.
**WARNING: These properties should not be commited as they should not be available on GitHub**

3. Run the application:  
   Run > Run 'moultdb-api'

## How to build the API war be deployed on server
1. Set empty properties in `moultdb-api/src/main/resources/application.properties` such as `spring.datasource.username`.
   **

**WARNING: These properties should not be commited as they should not be available on GitHub**

These properties can be set after the decompression of the war and before the deployment in Tomcat.

2. Build the war from moultdb-api directory: `cd moultdb-api ; mvn -DskipTests clean install`
