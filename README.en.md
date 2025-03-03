# java-tools-dashboard
JAVA code data cockpit application can create data sets, analyze data charts and produce data kanban through JDBC, SOLR, ELASTIC and other data sources.

## Description
```
-Intelligent data cockpit system, which can access multiple data sources to realize SQL analysis, and use clustering method to realize the display and reading of target data; Through more than ten kinds of Echarts charts, the best cockpit experience of data display can be realized.
-Intelligent operation data cockpit system, accessible data sources include: relational databases such as Oracle, Mysql, Dameng database, Db2, etc., text databases such as XLS, XLSX, JSON texts, Solr full-text retrieval databases supporting Apache, etc.
-Intelligent data cockpit system, with functions including: resource catalog maintenance, data source configuration and management, data set information management, chart function analysis, dashboard function design, dynamic cockpit interface with WEB page mode and sharing and distribution requirements in combination with comprehensive display requirements.
-Intelligent data cockpit system, based on Spring MVC framework, supported by AdminLte and Bootstrap components in the background, and rendered by PhantomJS for the distribution of charts, which can dynamically configure the presentation forms and requirements of data dashboards.
1. Main functions: access various data sources mainly by combining all kinds of structured data running in the city, and display and read the target data by using the clustering method through the SQL editor and analyzer; Through more than ten kinds of Echarts charts, the best cockpit experience of data display can be realized. Functions include: resource catalog maintenance, data source configuration and management, data set information management, chart function analysis, dashboard function design, dynamic cockpit interface of WEB page mode and sharing distribution requirements in combination with comprehensive display requirements.
2. Technical features: The software is programmed in Java language, which has high running efficiency, stability, safety and reliability, supports cross-platform deployment and has good portability. The interface presentation is based on Spring MVC framework, supported by AdminLte and Bootstrap components in the background, and the chart distribution is rendered by PhantomJS, which can dynamically configure the presentation form and requirements of data dashboard. Accessible data sources include: relational databases such as Oracle, Mysql, Dameng database, Db2, etc., text databases such as XLS, XLSX, JSON texts, full-text retrieval databases supporting Solr or elasti, etc. Mature and open three-tier architecture can reduce the coupling between the business logic of each module of the application system and is easy to expand. Provide subsequent expansion function in a configurable way, without hard coding, providing strong adaptability and expansion ability.
```

## Version

```
2.3.0   2023-12-27  The name of the independent version table was changed to DASH_ prefix.
        Solr resource is moved to dashboard, and solr version is upgraded to 8.2.0.
        Adjust the referenced basic package information and version, and improve and standardize the README.md file.

2.2.1   2023-05-17  Adjust xml error message
2.2.0   2022-05-25  Modify bugs related to personnel and role permissions.

2.1.0   2020-10-30    adjusted based on the previous version.
2.0.0 2020-05-30 Optimize the previous version and strengthen the related development mode of angularjs.

1.5     2019-05-11  the project name was adjusted to java-dashboard, and the controller name in js was modified accordingly.
        2019-05-10 Adjust functions and files, and adjust class names.
        Adjust the entry method in the controller.
        Modify the interception policy of some configuration files

1.4     2019-05-08  the product positioning was adjusted and changed to tools mode, and the file resource directory was unified and the pom file was adjusted.
        2019-04-08 Delete the related codes of view_dashboard_category.

1.3     2018-12-09 In order to maintain the integrity of products, view_dashboard_dataset and view _ dashboard _ datasource were added.

1.2     2018-07-11  the dataset table and DashDataset class were added, and related methods and resources were added.
        Merge user_id and sysuser_id in view_admin_user, and modify the corresponding method.
        On July 6, 2018, the implementation-related classes of provider were added, and currently six types of data sources are provided.
        Add H2' s cleaning task executors and services, and clean up regularly.
        On June 26, 2018, the standardization of pom documents was adjusted, including version, citation and exclusion.
        On June 25, 2018, the pages, databases, methods, etc. after adding folder and version functions were updated and adjusted.

1.1     2018-06-16      the datasource table and DSE_datasource were integrated, the category_id was changed to folder_id, and the dashboard_folder table and class were added.
        On May 5, 2018, the database was adjusted to Oracle, and the Category function was modified to increase the category_id.
        On March 3, 2018, the framework was refined on the basis of open source projects.

1.0     2018-2-5    Continue to use the existing resources of open source projects.

```

## DB-Change
![Table Scheme](./.img/db_view.png "Table Scheme")
[Table Scripts](sql/create_db.sql)
[Initial Scripts](sql/initial.sql)
```

2023-12-27      Standardized Adjustment of Table Names and Class Objects

2019-05-08      unified adjustment of the board part.
        Adjust folder_id, widget_id, board_id and config_id to Varchar type.

        Delete the view_dashboard_category table on April 8, 2019.
        On February 8, 2019, the type of folderIds was adjusted to Long.
2018-12-08      In order to maintain product integrity, view_dashboard_dataset and View _ dashboard _ datasource were added.
        On July 11th, 2018, the view_dashboard_dataset was changed to dash_dataset, and the dataset_id was changed to varchar.
        Merge user_id and sysuser_id in view_admin_user.
        On June 20, 2018, the view_dashboard_category table was temporarily reserved.

2018-06-16      the view_dashboard_datasource table and DSE_DATASOURCE were integrated.
        Add tables view_dashboard_folder and view_config_version.
        Adjust the category_id in view_dashboard_board to FOLDER_ID.
2018-05-30      all the table structures in the SS project were generated and the data were initialized.

2017-12-15      the table structure of view_ was completed and the table was generated.
        View_table 13
```

## Technical Guidelines
### Software Architecture
```
1. Springboot MVC 
2. AngelarJS 
```

### Instructions
1. download code
2. create table scripts in mysql8.0 [table scripts](sql/create_db.sql)
3. run initial scripts in mysql8.0 [initial scripts](sql/initial.sql)
4. Myeclipse or IDEA InterliJ，add tomcat server，config and run，login and operation

## 应用效果
![login](./.img/login.png "login")</br>admin，admin
![datasource](./.img/config-datasource.png "datasource")
![dataset](./.img/config-dataset.png "dataset")
![widget](./.img/config-widget.png "widget")

### Instructions
1. use by maven
```
   <dependency>
      <groupId>${groupId}</groupId>
      <artifactId>${artifactId}</artifactId>
      <version>${project.version}</version>
      <scope>test</scope>
   </dependency>
```
2. use direct jar

## Contribution
1.  Fork repository
2.  new Feat_1.0.0 branch
3.  commit
```
git config user.name linlaninfo
git config user.email linlannet@163.com
git config --global --list
git config --list
```
4. new Pull Request
5. create and commit tag
```
create
git tag -a 2.6.0 -m "统一工具包路径，合并之前分散的5个子包"
list
git tag
commit
git push origin --tags
delete
git tag -d 2.4.1

install: mvn clean install
deploy: mvn clean deploy

```
6. other

## 联系我们
```
网站：https://www.linlan.net
邮箱：linlannet@163.com
```
