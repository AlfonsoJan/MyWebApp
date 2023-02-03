## Illumination

### Next generation sequencing data processing web application

J.A. Busker, S.J. Nelen, W.H. Zeetvat, and R. Hartsuiker,  
Hanze University of Applied Sciences, Groningen.

#### Goal and description
The goal of this application is to help researchers, at the Hanze university, 
work with a new Illumina sequencer. To achieve that goal we used a web application,
with a database as backend, to keep track of al the files and analysis a user might have.

With the making of the web application the following programming languages, 
frameworks, libraries and packages have been used. The controller which communicates
with the different components consists of various java servlets. The frontend is made with HTML
and CSS. With Thymeleaf as the template engine for HTML, and the Tailwind framework for the CSS. 
The icons are from the tabler-icons, and the functionality of these icons and buttons is handled by 
javascript in the HTML code. For the database MySQL is used, and there is a set of java classes that handles
the communication between the data layer and the model called a Database Acces Object(DAO). 
Lastly the java based Apache Tomcat software is used as webserver.

#### Development and Testing
For the development of this application IntelliJ IDE (version 2022.3.2) is used with Apache tomcat server for testing purposes.
Furthermore Gradle is used as dependency management. The Apache Tomcat server can be found 
[here](https://tomcat.apache.org/download-90.cgi) as download, version 9.0.70 or 9.0.71 is used with this project. 
To set this up in IntelliJ you can find this in settings -> Build, Execution, Deployment -> Application servers ->
click + and point to the location where the zip is extracted. Now you can start the server and it deploys the the Tomcat
server and starts up a browser tab with the web application. If it didn't open a browser
you can access it using localhost:8080/new-files in your browser. All the java, HTML and CSS files can be found in the
[src folder](https://github.com/AlfonsoJan/MyWebApp/tree/master/src/main), and the setup for the database can be seen in 
the [MyWebApp.sql file](https://github.com/AlfonsoJan/MyWebApp/blob/master/MyWebApp.sql).
