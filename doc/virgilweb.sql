/*
Navicat MySQL Data Transfer

Source Server         : localhost_3306
Source Server Version : 50621
Source Host           : localhost:3306
Source Database       : virgilweb

Target Server Type    : MYSQL
Target Server Version : 50621
File Encoding         : 65001

Date: 2017-02-15 23:25:50
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for sys_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu` (
  `menu_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `icon` varchar(255) DEFAULT NULL,
  `menu_name` varchar(255) DEFAULT NULL,
  `ztree_open` bit(1) DEFAULT NULL,
  `order_num` int(11) DEFAULT NULL,
  `parent_id` bigint(20) DEFAULT NULL,
  `parent_name` varchar(255) DEFAULT NULL,
  `perms` varchar(255) DEFAULT NULL,
  `menu_type` int(11) DEFAULT NULL,
  `menu_url` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`menu_id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_menu
-- ----------------------------
INSERT INTO `sys_menu` VALUES ('1', 'fa fa-cog', '系统管理', null, null, '0', null, null, '0', null);
INSERT INTO `sys_menu` VALUES ('2', 'fa fa-user', '管理员列表', null, '1', '1', null, null, '1', 'sys/user.html');
INSERT INTO `sys_menu` VALUES ('3', 'fa fa-user-secret', '角色管理', null, '2', '1', null, null, '1', 'sys/role.html');
INSERT INTO `sys_menu` VALUES ('4', 'fa fa-th-list', '菜单管理', null, '3', '1', null, null, '1', 'sys/menu.html');
INSERT INTO `sys_menu` VALUES ('5', 'fa fa-bug', 'SQL监控', null, '4', '1', null, null, '1', 'druid/sql.html');
INSERT INTO `sys_menu` VALUES ('6', 'fa fa-tasks', '定时任务', null, '5', '1', null, null, '1', 'sys/schedule.html');
INSERT INTO `sys_menu` VALUES ('7', null, '查看', null, '0', '6', null, 'sys:schedule:list,sys:schedule:info', '2', null);
INSERT INTO `sys_menu` VALUES ('8', null, '查看', null, '0', '2', null, 'sys:user:list,sys:user:info', '2', null);

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role` (
  `role_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `create_time` datetime DEFAULT NULL,
  `remark` varchar(255) DEFAULT NULL,
  `role_name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_role
-- ----------------------------

-- ----------------------------
-- Table structure for sys_role_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_menu`;
CREATE TABLE `sys_role_menu` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `menu_id` bigint(20) DEFAULT NULL,
  `role_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_role_menu
-- ----------------------------

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user` (
  `user_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `create_time` datetime DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `mobile` varchar(255) DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  `user_name` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES ('1', '2017-01-31 12:56:04', 'virgil1993@163.com', '18801911004', '1', 'admin', '8c6976e5b5410415bde908bd4dee15dfb167a9c873fc4bb8a81f6f2ab448a918');

-- ----------------------------
-- Table structure for sys_user_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role` (
  `user_role_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `role_id` bigint(20) DEFAULT NULL,
  `user_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`user_role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_user_role
-- ----------------------------
