DROP DATABASE IF EXISTS `chat_web`;

CREATE DATABASE `chat_web` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_bin;

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

USE `chat_web`;

-- ----------------------------
-- Table structure for chat_customer
-- ----------------------------
DROP TABLE IF EXISTS `chat_customer`;
CREATE TABLE `chat_customer` (
    `user_id` bigint NOT NULL,
    `username` varchar(64) NOT NULL COMMENT '用户名',
    `password` varchar(255) NOT NULL COMMENT '密码',
    `salt` varchar(255) DEFAULT NULL COMMENT '随机盐',
    `phone` varchar(20) DEFAULT NULL COMMENT '简介',
    `avatar` varchar(255) DEFAULT NULL COMMENT '头像',
    `lock_flag` char(1) DEFAULT '0' COMMENT '0-正常，9-锁定',
    `del_flag` char(1) DEFAULT '0' COMMENT '0-正常，1-删除',
    `create_time` datetime DEFAULT NULL COMMENT '创建时间',
    `update_time` datetime DEFAULT NULL COMMENT '修改时间',
    `create_by` varchar(64) DEFAULT NULL COMMENT '创建者',
    `update_by` varchar(64) DEFAULT NULL COMMENT '更新人',
    PRIMARY KEY (`user_id`),
    KEY `user_idx1_username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='聊天用户表';

-- ----------------------------
-- Table structure for chat_message
-- ----------------------------
DROP TABLE IF EXISTS `chat_message`;
CREATE TABLE `chat_message` (
    `message_id` bigint NOT NULL,
    `message_from` bigint NOT NULL COMMENT '发送者ID',
    `message_to` bigint NOT NULL COMMENT '接收者ID',
    `content` varchar(5000) NOT NULL COMMENT '内容',
    PRIMARY KEY (`message_id`),
    KEY `message_idx1_from_to` (`message_from`, `message_to`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='聊天消息表';

SET FOREIGN_KEY_CHECKS = 1;