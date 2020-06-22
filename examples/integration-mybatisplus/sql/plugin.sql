SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for plugin_data
-- ----------------------------
DROP TABLE IF EXISTS `plugin_data`;
CREATE TABLE `plugin_data` (
  `plugin_id` varchar(32) NOT NULL,
  `name` varchar(32) DEFAULT NULL,
  `type` int(1) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`plugin_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
