/*==============================================================*/
/* DBMS name:      MySQL 5.0                                    */
/* Created on:     2024/9/20 22:12:14                           */
/*==============================================================*/


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
   ROLE_ID              VARCHAR(50) NOT NULL  COMMENT '角色ID',
   ROLETYPE_ID          VARCHAR(50)  COMMENT '角色分类ID',
   USER_ID              VARCHAR(50)  COMMENT '管理员ID',
   NAME                 VARCHAR(100)  COMMENT '角色自定义名称',
   CREATE_TIME          DATETIME  COMMENT '创建时间',
   LAST_TIME            DATETIME  COMMENT '最后时间',
   DESCRIPTION          VARCHAR(800)  COMMENT '描述',
   PRIMARY KEY (ROLE_ID)
);

ALTER TABLE DASH_ADMIN_ROLE COMMENT '扩展角色';

/*==============================================================*/
/* Table: DASH_ADMIN_ROLE_RES                                   */
/*==============================================================*/
CREATE TABLE DASH_ADMIN_ROLE_RES
(
   ROLERES_ID           BIGINT(20) NOT NULL AUTO_INCREMENT  COMMENT '角色资源ID',
   ROLE_ID              VARCHAR(50)  COMMENT '角色ID',
   RES_TYPE             VARCHAR(50)  COMMENT '资源类型',
   RES_ID               VARCHAR(50)  COMMENT '资源ID',
   PERMISSION           VARCHAR(50)  COMMENT '权限URL',
   PRIMARY KEY (ROLERES_ID)
);

ALTER TABLE DASH_ADMIN_ROLE_RES COMMENT '角色资源关联';

/*==============================================================*/
/* Table: DASH_ADMIN_USER                                       */
/*==============================================================*/
CREATE TABLE DASH_ADMIN_USER
(
   USER_ID              VARCHAR(50) NOT NULL  COMMENT '系统管理用户ID',
   CREATE_TYPE          INT(10) DEFAULT 1  COMMENT '创建方式0导入1录入2授权',
   NAME                 VARCHAR(200)  COMMENT '显示名称，可设置为用户名、手机号、昵称、真名',
   USERNAME             VARCHAR(100)  COMMENT '用户名',
   MOBILE               VARCHAR(50)  COMMENT '手机号码',
   EMAIL                VARCHAR(200)  COMMENT '电子邮箱',
   PASSWORD             VARCHAR(200)  COMMENT '密码',
   SALT                 VARCHAR(200)  COMMENT '混淆字符',
   CREATE_TIME          DATETIME  COMMENT '创建时间',
   STATUS               INT(10) NOT NULL DEFAULT 1  COMMENT '状态0未生效1正常2受限3锁定',
   SPARE1               VARCHAR(255)  COMMENT '备用1',
   SPARE2               VARCHAR(255)  COMMENT '备用2',
   PRIMARY KEY (USER_ID)
);

ALTER TABLE DASH_ADMIN_USER COMMENT '驾驶舱管理用户';

/*==============================================================*/
/* Table: DASH_ADMIN_USER_ROLE                                  */
/*==============================================================*/
CREATE TABLE DASH_ADMIN_USER_ROLE
(
   USERROLE_ID          BIGINT(20) NOT NULL AUTO_INCREMENT  COMMENT '用户角色ID',
   USER_ID              VARCHAR(50)  COMMENT '用户ID',
   ROLE_ID              VARCHAR(50)  COMMENT '角色ID',
   PRIMARY KEY (USERROLE_ID)
);

ALTER TABLE DASH_ADMIN_USER_ROLE COMMENT '用户角色关联表';

/*==============================================================*/
/* Table: DASH_BOARD                                            */
/*==============================================================*/
CREATE TABLE DASH_BOARD
(
   BOARD_ID             VARCHAR(50) NOT NULL  COMMENT '仪表盘ID',
   USER_ID              VARCHAR(50)  COMMENT '管理员ID',
   APP_ID               VARCHAR(50)  COMMENT '应用ID',
   FOLDER_ID            VARCHAR(50)  COMMENT '目录ID',
   NAME                 VARCHAR(100) NOT NULL  COMMENT '名称',
   SNAME                VARCHAR(100)  COMMENT '名称，简称，英文',
   CONTENT              TEXT  COMMENT '布局JSON',
   CREATE_TIME          DATETIME  COMMENT '创建时间',
   LAST_TIME            DATETIME  COMMENT '最后时间',
   DESCRIPTION          VARCHAR(800)  COMMENT '描述',
   PRIMARY KEY (BOARD_ID)
);

ALTER TABLE DASH_BOARD COMMENT '仪表盘';

/*==============================================================*/
/* Table: DASH_BOARD_PARAM                                      */
/*==============================================================*/
CREATE TABLE DASH_BOARD_PARAM
(
   PARAM_ID             BIGINT(20) NOT NULL AUTO_INCREMENT  COMMENT '仪表盘子项ID',
   BOARD_ID             VARCHAR(50) NOT NULL  COMMENT '仪表盘ID',
   USER_ID              VARCHAR(50)  COMMENT '管理员ID',
   CONTENT              TEXT  COMMENT '配置内容',
   PRIMARY KEY (PARAM_ID)
);

ALTER TABLE DASH_BOARD_PARAM COMMENT '仪表盘子项';

/*==============================================================*/
/* Table: DASH_CONFIG_VERSION                                   */
/*==============================================================*/
CREATE TABLE DASH_CONFIG_VERSION
(
   VERSION_ID           VARCHAR(50) NOT NULL  COMMENT '版本ID',
   USER_ID              VARCHAR(50) NOT NULL  COMMENT '创建人ID',
   APP_ID               VARCHAR(50)  COMMENT '应用ID',
   NAME                 VARCHAR(100) NOT NULL  COMMENT '名称',
   CODE                 VARCHAR(50)  COMMENT '代码',
   STATUS               INT(10)  COMMENT '状态0未生效1正常2受限3锁定',
   CREATE_TIME          DATETIME  COMMENT '创建时间',
   LAST_TIME            TIMESTAMP  COMMENT '最后时间',
   DESCRIPTION          VARCHAR(800)  COMMENT '描述',
   PRIMARY KEY (VERSION_ID)
);

ALTER TABLE DASH_CONFIG_VERSION COMMENT '配置版本';

/*==============================================================*/
/* Table: DASH_DATASET                                          */
/*==============================================================*/
CREATE TABLE DASH_DATASET
(
   DATASET_ID           VARCHAR(50) NOT NULL  COMMENT '数据集ID',
   APP_ID               VARCHAR(50)  COMMENT '应用ID',
   USER_ID              VARCHAR(50)  COMMENT '用户ID',
   FOLDER_ID            VARCHAR(50)  COMMENT '目录ID',
   DATASOURCE_ID        VARCHAR(50)  COMMENT '数据源ID',
   NAME                 VARCHAR(100)  COMMENT '数据集名称',
   SNAME                VARCHAR(100)  COMMENT '名称，简称，英文',
   CATA_NAME            VARCHAR(100)  COMMENT '数据源分组名称，可方便进行分组',
   CONTENT              TEXT  COMMENT '数据JSON',
   CREATE_TIME          DATETIME  COMMENT '创建时间',
   LAST_TIME            DATETIME  COMMENT '最后时间',
   DESCRIPTION          VARCHAR(800)  COMMENT '描述',
   PRIMARY KEY (DATASET_ID)
);

ALTER TABLE DASH_DATASET COMMENT '数据集';

/*==============================================================*/
/* Table: DASH_DATASOURCE                                       */
/*==============================================================*/
CREATE TABLE DASH_DATASOURCE
(
   DATASOURCE_ID        VARCHAR(50) NOT NULL  COMMENT '数据源ID',
   APP_ID               VARCHAR(50)  COMMENT '所属应用ID',
   USER_ID              VARCHAR(50)  COMMENT '用户ID',
   NAME                 VARCHAR(100) NOT NULL  COMMENT '数据源名称',
   SNAME                VARCHAR(100)  COMMENT '名称，简称，英文',
   CATA_NAME            VARCHAR(100)  COMMENT '数据源分组名称，可方便进行分组',
   TYPE                 VARCHAR(20) NOT NULL  COMMENT '数据源类型，JDBC，TEXTFILE，SOLR，ELASTIC，SAIKU',
   CONTENT              TEXT  COMMENT '配置JSON，针对不同 类型，保存不同JSON格式',
   CREATE_TIME          DATETIME NOT NULL  COMMENT '创建时间',
   LAST_TIME            DATETIME  COMMENT '最后时间',
   DESCRIPTION          VARCHAR(800)  COMMENT '描述',
   PRIMARY KEY (DATASOURCE_ID)
);

ALTER TABLE DASH_DATASOURCE COMMENT '数据源信息';

/*==============================================================*/
/* Table: DASH_FOLDER                                           */
/*==============================================================*/
CREATE TABLE DASH_FOLDER
(
   FOLDER_ID            VARCHAR(50) NOT NULL  COMMENT '目录ID',
   PARENT_ID            VARCHAR(50)  COMMENT '父目录ID',
   APP_ID               VARCHAR(50)  COMMENT '应用ID',
   USER_ID              VARCHAR(50)  COMMENT '用户ID',
   NAME                 VARCHAR(100)  COMMENT '名称',
   SNAME                VARCHAR(100)  COMMENT '名称，简称，英文',
   IS_PRIVATE           TINYINT(1)  COMMENT '是否私有1是0否',
   CREATE_TIME          DATETIME  COMMENT '创建时间',
   LAST_TIME            TIMESTAMP  COMMENT '最后时间',
   DESCRIPTION          VARCHAR(800)  COMMENT '描述',
   PRIMARY KEY (FOLDER_ID)
);

ALTER TABLE DASH_FOLDER COMMENT '目录结构';

/*==============================================================*/
/* Table: DASH_OPERATION_JOB                                    */
/*==============================================================*/
CREATE TABLE DASH_OPERATION_JOB
(
   JOB_ID               BIGINT(20) NOT NULL AUTO_INCREMENT  COMMENT '计划编号',
   APP_ID               VARCHAR(50)  COMMENT '应用ID',
   USER_ID              VARCHAR(50)  COMMENT '管理员ID',
   JOB_NAME             VARCHAR(200)  COMMENT '任务名称',
   CRON_EXP             VARCHAR(200)  COMMENT 'cron任务表达式',
   START_DATE           DATETIME  COMMENT '开始时间',
   END_DATE             DATETIME  COMMENT '结束时间',
   JOB_TYPE             VARCHAR(200)  COMMENT '任务类型',
   JOB_CONFIG           TEXT  COMMENT '任务详情',
   JOB_STATUS           INT(10)  COMMENT '任务状态:0正常1暂停',
   LAST_EXEC_TIME       TIMESTAMP  COMMENT '最后执行时间',
   EXEC_LOG             TEXT  COMMENT '执行日志',
   SPARE1               VARCHAR(255)  COMMENT '备用1',
   SPARE2               VARCHAR(255)  COMMENT '备用2',
   PRIMARY KEY (JOB_ID)
);

ALTER TABLE DASH_OPERATION_JOB COMMENT '要素任务';

/*==============================================================*/
/* Table: DASH_WIDGET                                           */
/*==============================================================*/
CREATE TABLE DASH_WIDGET
(
   WIDGET_ID            VARCHAR(50) NOT NULL  COMMENT '图表ID',
   APP_ID               VARCHAR(50)  COMMENT '应用ID',
   FOLDER_ID            VARCHAR(50)  COMMENT '目录ID',
   USER_ID              VARCHAR(50)  COMMENT '用户ID',
   DATASOURCE_ID        VARCHAR(50)  COMMENT '数据源ID',
   DATASET_ID           VARCHAR(50)  COMMENT '数据集ID',
   CATA_NAME            VARCHAR(100)  COMMENT '图表分类名称',
   NAME                 VARCHAR(100)  COMMENT '图表名称',
   SNAME                VARCHAR(100)  COMMENT '名称，简称，英文',
   CONTENT              TEXT  COMMENT '数据JSON',
   CREATE_TIME          DATETIME  COMMENT '创建时间',
   LAST_TIME            DATETIME  COMMENT '最后时间',
   DESCRIPTION          VARCHAR(800)  COMMENT '描述',
   PRIMARY KEY (WIDGET_ID)
);

ALTER TABLE DASH_WIDGET COMMENT '数据图表';

