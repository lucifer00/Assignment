This is a Spring Boot application that helps user store Employee and their addresses on the database.

This application uses MySql as the underlying database.

Project functionalities :-

1. Has endpoints to add new Employee and associated address(Here i've kept a one-one relationship).

2. User can add bulk data using Java Executor thread pool.

3. Caching is implemented in the application to reduce network latency and number of database calls.
   There are two levels of caches implemented.
   The First Level Cache contains the latest 5 minutes old data and the second level cache contains the entire data set 
   in consideration to the fact that data set is small.
   

4. For mapping between classes (i.e from Entity to DTO and vice versa) Dozer Mapper has been used.

5. ehCache has been used for the caching mechanism.

6. Logging functionality is implemented in the application.
