# Project made available to the community. Full functional flow, offering an MVP for studies and knowledge of the technologies involved.
&nbsp;
##### ```Project objective:``` Manage registration of users, customers, products, versions, among other records and rules. There are also permission levels for certain types and groups of users, thus creating layers of access within the system for client management and queries.
&nbsp;
&nbsp;

## GRANT CONTROL
##### version:`[1.0.0]`
![](https://raw.githubusercontent.com/guizaramellaf91/grantcontrol/master/src/main/resources/static/img/grantcontrol.png)
&nbsp;
## Main Libraries:
| Plugin | Version |
| ------ | ------ |
| springframework.boot | ``2.1.8.RELEASE`` |
| mysql | ``5.1.47`` |
| flyway-core | ``current`` |
| org.projectlombok | ``1.18.20`` |
| thymeleaf | ``current`` |
| sonarsource.scanner | ``3.7.0.1746`` |

&nbsp;

## Installation and configuration

Required ``Java8`` or ``+``.

After cloning the project in git, run the command ```mvn clean install```. This process will download all dependencies needed to run the project.

```sh
MySql installed locally is required. 
Access mysql via command line to create the database. After accessing mysql with the username and password, type the following command 
"create database gcdb;"
More information and settings can be done in the "application.properties" file, including database and mysql connection settings.
```

After configuring the database, we can start our application....

```sh
In the command terminal it is very simple, just run the command "mvn spring-boot:run". Or using your preferred IDE for development.
Default username and password created through the flyway are: "admin:admin123". sorry for the password :(
```

```sh
Access the application at: "localhost:8580/grantcontrol"
```

## License
**Free Software, Hell Yeah!**

### The application must be used freely, including improvements, changes and adaptations must happen to expand knowledge, ``good code``!
&nbsp;
&nbsp;
##### by Guilherme Zaramella