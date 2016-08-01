

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for sys_script
-- ----------------------------
DROP TABLE IF EXISTS `sys_script`;
CREATE TABLE `sys_script` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `type` varchar(255) NOT NULL COMMENT '1=SQL，2=存储过程',
  `module` varchar(255) DEFAULT NULL COMMENT '模块',
  `name` varchar(255) NOT NULL,
  `script` varchar(2000) NOT NULL COMMENT '执行脚本',
  `description` varchar(255) DEFAULT NULL,
  `flag` varchar(1) DEFAULT NULL COMMENT '是否有效，0无效，1或其他有效',
  PRIMARY KEY (`id`),
  UNIQUE KEY `I_SYS_SCRIPT_NAME` (`name`) USING HASH
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_script
-- ----------------------------
INSERT INTO `sys_script` VALUES ('1', '1', 'sys', 'script_list', 'select * from sys_script where name <> \'script_list\'', '系统管理接口列表', '1');


-- ----------------------------
-- Table structure for sys_scriptparam
-- ----------------------------
DROP TABLE IF EXISTS `sys_scriptparam`;
CREATE TABLE `sys_scriptparam` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `scriptId` int(11) DEFAULT NULL COMMENT '参数对应脚本的id',
  `name` varchar(255) DEFAULT NULL COMMENT '参数名称',
  `content` varchar(255) DEFAULT NULL COMMENT '参数内容，sql条件语句或者过程参数',
  PRIMARY KEY (`id`),
  KEY `I_SCRIPT_ID` (`scriptId`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

