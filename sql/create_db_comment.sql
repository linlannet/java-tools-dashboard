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
   ROLE_ID              VARCHAR(50) NOT NULL  COMMENT '��ɫID',
   ROLETYPE_ID          VARCHAR(50)  COMMENT '��ɫ����ID',
   USER_ID              VARCHAR(50)  COMMENT '����ԱID',
   NAME                 VARCHAR(100)  COMMENT '��ɫ�Զ�������',
   CREATE_TIME          DATETIME  COMMENT '����ʱ��',
   LAST_TIME            DATETIME  COMMENT '���ʱ��',
   DESCRIPTION          VARCHAR(800)  COMMENT '����',
   PRIMARY KEY (ROLE_ID)
);

ALTER TABLE DASH_ADMIN_ROLE COMMENT '��չ��ɫ';

/*==============================================================*/
/* Table: DASH_ADMIN_ROLE_RES                                   */
/*==============================================================*/
CREATE TABLE DASH_ADMIN_ROLE_RES
(
   ROLERES_ID           BIGINT(20) NOT NULL AUTO_INCREMENT  COMMENT '��ɫ��ԴID',
   ROLE_ID              VARCHAR(50)  COMMENT '��ɫID',
   RES_TYPE             VARCHAR(50)  COMMENT '��Դ����',
   RES_ID               VARCHAR(50)  COMMENT '��ԴID',
   PERMISSION           VARCHAR(50)  COMMENT 'Ȩ��URL',
   PRIMARY KEY (ROLERES_ID)
);

ALTER TABLE DASH_ADMIN_ROLE_RES COMMENT '��ɫ��Դ����';

/*==============================================================*/
/* Table: DASH_ADMIN_USER                                       */
/*==============================================================*/
CREATE TABLE DASH_ADMIN_USER
(
   USER_ID              VARCHAR(50) NOT NULL  COMMENT 'ϵͳ�����û�ID',
   CREATE_TYPE          INT(10) DEFAULT 1  COMMENT '������ʽ0����1¼��2��Ȩ',
   NAME                 VARCHAR(200)  COMMENT '��ʾ���ƣ�������Ϊ�û������ֻ��š��ǳơ�����',
   USERNAME             VARCHAR(100)  COMMENT '�û���',
   MOBILE               VARCHAR(50)  COMMENT '�ֻ�����',
   EMAIL                VARCHAR(200)  COMMENT '��������',
   PASSWORD             VARCHAR(200)  COMMENT '����',
   SALT                 VARCHAR(200)  COMMENT '�����ַ�',
   CREATE_TIME          DATETIME  COMMENT '����ʱ��',
   STATUS               INT(10) NOT NULL DEFAULT 1  COMMENT '״̬0δ��Ч1����2����3����',
   SPARE1               VARCHAR(255)  COMMENT '����1',
   SPARE2               VARCHAR(255)  COMMENT '����2',
   PRIMARY KEY (USER_ID)
);

ALTER TABLE DASH_ADMIN_USER COMMENT '��ʻ�չ����û�';

/*==============================================================*/
/* Table: DASH_ADMIN_USER_ROLE                                  */
/*==============================================================*/
CREATE TABLE DASH_ADMIN_USER_ROLE
(
   USERROLE_ID          BIGINT(20) NOT NULL AUTO_INCREMENT  COMMENT '�û���ɫID',
   USER_ID              VARCHAR(50)  COMMENT '�û�ID',
   ROLE_ID              VARCHAR(50)  COMMENT '��ɫID',
   PRIMARY KEY (USERROLE_ID)
);

ALTER TABLE DASH_ADMIN_USER_ROLE COMMENT '�û���ɫ������';

/*==============================================================*/
/* Table: DASH_BOARD                                            */
/*==============================================================*/
CREATE TABLE DASH_BOARD
(
   BOARD_ID             VARCHAR(50) NOT NULL  COMMENT '�Ǳ���ID',
   USER_ID              VARCHAR(50)  COMMENT '����ԱID',
   APP_ID               VARCHAR(50)  COMMENT 'Ӧ��ID',
   FOLDER_ID            VARCHAR(50)  COMMENT 'Ŀ¼ID',
   NAME                 VARCHAR(100) NOT NULL  COMMENT '����',
   SNAME                VARCHAR(100)  COMMENT '���ƣ���ƣ�Ӣ��',
   CONTENT              TEXT  COMMENT '����JSON',
   CREATE_TIME          DATETIME  COMMENT '����ʱ��',
   LAST_TIME            DATETIME  COMMENT '���ʱ��',
   DESCRIPTION          VARCHAR(800)  COMMENT '����',
   PRIMARY KEY (BOARD_ID)
);

ALTER TABLE DASH_BOARD COMMENT '�Ǳ���';

/*==============================================================*/
/* Table: DASH_BOARD_PARAM                                      */
/*==============================================================*/
CREATE TABLE DASH_BOARD_PARAM
(
   PARAM_ID             BIGINT(20) NOT NULL AUTO_INCREMENT  COMMENT '�Ǳ�������ID',
   BOARD_ID             VARCHAR(50) NOT NULL  COMMENT '�Ǳ���ID',
   USER_ID              VARCHAR(50)  COMMENT '����ԱID',
   CONTENT              TEXT  COMMENT '��������',
   PRIMARY KEY (PARAM_ID)
);

ALTER TABLE DASH_BOARD_PARAM COMMENT '�Ǳ�������';

/*==============================================================*/
/* Table: DASH_CONFIG_VERSION                                   */
/*==============================================================*/
CREATE TABLE DASH_CONFIG_VERSION
(
   VERSION_ID           VARCHAR(50) NOT NULL  COMMENT '�汾ID',
   USER_ID              VARCHAR(50) NOT NULL  COMMENT '������ID',
   APP_ID               VARCHAR(50)  COMMENT 'Ӧ��ID',
   NAME                 VARCHAR(100) NOT NULL  COMMENT '����',
   CODE                 VARCHAR(50)  COMMENT '����',
   STATUS               INT(10)  COMMENT '״̬0δ��Ч1����2����3����',
   CREATE_TIME          DATETIME  COMMENT '����ʱ��',
   LAST_TIME            TIMESTAMP  COMMENT '���ʱ��',
   DESCRIPTION          VARCHAR(800)  COMMENT '����',
   PRIMARY KEY (VERSION_ID)
);

ALTER TABLE DASH_CONFIG_VERSION COMMENT '���ð汾';

/*==============================================================*/
/* Table: DASH_DATASET                                          */
/*==============================================================*/
CREATE TABLE DASH_DATASET
(
   DATASET_ID           VARCHAR(50) NOT NULL  COMMENT '���ݼ�ID',
   APP_ID               VARCHAR(50)  COMMENT 'Ӧ��ID',
   USER_ID              VARCHAR(50)  COMMENT '�û�ID',
   FOLDER_ID            VARCHAR(50)  COMMENT 'Ŀ¼ID',
   DATASOURCE_ID        VARCHAR(50)  COMMENT '����ԴID',
   NAME                 VARCHAR(100)  COMMENT '���ݼ�����',
   SNAME                VARCHAR(100)  COMMENT '���ƣ���ƣ�Ӣ��',
   CATA_NAME            VARCHAR(100)  COMMENT '����Դ�������ƣ��ɷ�����з���',
   CONTENT              TEXT  COMMENT '����JSON',
   CREATE_TIME          DATETIME  COMMENT '����ʱ��',
   LAST_TIME            DATETIME  COMMENT '���ʱ��',
   DESCRIPTION          VARCHAR(800)  COMMENT '����',
   PRIMARY KEY (DATASET_ID)
);

ALTER TABLE DASH_DATASET COMMENT '���ݼ�';

/*==============================================================*/
/* Table: DASH_DATASOURCE                                       */
/*==============================================================*/
CREATE TABLE DASH_DATASOURCE
(
   DATASOURCE_ID        VARCHAR(50) NOT NULL  COMMENT '����ԴID',
   APP_ID               VARCHAR(50)  COMMENT '����Ӧ��ID',
   USER_ID              VARCHAR(50)  COMMENT '�û�ID',
   NAME                 VARCHAR(100) NOT NULL  COMMENT '����Դ����',
   SNAME                VARCHAR(100)  COMMENT '���ƣ���ƣ�Ӣ��',
   CATA_NAME            VARCHAR(100)  COMMENT '����Դ�������ƣ��ɷ�����з���',
   TYPE                 VARCHAR(20) NOT NULL  COMMENT '����Դ���ͣ�JDBC��TEXTFILE��SOLR��ELASTIC��SAIKU',
   CONTENT              TEXT  COMMENT '����JSON����Բ�ͬ ���ͣ����治ͬJSON��ʽ',
   CREATE_TIME          DATETIME NOT NULL  COMMENT '����ʱ��',
   LAST_TIME            DATETIME  COMMENT '���ʱ��',
   DESCRIPTION          VARCHAR(800)  COMMENT '����',
   PRIMARY KEY (DATASOURCE_ID)
);

ALTER TABLE DASH_DATASOURCE COMMENT '����Դ��Ϣ';

/*==============================================================*/
/* Table: DASH_FOLDER                                           */
/*==============================================================*/
CREATE TABLE DASH_FOLDER
(
   FOLDER_ID            VARCHAR(50) NOT NULL  COMMENT 'Ŀ¼ID',
   PARENT_ID            VARCHAR(50)  COMMENT '��Ŀ¼ID',
   APP_ID               VARCHAR(50)  COMMENT 'Ӧ��ID',
   USER_ID              VARCHAR(50)  COMMENT '�û�ID',
   NAME                 VARCHAR(100)  COMMENT '����',
   SNAME                VARCHAR(100)  COMMENT '���ƣ���ƣ�Ӣ��',
   IS_PRIVATE           TINYINT(1)  COMMENT '�Ƿ�˽��1��0��',
   CREATE_TIME          DATETIME  COMMENT '����ʱ��',
   LAST_TIME            TIMESTAMP  COMMENT '���ʱ��',
   DESCRIPTION          VARCHAR(800)  COMMENT '����',
   PRIMARY KEY (FOLDER_ID)
);

ALTER TABLE DASH_FOLDER COMMENT 'Ŀ¼�ṹ';

/*==============================================================*/
/* Table: DASH_OPERATION_JOB                                    */
/*==============================================================*/
CREATE TABLE DASH_OPERATION_JOB
(
   JOB_ID               BIGINT(20) NOT NULL AUTO_INCREMENT  COMMENT '�ƻ����',
   APP_ID               VARCHAR(50)  COMMENT 'Ӧ��ID',
   USER_ID              VARCHAR(50)  COMMENT '����ԱID',
   JOB_NAME             VARCHAR(200)  COMMENT '��������',
   CRON_EXP             VARCHAR(200)  COMMENT 'cron������ʽ',
   START_DATE           DATETIME  COMMENT '��ʼʱ��',
   END_DATE             DATETIME  COMMENT '����ʱ��',
   JOB_TYPE             VARCHAR(200)  COMMENT '��������',
   JOB_CONFIG           TEXT  COMMENT '��������',
   JOB_STATUS           INT(10)  COMMENT '����״̬:0����1��ͣ',
   LAST_EXEC_TIME       TIMESTAMP  COMMENT '���ִ��ʱ��',
   EXEC_LOG             TEXT  COMMENT 'ִ����־',
   SPARE1               VARCHAR(255)  COMMENT '����1',
   SPARE2               VARCHAR(255)  COMMENT '����2',
   PRIMARY KEY (JOB_ID)
);

ALTER TABLE DASH_OPERATION_JOB COMMENT 'Ҫ������';

/*==============================================================*/
/* Table: DASH_WIDGET                                           */
/*==============================================================*/
CREATE TABLE DASH_WIDGET
(
   WIDGET_ID            VARCHAR(50) NOT NULL  COMMENT 'ͼ��ID',
   APP_ID               VARCHAR(50)  COMMENT 'Ӧ��ID',
   FOLDER_ID            VARCHAR(50)  COMMENT 'Ŀ¼ID',
   USER_ID              VARCHAR(50)  COMMENT '�û�ID',
   DATASOURCE_ID        VARCHAR(50)  COMMENT '����ԴID',
   DATASET_ID           VARCHAR(50)  COMMENT '���ݼ�ID',
   CATA_NAME            VARCHAR(100)  COMMENT 'ͼ���������',
   NAME                 VARCHAR(100)  COMMENT 'ͼ������',
   SNAME                VARCHAR(100)  COMMENT '���ƣ���ƣ�Ӣ��',
   CONTENT              TEXT  COMMENT '����JSON',
   CREATE_TIME          DATETIME  COMMENT '����ʱ��',
   LAST_TIME            DATETIME  COMMENT '���ʱ��',
   DESCRIPTION          VARCHAR(800)  COMMENT '����',
   PRIMARY KEY (WIDGET_ID)
);

ALTER TABLE DASH_WIDGET COMMENT '����ͼ��';

