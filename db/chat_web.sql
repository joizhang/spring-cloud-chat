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
    `id` bigint NOT NULL,
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
    PRIMARY KEY (`id`),
    KEY `customer_idx1_username` (`username`),
    KEY `customer_idx2_phone` (`phone`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='聊天用户表';

-- ----------------------------
-- Table structure for chat_message
-- ----------------------------
DROP TABLE IF EXISTS `chat_message`;
CREATE TABLE `chat_message` (
    `id` bigint NOT NULL,
    `sender_id` bigint NOT NULL COMMENT '发送者ID',
    `receiver_id` bigint NOT NULL COMMENT '接收者ID',
    `content` varchar(5000) NOT NULL COMMENT '消息内容',
    `content_type` int NOT NULL COMMENT '消息类型：1-text, 2-emoji, 3-image, 4-audio, 5-video',
    PRIMARY KEY (`id`),
    KEY `message_idx1_from_to` (`sender_id`, `receiver_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='聊天消息表';

-- ----------------------------
-- Table structure for chat_friend
-- ----------------------------
DROP TABLE IF EXISTS `chat_friend`;
CREATE TABLE `chat_friend` (
    `id` bigint NOT NULL,
    `user_id` bigint NOT NULL COMMENT '用户id',
    `friend_id` bigint NOT NULL COMMENT '朋友id',
    `remark` varchar(256) NOT NULL COMMENT '备注',
    `del_flag` char(1) DEFAULT '0' COMMENT '0-正常，1-删除',
    `create_time` datetime DEFAULT NULL COMMENT '创建时间',
    `update_time` datetime DEFAULT NULL COMMENT '修改时间',
    `create_by` varchar(64) DEFAULT NULL COMMENT '创建者',
    `update_by` varchar(64) DEFAULT NULL COMMENT '更新人',
    PRIMARY KEY (`id`),
    KEY `friend_idx1_from_to` (`user_id`, `friend_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='朋友关系表';

-- ----------------------------
-- Table structure for chat_friend_request
-- ----------------------------
DROP TABLE IF EXISTS `chat_friend_request`;
CREATE TABLE `chat_friend_request` (
    `id` bigint NOT NULL,
    `senderId` bigint DEFAULT NULL COMMENT '好友请求发送着',
    `receiverId` bigint DEFAULT NULL COMMENT '好友请求接收者',
    `status` int DEFAULT NULL COMMENT '好友请求状态：1-PENDING, 2-ACCEPTED, 3-DECLINED',
    `create_time` datetime DEFAULT NULL COMMENT '创建时间',
    `update_time` datetime DEFAULT NULL COMMENT '修改时间',
    `create_by` varchar(64) DEFAULT NULL COMMENT '创建者',
    `update_by` varchar(64) DEFAULT NULL COMMENT '更新人',
    PRIMARY KEY (`id`),
    KEY `friend_request_idx1_sender` (`senderId`),
    KEY `friend_request_idx1_receiver` (`receiverId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='好友请求表';

SET FOREIGN_KEY_CHECKS = 1;