/*
Navicat MySQL Data Transfer

Source Server         : conn
Source Server Version : 50528
Source Host           : localhost:3306
Source Database       : kopm

Target Server Type    : MYSQL
Target Server Version : 50528
File Encoding         : 65001

Date: 2016-07-11 23:02:39
*/

SET FOREIGN_KEY_CHECKS=0;
-- ----------------------------
-- Table structure for `user`
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `uid` int(10) NOT NULL AUTO_INCREMENT,
  `username` varchar(50) NOT NULL,
  `password` varchar(50) NOT NULL,
  `score` int(5) DEFAULT NULL,
  PRIMARY KEY (`uid`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES ('4', 'davionli', '123456', '20');
INSERT INTO `user` VALUES ('5', 'pm', '000000', '0');
INSERT INTO `user` VALUES ('6', 'firminli', '123456', '10');
INSERT INTO `user` VALUES ('7', 'violachen', '123456', '10');
INSERT INTO `user` VALUES ('8', 'fangzhan', '123456', '10');
INSERT INTO `user` VALUES ('9', 'chmwang', '123456', '10');
INSERT INTO `user` VALUES ('10', 'perphi', '123456', '10');
INSERT INTO `user` VALUES ('11', 'nimo', '123456', '10');
INSERT INTO `user` VALUES ('12', 'jeanzhu', '123456', '10');
INSERT INTO `user` VALUES ('13', 'nickel', '123456', '10');
