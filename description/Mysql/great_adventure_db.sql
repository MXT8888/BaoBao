/*
Navicat MySQL Data Transfer

Source Server         : mysql
Source Server Version : 50721
Source Host           : localhost:3306
Source Database       : great_adventure_db

Target Server Type    : MYSQL
Target Server Version : 50721
File Encoding         : 65001

Date: 2019-06-16 14:09:05
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for deal
-- ----------------------------
DROP TABLE IF EXISTS `deal`;
CREATE TABLE `deal` (
  `deal_id` bigint(9) NOT NULL AUTO_INCREMENT COMMENT '任务记录id',
  `user_id` bigint(9) NOT NULL COMMENT '用户id',
  `content` varchar(20) NOT NULL COMMENT '交易内容',
  `sum` double(50,2) NOT NULL COMMENT '金额',
  `time` datetime NOT NULL COMMENT '交易时间',
  PRIMARY KEY (`deal_id`),
  KEY `deal_user_id` (`user_id`),
  CONSTRAINT `deal_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=114 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for partner
-- ----------------------------
DROP TABLE IF EXISTS `partner`;
CREATE TABLE `partner` (
  `partner_id` bigint(9) NOT NULL AUTO_INCREMENT COMMENT '参与id',
  `user_id` bigint(9) NOT NULL COMMENT '参与任务的用户id',
  `task_id` bigint(9) NOT NULL COMMENT '任务id',
  `image_url` varchar(255) DEFAULT NULL COMMENT '图片地址',
  `is_public` int(1) NOT NULL COMMENT '是否公开',
  `note` varchar(200) DEFAULT NULL COMMENT '说明',
  `status` varchar(50) NOT NULL COMMENT '审核状态',
  `bounty` double(50,2) NOT NULL DEFAULT '0.00' COMMENT '获得的赏金',
  `time` datetime NOT NULL COMMENT '做任务的时间',
  PRIMARY KEY (`partner_id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for task
-- ----------------------------
DROP TABLE IF EXISTS `task`;
CREATE TABLE `task` (
  `task_id` bigint(9) NOT NULL AUTO_INCREMENT COMMENT '任务id',
  `user_id` bigint(9) NOT NULL COMMENT '发布任务的用户id',
  `status` varchar(50) NOT NULL COMMENT '任务状态',
  `claim` varchar(200) DEFAULT NULL COMMENT '任务要求',
  `bounty` double(50,2) NOT NULL DEFAULT '0.00' COMMENT '剩余赏金',
  `total_bounty` double(100,2) NOT NULL DEFAULT '0.00' COMMENT '总赏金额',
  `red` int(3) NOT NULL DEFAULT '0' COMMENT '已经领取的红包数',
  `total_red` int(3) NOT NULL COMMENT '红包总量',
  `partner` int(3) NOT NULL DEFAULT '0' COMMENT '已经参与的人数',
  `total_partner` int(3) NOT NULL COMMENT '总参与人数',
  `time` datetime NOT NULL COMMENT '发布任务时间',
  PRIMARY KEY (`task_id`)
) ENGINE=InnoDB AUTO_INCREMENT=93 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `user_id` bigint(9) NOT NULL AUTO_INCREMENT COMMENT '用户id',
  `open_id` varchar(255) NOT NULL COMMENT '微信唯一标识',
  `overage` varchar(50) NOT NULL COMMENT '余额',
  `nickname` varchar(255) DEFAULT NULL COMMENT '用户昵称',
  `head_portrait` varchar(255) DEFAULT NULL COMMENT '用户头像地址',
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;
