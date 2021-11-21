**NewsArticle App**
-

There are five REST APIs defined in this application based on the problem statement-
1) POST method to create the article 
   - **https://<server-url>/news/v1/article**
2) PUT method to update the article 
   - **https://<server-url>/news/v1/article/{articleId}**
3) DELETE method to delete the article 
   - **https://<server-url>/news/v1/article/{articleId}**
4) GET method to retrieve article using articleId 
   - **https://<server-url>/news/v1/article/{articleId}**
    
5) GET method to retrieve all articles using filters like author, keyword or a given period 
   - **https://<server-url>/news/v1/article?author={author}&startDate={startDate}&endDate={endDate}&keyword={keyword}** 
   - If no filters are provided, it will retrieve all the articles. 
   - If all filters are provided, it will retrieve the articles based on all the search criteria. 
   - Either one of the query parameter will work or all of them. Permutations of them is not implemented yet.
   
**Tech-stack used-**

- SpringBoot 2 app using Java 11
- Postgres 9.6 SQL Database to store articles
- Junit 5 to write unit test cases.
- Mock MVC(extension of Spring framework) to write integration test cases. This can also be implemented using Rest-Assured.
- Apache tomcat server (embedded in the springboot app)

**Main architecture features of the app-**

- Follows OOPS design principle and uses Spring dependency to handle creation of beans.
- Includes Exception handling, loggers, unit and integration test cases.
- Use JPA CrudRepository(ORM) to perform all the DB operations which is simple to implement and understand.
- Unit test cases are written in Junit5 using BDD style of given-when-then to simplify understanding and maintainability.
- All the libraries needed for the implementation are embedded in the springBoot app and included in the pom.xml using spring-boot-starter-web.

Note- A generic custom exception is currently handled(NewsArticleException). As the application grows and more exceptions are handled, we can define those exception classes and handled them in the controller by using Rest Controller Advice.

Things which are not currently handled in the application(due to time constraint)

- Authorization & Authentication of API end points. This can be done using OAuth2 implementation.
- Validation of the request body and parameters passed in the end point.  
- Pagination for GET methods. As the data grows in the db, it's better to implement pagination so that server is not loaded while retrieving records.
- Caching- helps to improve the performance of the application.
- Multi threading in asynchronous block using completableFuture.



