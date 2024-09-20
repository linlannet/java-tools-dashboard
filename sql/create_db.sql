
DROP TABLE IF EXISTS DASH_ADMIN_ROLE;

DROP TABLE IF EXISTS DASH_ADMIN_ROLE_RES;

DROP TABLE IF EXISTS DASH_ADMIN_USER;

DROP TABLE IF EXISTS DASH_ADMIN_USER_ROLE;

DROP TABLE IF EXISTS DASH_BOARD;

DROP TABLE IF EXISTS DASH_BOARD_PARAM;

DROP TABLE IF EXISTS DASH_CONFIG_VERSION;

DROP TABLE IF EXISTS DASH_DATASET;

DROP TABLE IF EXISTS DASH_DATASOURCE;

DROP TABLE IF EXISTS DASH_FOLDER;

DROP TABLE IF EXISTS DASH_OPERATION_JOB;

DROP TABLE IF EXISTS DASH_WIDGET;


/*==============================================================*/
/* Table: DASH_ADMIN_ROLE                                       */
/*==============================================================*/
CREATE TABLE DASH_ADMIN_ROLE
(
    ROLE_ID              VARCHAR(50) NOT NULL,
    ROLETYPE_ID          VARCHAR(50),
    USER_ID              VARCHAR(50),
    NAME                 VARCHAR(100),
    CREATE_TIME          DATETIME,
    LAST_TIME            DATETIME,
    DESCRIPTION          VARCHAR(800),
    PRIMARY KEY (ROLE_ID)
);

/*==============================================================*/
/* Table: DASH_ADMIN_ROLE_RES                                   */
/*==============================================================*/
CREATE TABLE DASH_ADMIN_ROLE_RES
(
    ROLERES_ID           BIGINT(20) NOT NULL AUTO_INCREMENT,
    ROLE_ID              VARCHAR(50),
    RES_TYPE             VARCHAR(50),
    RES_ID               VARCHAR(50),
    PERMISSION           VARCHAR(50),
    PRIMARY KEY (ROLERES_ID)
);

/*==============================================================*/
/* Table: DASH_ADMIN_USER                                       */
/*==============================================================*/
CREATE TABLE DASH_ADMIN_USER
(
    USER_ID              VARCHAR(50) NOT NULL,
    CREATE_TYPE          INT(10) DEFAULT 1,
    NAME                 VARCHAR(200),
    USERNAME             VARCHAR(100),
    MOBILE               VARCHAR(50),
    EMAIL                VARCHAR(200),
    PASSWORD             VARCHAR(200),
    SALT                 VARCHAR(200),
    CREATE_TIME          DATETIME,
    STATUS               INT(10) NOT NULL DEFAULT 1,
    SPARE1               VARCHAR(255),
    SPARE2               VARCHAR(255),
    PRIMARY KEY (USER_ID)
);

/*==============================================================*/
/* Table: DASH_ADMIN_USER_ROLE                                  */
/*==============================================================*/
CREATE TABLE DASH_ADMIN_USER_ROLE
(
    USERROLE_ID          BIGINT(20) NOT NULL AUTO_INCREMENT,
    USER_ID              VARCHAR(50),
    ROLE_ID              VARCHAR(50),
    PRIMARY KEY (USERROLE_ID)
);

/*==============================================================*/
/* Table: DASH_BOARD                                            */
/*==============================================================*/
CREATE TABLE DASH_BOARD
(
    BOARD_ID             VARCHAR(50) NOT NULL,
    USER_ID              VARCHAR(50),
    APP_ID               VARCHAR(50),
    FOLDER_ID            VARCHAR(50),
    NAME                 VARCHAR(100) NOT NULL,
    SNAME                VARCHAR(100),
    CONTENT              TEXT,
    CREATE_TIME          DATETIME,
    LAST_TIME            DATETIME,
    DESCRIPTION          VARCHAR(800),
    PRIMARY KEY (BOARD_ID)
);

/*==============================================================*/
/* Table: DASH_BOARD_PARAM                                      */
/*==============================================================*/
CREATE TABLE DASH_BOARD_PARAM
(
    PARAM_ID             BIGINT(20) NOT NULL AUTO_INCREMENT,
    BOARD_ID             VARCHAR(50) NOT NULL,
    USER_ID              VARCHAR(50),
    CONTENT              TEXT,
    PRIMARY KEY (PARAM_ID)
);

/*==============================================================*/
/* Table: DASH_CONFIG_VERSION                                   */
/*==============================================================*/
CREATE TABLE DASH_CONFIG_VERSION
(
    VERSION_ID           VARCHAR(50) NOT NULL,
    USER_ID              VARCHAR(50) NOT NULL,
    APP_ID               VARCHAR(50),
    NAME                 VARCHAR(100) NOT NULL,
    CODE                 VARCHAR(50),
    STATUS               INT(10),
    CREATE_TIME          DATETIME,
    LAST_TIME            TIMESTAMP,
    DESCRIPTION          VARCHAR(800),
    PRIMARY KEY (VERSION_ID)
);

/*==============================================================*/
/* Table: DASH_DATASET                                          */
/*==============================================================*/
CREATE TABLE DASH_DATASET
(
    DATASET_ID           VARCHAR(50) NOT NULL,
    APP_ID               VARCHAR(50),
    USER_ID              VARCHAR(50),
    FOLDER_ID            VARCHAR(50),
    DATASOURCE_ID        VARCHAR(50),
    NAME                 VARCHAR(100),
    SNAME                VARCHAR(100),
    CATA_NAME            VARCHAR(100),
    CONTENT              TEXT,
    CREATE_TIME          DATETIME,
    LAST_TIME            DATETIME,
    DESCRIPTION          VARCHAR(800),
    PRIMARY KEY (DATASET_ID)
);

/*==============================================================*/
/* Table: DASH_DATASOURCE                                       */
/*==============================================================*/
CREATE TABLE DASH_DATASOURCE
(
    DATASOURCE_ID        VARCHAR(50) NOT NULL,
    APP_ID               VARCHAR(50),
    USER_ID              VARCHAR(50),
    NAME                 VARCHAR(100) NOT NULL,
    SNAME                VARCHAR(100),
    CATA_NAME            VARCHAR(100),
    TYPE                 VARCHAR(20) NOT NULL,
    CONTENT              TEXT,
    CREATE_TIME          DATETIME NOT NULL,
    LAST_TIME            DATETIME,
    DESCRIPTION          VARCHAR(800),
    PRIMARY KEY (DATASOURCE_ID)
);

/*==============================================================*/
/* Table: DASH_FOLDER                                           */
/*==============================================================*/
CREATE TABLE DASH_FOLDER
(
    FOLDER_ID            VARCHAR(50) NOT NULL,
    PARENT_ID            VARCHAR(50),
    APP_ID               VARCHAR(50),
    USER_ID              VARCHAR(50),
    NAME                 VARCHAR(100),
    SNAME                VARCHAR(100),
    IS_PRIVATE           TINYINT(1),
    CREATE_TIME          DATETIME,
    LAST_TIME            TIMESTAMP,
    DESCRIPTION          VARCHAR(800),
    PRIMARY KEY (FOLDER_ID)
);

/*==============================================================*/
/* Table: DASH_OPERATION_JOB                                    */
/*==============================================================*/
CREATE TABLE DASH_OPERATION_JOB
(
    JOB_ID               BIGINT(20) NOT NULL AUTO_INCREMENT,
    APP_ID               VARCHAR(50),
    USER_ID              VARCHAR(50),
    JOB_NAME             VARCHAR(200),
    CRON_EXP             VARCHAR(200),
    START_DATE           DATETIME,
    END_DATE             DATETIME,
    JOB_TYPE             VARCHAR(200),
    JOB_CONFIG           TEXT,
    JOB_STATUS           INT(10),
    LAST_EXEC_TIME       TIMESTAMP,
    EXEC_LOG             TEXT,
    SPARE1               VARCHAR(255),
    SPARE2               VARCHAR(255),
    PRIMARY KEY (JOB_ID)
);

/*==============================================================*/
/* Table: DASH_WIDGET                                           */
/*==============================================================*/
CREATE TABLE DASH_WIDGET
(
    WIDGET_ID            VARCHAR(50) NOT NULL,
    APP_ID               VARCHAR(50),
    FOLDER_ID            VARCHAR(50),
    USER_ID              VARCHAR(50),
    DATASOURCE_ID        VARCHAR(50),
    DATASET_ID           VARCHAR(50),
    CATA_NAME            VARCHAR(100),
    NAME                 VARCHAR(100),
    SNAME                VARCHAR(100),
    CONTENT              TEXT,
    CREATE_TIME          DATETIME,
    LAST_TIME            DATETIME,
    DESCRIPTION          VARCHAR(800),
    PRIMARY KEY (WIDGET_ID)
);