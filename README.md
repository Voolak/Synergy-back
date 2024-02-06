# Projet Synergy.

### HOW TO LAUNCH THE PROJECT :

--- 
- Clone the project.
- Create a folder ```resources``` in ```main```.
- Add file ```application.properties``` with the following configuration :

```java
spring.datasource.url=jdbc:postgresql://localhost:5432/**your database name**
spring.datasource.username=**username**
spring.datasource.password=**password**

spring.jpa.hibernate.ddl-auto=validate
spring.jpa.show-sql=true
```
