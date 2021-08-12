-- ----------------------------
-- 动态表单数据存储
-- ----------------------------
DROP TABLE IF EXISTS `formdata`;
CREATE TABLE `formdata`
(
    `PROC_DEF_ID_`   varchar(64)                                            DEFAULT NULL,
    `PROC_INST_ID_`  varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
    `FORM_KEY_`      varchar(255)                                           DEFAULT NULL,
    `Control_ID_`    varchar(100)                                           DEFAULT NULL,
    `Control_VALUE_` varchar(2000)                                          DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
