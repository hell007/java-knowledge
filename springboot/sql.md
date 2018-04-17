/*
Navicat MySQL Data Transfer

Source Server         : mysql
Source Server Version : 50719
Source Host           : localhost:3306
Source Database       : jie

Target Server Type    : MYSQL
Target Server Version : 50719
File Encoding         : 65001

Date: 2018-04-17 11:48:22
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for jie_schedule_job
-- ----------------------------
DROP TABLE IF EXISTS `jie_schedule_job`;
CREATE TABLE `jie_schedule_job` (
  `id` varchar(20) NOT NULL COMMENT '任务ID',
  `jobname` varchar(40) DEFAULT NULL COMMENT '任务名称',
  `jobgroup` varchar(40) DEFAULT NULL COMMENT '任务分组',
  `jobstatus` tinyint(3) unsigned NOT NULL DEFAULT '1' COMMENT '任务状态 （0禁用， 1启用）',
  `status` tinyint(3) unsigned NOT NULL DEFAULT '0' COMMENT '审核状态（0 已创建， 1 审核通过， 2 审核驳回）',
  `cronexpression` varchar(40) NOT NULL COMMENT '任务运行时间表达式',
  `quartzclass` varchar(255) DEFAULT NULL COMMENT '定时任务处理类',
  `description` varchar(280) DEFAULT NULL COMMENT '描述信息',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of jie_schedule_job
-- ----------------------------
INSERT INTO `jie_schedule_job` VALUES ('1', 'job1', 'notice', '1', '1', '0/6 * * * * ?', 'com.self.task.ScheduleTask1', '清明节活动专场提醒');
INSERT INTO `jie_schedule_job` VALUES ('2', 'job2', 'message', '1', '1', '0/8 * * * * ?', 'com.self.task.ScheduleTask2', '消息发送提醒');

-- ----------------------------
-- Table structure for jie_user
-- ----------------------------
DROP TABLE IF EXISTS `jie_user`;
CREATE TABLE `jie_user` (
  `id` varchar(64) NOT NULL COMMENT '用户id',
  `username` varchar(20) NOT NULL COMMENT '用户名',
  `password` char(32) NOT NULL COMMENT '密码',
  `mobile` varchar(15) NOT NULL COMMENT '手机号码',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uid` (`id`),
  UNIQUE KEY `username` (`username`),
  UNIQUE KEY `mobile` (`mobile`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of jie_user
-- ----------------------------
INSERT INTO `jie_user` VALUES ('1', 'admin', 'wcPU4U/Fe8rUbk8k4pvp8g==', '13877777777');
INSERT INTO `jie_user` VALUES ('61afaadf-e6ea-11e7-b9a5-68f728b9bfe2', 'test', 'nJrx3defJSozx3dYCq+x8g==', '13888888888');
