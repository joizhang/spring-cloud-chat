DROP DATABASE IF EXISTS `chat_config`;

CREATE DATABASE `chat_config` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;

USE chat_config;

SET NAMES utf8;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for config_info
-- ----------------------------
DROP TABLE IF EXISTS `config_info`;
CREATE TABLE `config_info` (
    `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
    `data_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'data_id',
    `group_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
    `content` longtext CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'content',
    `md5` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT 'md5',
    `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
    `src_user` text CHARACTER SET utf8 COLLATE utf8_bin COMMENT 'source user',
    `src_ip` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT 'source ip',
    `app_name` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
    `tenant_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT '' COMMENT '租户字段',
    `c_desc` varchar(256) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
    `c_use` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
    `effect` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
    `type` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
    `c_schema` text CHARACTER SET utf8 COLLATE utf8_bin,
    `encrypted_data_key` text CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '秘钥',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_configinfo_datagrouptenant` (`data_id`,`group_id`,`tenant_id`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='config_info';

-- ----------------------------
-- Records of config_info
-- ----------------------------
BEGIN;
INSERT INTO `config_info` VALUES (1, 'application-dev.yml','DEFAULT_GROUP','# 配置文件加密根密码\njasypt:\n  encryptor:\n    password: chat #根密码\n    algorithm: PBEWithMD5AndDES\n    iv-generator-classname: org.jasypt.iv.NoIvGenerator\n    \n# Spring 相关\nspring:\n  cache:\n    type: redis\n  redis:\n    host: xinlin-04\n  cloud:\n    sentinel:\n      eager: true\n      transport:\n        dashboard: chat-sentinel:5003\n\n# 暴露监控端点\nmanagement:\n  endpoints:\n    web:\n      exposure:\n        include: "*"  \n  endpoint:\n    health:\n      show-details: ALWAYS\n\n\n# feign 配置\nfeign:\n  sentinel:\n    enabled: true\n  okhttp:\n    enabled: true\n  httpclient:\n    enabled: false\n  client:\n    config:\n      default:\n        connectTimeout: 10000\n        readTimeout: 10000\n  compression:\n    request:\n      enabled: true\n    response:\n      enabled: true\n\n# mybaits-plus配置\nmybatis-plus:\n  mapper-locations: classpath:/mapper/*Mapper.xml\n  global-config:\n    banner: false\n    db-config:\n      id-type: auto\n      table-underline: true\n      logic-delete-value: 1\n      logic-not-delete-value: 0\n  configuration:\n    map-underscore-to-camel-case: true\n\n# spring security 配置\nsecurity:\n  oauth2:\n    # 通用放行URL，服务个性化，请在对应配置文件覆盖\n    ignore:\n      urls:\n        - /v3/api-docs\n        - /actuator/**\n\n# swagger 配置\nswagger:\n  enabled: true\n  title: Swagger API\n  gateway: http://${GATEWAY_HOST:chat-gateway}:${GATEWAY-PORT:9999}\n  token-url: ${swagger.gateway}/auth/oauth2/token\n  scope: server\n  services:\n    chat-upms-biz: admin\n    chat-web-biz: chat','3db8cb816b7d65a099975d3ef087483e','2022-05-08 12:10:37','2023-04-19 19:01:47','nacos','172.19.0.1','','','','','','yaml','','');
INSERT INTO `config_info` VALUES (2, 'chat-auth-dev.yml','DEFAULT_GROUP','# 数据源\nspring:\n  freemarker:\n    allow-request-override: false\n    allow-session-override: false\n    cache: true\n    charset: UTF-8\n    check-template-location: true\n    content-type: text/html\n    enabled: true\n    expose-request-attributes: false\n    expose-session-attributes: false\n    expose-spring-macro-helpers: true\n    prefer-file-system-access: true\n    suffix: .ftl\n    template-loader-path: classpath:/templates/','74f53b71c7799aa754da75662378b93c','2022-05-08 12:10:37','2022-06-04 14:15:35','nacos','127.0.0.1','','','','','','yaml','','');
INSERT INTO `config_info` VALUES (3, 'chat-codegen-dev.yml','DEFAULT_GROUP','## spring security 配置\nsecurity:\n  oauth2:\n    client:\n      client-id: ENC(27v1agvAug87ANOVnbKdsw==)\n      client-secret: ENC(VbnkopxrwgbFVKp+UxJ2pg==)\n      scope: server\n\n# 数据源配置\nspring:\n  datasource:\n    type: com.zaxxer.hikari.HikariDataSource\n    driver-class-name: com.mysql.cj.jdbc.Driver\n    username: ENC(FBtIssHG7MdjezT4gVQGpA==) # test\n    password: ENC(/PjMTli3WPIcgcTgXdemreY4iAI7lyJP)\n    url: jdbc:mysql://xinlin-04:3306/chat_codegen?characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=false&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=Asia/Shanghai&nullCatalogMeansCurrent=true&allowPublicKeyRetrieval=true\n  resources:\n    static-locations: classpath:/static/,classpath:/views/\n','4b908d9df1521b8eb97fdb35dddc9a20','2022-05-08 12:10:37','2023-04-24 18:01:51','nacos','172.19.0.1','','','','','','yaml','','');
INSERT INTO `config_info` VALUES (4, 'chat-gateway-dev.yml','DEFAULT_GROUP','spring:\n  cloud:\n    gateway:\n      locator:\n        enabled: true\n      routes:\n        # 认证中心\n        - id: chat-auth\n          uri: lb://chat-auth\n          predicates:\n            - Path=/auth/**\n          filters:\n            # 验证码处理\n            - ValidateCodeGatewayFilter\n            # 前端密码解密\n            - PasswordDecoderFilter\n        # UPMS 模块\n        - id: chat-upms-biz\n          uri: lb://chat-upms-biz\n          predicates:\n            - Path=/admin/**\n          filters:\n            # 限流配置\n            - name: RequestRateLimiter\n              args:\n                key-resolver: ''#{@remoteAddrKeyResolver}''\n                redis-rate-limiter.replenishRate: 100\n                redis-rate-limiter.burstCapacity: 200\n        # 代码生成模块\n        # - id: chat-codegen\n        #   uri: lb://chat-codegen\n        #   predicates:\n        #     - Path=/gen/**\n        # 固定路由转发配置 无修改\n        # 聊天模块\n        - id: chat-web-biz-svc\n          uri: lb://chat-web-biz\n          predicates:\n            - Path=/chat/svc/**\n          filters:\n            # 限流配置\n            - name: RequestRateLimiter\n              args:\n                key-resolver: ''#{@remoteAddrKeyResolver}''\n                redis-rate-limiter.replenishRate: 100\n                redis-rate-limiter.burstCapacity: 200\n        # 聊天模块ws\n        - id: chat-web-biz-ws\n          uri: lb://chat-web-biz\n          predicates:\n            - Path=/chat/ws/**\n          filters:\n            # 限流配置\n            - name: RequestRateLimiter\n              args:\n                key-resolver: ''#{@remoteAddrKeyResolver}''\n                redis-rate-limiter.replenishRate: 100\n                redis-rate-limiter.burstCapacity: 200\n        - id: openapi\n          uri: lb://chat-gateway\n          predicates:\n            - Path=/v3/api-docs/**\n          filters:\n            - RewritePath=/v3/api-docs/(?<path>.*), /$\\{path}/$\\{path}/v3/api-docs\n\ngateway:\n  encode-key: ''thanks,pig4cloud''\n  ignore-clients:\n    - test\n    - client','f5b82efa752976e4aed08e85c13a008c','2022-05-08 12:10:37','2023-04-22 16:33:42','nacos','172.19.0.1','','','','','','yaml','','');
INSERT INTO `config_info` VALUES (5, 'chat-monitor-dev.yml','DEFAULT_GROUP','spring:\n  autoconfigure:\n    exclude: com.joizhang.chat.common.core.config.JacksonConfiguration\n  # 安全配置\n  security:\n    user:\n      name: ENC(K24SXIYix81ef7ZQaWHgqQ==)     # admin\n      password: ENC(T2bJImy7KWiM1QxT7X8lkA==) # admin\n','fe78a3d8e58f7b630390b548efdea209','2022-05-08 12:10:37','2023-04-14 12:53:05','nacos','172.19.0.1','','','','','','yaml','','');
INSERT INTO `config_info` VALUES (6, 'chat-upms-biz-dev.yml','DEFAULT_GROUP','security:\n  oauth2:\n    client:\n      client-id: ENC(imENTO7M8bLO38LFSIxnzw==)\n      client-secret: ENC(i3cDFhs26sa2Ucrfz2hnQw==)\n      scope: server\n\n# 数据源\nspring:\n  datasource:\n    type: com.zaxxer.hikari.HikariDataSource\n    driver-class-name: com.mysql.cj.jdbc.Driver\n    username: ENC(B6MRelC+0KZziFEKNX7OwQ==)\n    password: ENC(LXaP7OTcW1V/fijimCJT0zLZgh9o59Oj)\n    url: jdbc:mysql://xinlin-04:3306/chat_sys?characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=false&allowMultiQueries=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=Asia/Shanghai&nullCatalogMeansCurrent=true&allowPublicKeyRetrieval=true\n\n# 文件上传相关 支持阿里云、华为云、腾讯、minio\noss:\n  endpoint: http://minio.pig4cloud.com\n  accessKey: lengleng\n  secretKey: lengleng\n  bucket-name: tmp','a680e8e3dda155fcf28c8e28ae953187','2022-05-08 12:10:37','2023-04-09 21:31:46','nacos','172.19.0.1','','','','','','yaml','','');
INSERT INTO `config_info` VALUES (7, 'chat-web-biz-dev.yml','DEFAULT_GROUP','# 数据源\nspring:\n  datasource:\n    type: com.zaxxer.hikari.HikariDataSource\n    driver-class-name: com.mysql.cj.jdbc.Driver\n    username: ENC(B6MRelC+0KZziFEKNX7OwQ==) # test\n    password: ENC(LXaP7OTcW1V/fijimCJT0zLZgh9o59Oj)\n    url: jdbc:mysql://xinlin-04:3306/chat_web?characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=false&allowMultiQueries=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=Asia/Shanghai&nullCatalogMeansCurrent=true&allowPublicKeyRetrieval=true\n  rabbitmq:\n    host: localhost\n    port: 5672\n    username: ENC(msCBv+Uv8/21qE1p+Acqhg==) # admin\n    password: ENC(pSHCCpbOyq6+nhH6povIZQ==) # admin\n    virtual-host: /\n    # 手动提交消息\n    listener:\n      simple:\n        acknowledge-mode: manual\n      direct:\n        acknowledge-mode: manual','f4a7f5a41518bce8005223403b570d19','2023-04-15 15:55:55','2023-04-24 18:04:17','nacos','172.19.0.1','','','','','','yaml','','');
COMMIT;

-- ----------------------------
-- Table structure for config_info_aggr
-- ----------------------------
DROP TABLE IF EXISTS `config_info_aggr`;
CREATE TABLE `config_info_aggr` (
`id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
`data_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'data_id',
`group_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'group_id',
`datum_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'datum_id',
`content` longtext CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '内容',
`gmt_modified` datetime NOT NULL COMMENT '修改时间',
`app_name` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
`tenant_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT '' COMMENT '租户字段',
PRIMARY KEY (`id`),
UNIQUE KEY `uk_configinfoaggr_datagrouptenantdatum` (`data_id`,`group_id`,`tenant_id`,`datum_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='增加租户字段';

-- ----------------------------
-- Records of config_info_aggr
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for config_info_beta
-- ----------------------------
DROP TABLE IF EXISTS `config_info_beta`;
CREATE TABLE `config_info_beta` (
    `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
    `data_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'data_id',
    `group_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'group_id',
    `app_name` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT 'app_name',
    `content` longtext CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'content',
    `beta_ips` varchar(1024) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT 'betaIps',
    `md5` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT 'md5',
    `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
    `src_user` text CHARACTER SET utf8 COLLATE utf8_bin COMMENT 'source user',
    `src_ip` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT 'source ip',
    `tenant_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT '' COMMENT '租户字段',
    `encrypted_data_key` text CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '秘钥',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_configinfobeta_datagrouptenant` (`data_id`,`group_id`,`tenant_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='config_info_beta';

-- ----------------------------
-- Records of config_info_beta
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for config_info_tag
-- ----------------------------
DROP TABLE IF EXISTS `config_info_tag`;
CREATE TABLE `config_info_tag` (
    `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
    `data_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'data_id',
    `group_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'group_id',
    `tenant_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT '' COMMENT 'tenant_id',
    `tag_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'tag_id',
    `app_name` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT 'app_name',
    `content` longtext CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'content',
    `md5` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT 'md5',
    `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
    `src_user` text CHARACTER SET utf8 COLLATE utf8_bin COMMENT 'source user',
    `src_ip` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT 'source ip',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_configinfotag_datagrouptenanttag` (`data_id`,`group_id`,`tenant_id`,`tag_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='config_info_tag';

-- ----------------------------
-- Records of config_info_tag
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for config_tags_relation
-- ----------------------------
DROP TABLE IF EXISTS `config_tags_relation`;
CREATE TABLE `config_tags_relation` (
    `id` bigint NOT NULL COMMENT 'id',
    `tag_name` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'tag_name',
    `tag_type` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT 'tag_type',
    `data_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'data_id',
    `group_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'group_id',
    `tenant_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT '' COMMENT 'tenant_id',
    `nid` bigint NOT NULL AUTO_INCREMENT,
    PRIMARY KEY (`nid`),
    UNIQUE KEY `uk_configtagrelation_configidtag` (`id`,`tag_name`,`tag_type`),
    KEY `idx_tenant_id` (`tenant_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='config_tag_relation';

-- ----------------------------
-- Records of config_tags_relation
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for group_capacity
-- ----------------------------
DROP TABLE IF EXISTS `group_capacity`;
CREATE TABLE `group_capacity` (
    `id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `group_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL DEFAULT '' COMMENT 'Group ID，空字符表示整个集群',
    `quota` int unsigned NOT NULL DEFAULT '0' COMMENT '配额，0表示使用默认值',
    `usage` int unsigned NOT NULL DEFAULT '0' COMMENT '使用量',
    `max_size` int unsigned NOT NULL DEFAULT '0' COMMENT '单个配置大小上限，单位为字节，0表示使用默认值',
    `max_aggr_count` int unsigned NOT NULL DEFAULT '0' COMMENT '聚合子配置最大个数，，0表示使用默认值',
    `max_aggr_size` int unsigned NOT NULL DEFAULT '0' COMMENT '单个聚合数据的子配置大小上限，单位为字节，0表示使用默认值',
    `max_history_count` int unsigned NOT NULL DEFAULT '0' COMMENT '最大变更历史数量',
    `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_group_id` (`group_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='集群、各Group容量信息表';

-- ----------------------------
-- Records of group_capacity
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for his_config_info
-- ----------------------------
DROP TABLE IF EXISTS `his_config_info`;
CREATE TABLE `his_config_info` (
    `id` bigint unsigned NOT NULL,
    `nid` bigint unsigned NOT NULL AUTO_INCREMENT,
    `data_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
    `group_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
    `app_name` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT 'app_name',
    `content` longtext CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
    `md5` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
    `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `src_user` text CHARACTER SET utf8 COLLATE utf8_bin,
    `src_ip` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
    `op_type` char(10) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
    `tenant_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT '' COMMENT '租户字段',
    `encrypted_data_key` text CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '秘钥',
    PRIMARY KEY (`nid`),
    KEY `idx_gmt_create` (`gmt_create`),
    KEY `idx_gmt_modified` (`gmt_modified`),
    KEY `idx_did` (`data_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='多租户改造';

-- ----------------------------
-- Records of his_config_info
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for permissions
-- ----------------------------
DROP TABLE IF EXISTS `permissions`;
CREATE TABLE `permissions` (
    `role` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
    `resource` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
    `action` varchar(8) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
    UNIQUE KEY `uk_role_permission` (`role`,`resource`,`action`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

-- ----------------------------
-- Records of permissions
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for roles
-- ----------------------------
DROP TABLE IF EXISTS `roles`;
CREATE TABLE `roles` (
    `username` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
    `role` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
    UNIQUE KEY `idx_user_role` (`username`,`role`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

-- ----------------------------
-- Records of roles
-- ----------------------------
BEGIN;
INSERT INTO `roles` VALUES ('nacos', 'ROLE_ADMIN');
COMMIT;

-- ----------------------------
-- Table structure for tenant_capacity
-- ----------------------------
DROP TABLE IF EXISTS `tenant_capacity`;
CREATE TABLE `tenant_capacity` (
    `id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `tenant_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL DEFAULT '' COMMENT 'Tenant ID',
    `quota` int unsigned NOT NULL DEFAULT '0' COMMENT '配额，0表示使用默认值',
    `usage` int unsigned NOT NULL DEFAULT '0' COMMENT '使用量',
    `max_size` int unsigned NOT NULL DEFAULT '0' COMMENT '单个配置大小上限，单位为字节，0表示使用默认值',
    `max_aggr_count` int unsigned NOT NULL DEFAULT '0' COMMENT '聚合子配置最大个数',
    `max_aggr_size` int unsigned NOT NULL DEFAULT '0' COMMENT '单个聚合数据的子配置大小上限，单位为字节，0表示使用默认值',
    `max_history_count` int unsigned NOT NULL DEFAULT '0' COMMENT '最大变更历史数量',
    `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_tenant_id` (`tenant_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='租户容量信息表';

-- ----------------------------
-- Records of tenant_capacity
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for tenant_info
-- ----------------------------
DROP TABLE IF EXISTS `tenant_info`;
CREATE TABLE `tenant_info` (
    `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
    `kp` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'kp',
    `tenant_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT '' COMMENT 'tenant_id',
    `tenant_name` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT '' COMMENT 'tenant_name',
    `tenant_desc` varchar(256) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT 'tenant_desc',
    `create_source` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT 'create_source',
    `gmt_create` bigint NOT NULL COMMENT '创建时间',
    `gmt_modified` bigint NOT NULL COMMENT '修改时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_tenant_info_kptenantid` (`kp`,`tenant_id`),
    KEY `idx_tenant_id` (`tenant_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='tenant_info';

-- ----------------------------
-- Records of tenant_info
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for users
-- ----------------------------
DROP TABLE IF EXISTS `users`;
CREATE TABLE `users` (
    `username` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
    `password` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
    `enabled` tinyint(1) NOT NULL,
    PRIMARY KEY (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

-- ----------------------------
-- Records of users
-- ----------------------------
BEGIN;
INSERT INTO `users` VALUES ('nacos', '$2a$10$EuWPZHzz32dJN7jexM34MOeYirDdFAZm2kuWj7VEOJhhZkDrxfvUu', 1);
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
