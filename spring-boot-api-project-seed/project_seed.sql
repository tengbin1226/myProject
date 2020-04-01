/*
 Navicat Premium Data Transfer

 Source Server         : MySQL
 Source Server Type    : MySQL
 Source Server Version : 80019
 Source Host           : localhost:3306
 Source Schema         : project_seed

 Target Server Type    : MySQL
 Target Server Version : 80019
 File Encoding         : 65001

 Date: 02/04/2020 06:27:29
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `id` int(0) NOT NULL AUTO_INCREMENT,
  `username` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `password` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `nick_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `sex` int(0) NULL DEFAULT NULL,
  `register_date` datetime(0) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 11 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (1, '89921218@qq.com', '1ee04e0b1cb5af7367c80c22e42efd8b', '土豆', 1, '2017-06-23 14:24:23');
INSERT INTO `user` VALUES (2, '2@qq.com', '1ee04e0b1cb5af7367c80c22e42efd8b', '土豆-2', 1, '2017-06-23 14:24:23');
INSERT INTO `user` VALUES (3, '3@qq.com', '1ee04e0b1cb5af7367c80c22e42efd8b', '土豆-3', 1, '2017-06-23 14:24:23');
INSERT INTO `user` VALUES (4, '4@qq.com', '1ee04e0b1cb5af7367c80c22e42efd8b', '土豆-4', 1, '2017-06-23 14:24:23');
INSERT INTO `user` VALUES (5, '5@qq.com', '1ee04e0b1cb5af7367c80c22e42efd8b', '土豆-5', 1, '2017-06-23 14:24:23');
INSERT INTO `user` VALUES (6, '6@qq.com', '1ee04e0b1cb5af7367c80c22e42efd8b', '土豆-6', 1, '2017-06-23 14:24:23');
INSERT INTO `user` VALUES (7, '7@qq.com', '1ee04e0b1cb5af7367c80c22e42efd8b', '土豆-7', 1, '2017-06-23 14:24:23');
INSERT INTO `user` VALUES (8, '8@qq.com', '1ee04e0b1cb5af7367c80c22e42efd8b', '土豆-8', 1, '2017-06-23 14:24:23');
INSERT INTO `user` VALUES (9, '9@qq.com', '1ee04e0b1cb5af7367c80c22e42efd8b', '土豆-9', 1, '2017-06-23 14:24:23');
INSERT INTO `user` VALUES (10, '10@qq.com', '1ee04e0b1cb5af7367c80c22e42efd8b', '土豆-10', 1, '2017-06-23 14:24:23');

SET FOREIGN_KEY_CHECKS = 1;
