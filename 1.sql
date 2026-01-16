/*
 Navicat Premium Dump SQL

 Source Server         : localhost_3306
 Source Server Type    : MySQL
 Source Server Version : 80401 (8.4.1)
 Source Host           : localhost:3306
 Source Schema         : springboot2z04j

 Target Server Type    : MySQL
 Target Server Version : 80401 (8.4.1)
 File Encoding         : 65001

 Date: 15/01/2026 17:38:55
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for role
-- ----------------------------
DROP TABLE IF EXISTS `role`;
CREATE TABLE `role`  (
  `id` bigint NOT NULL,
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `addtime` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_role_code`(`code` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for aboutus
-- ----------------------------
DROP TABLE IF EXISTS `aboutus`;
CREATE TABLE `aboutus`  (
  `id` bigint NOT NULL,
  `title` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `subtitle` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `content` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL,
  `picture1` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `picture2` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `picture3` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `addtime` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of aboutus
-- ----------------------------

-- ----------------------------
-- Table structure for address
-- ----------------------------
DROP TABLE IF EXISTS `address`;
CREATE TABLE `address`  (
  `id` bigint NOT NULL,
  `userid` bigint NOT NULL,
  `address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `isdefault` int NULL DEFAULT 0,
  `addtime` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_address_user`(`userid` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of address
-- ----------------------------

-- ----------------------------
-- Table structure for cart
-- ----------------------------
DROP TABLE IF EXISTS `cart`;
CREATE TABLE `cart`  (
  `id` bigint NOT NULL,
  `tablename` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `userid` bigint NOT NULL,
  `goodid` bigint NOT NULL,
  `goodname` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `picture` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `buynumber` int NOT NULL,
  `price` decimal(10, 2) NOT NULL,
  `discountprice` decimal(10, 2) NULL DEFAULT NULL,
  `addtime` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_cart_user`(`userid` ASC) USING BTREE,
  INDEX `idx_cart_good`(`goodid` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of cart
-- ----------------------------

-- ----------------------------
-- Table structure for chat
-- ----------------------------
DROP TABLE IF EXISTS `chat`;
CREATE TABLE `chat`  (
  `id` bigint NOT NULL,
  `userid` bigint NOT NULL,
  `adminid` bigint NULL DEFAULT NULL,
  `ask` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL,
  `reply` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL,
  `isreply` int NULL DEFAULT 0,
  `addtime` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_chat_user`(`userid` ASC) USING BTREE,
  INDEX `idx_chat_admin`(`adminid` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of chat
-- ----------------------------

-- ----------------------------
-- Table structure for discusszhuangxiushangpin
-- ----------------------------
DROP TABLE IF EXISTS `discusszhuangxiushangpin`;
CREATE TABLE `discusszhuangxiushangpin`  (
  `id` bigint NOT NULL,
  `refid` bigint NOT NULL,
  `userid` bigint NOT NULL,
  `avatarurl` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `nickname` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `content` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL,
  `reply` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL,
  `addtime` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_discuss_ref`(`refid` ASC) USING BTREE,
  INDEX `idx_discuss_user`(`userid` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of discusszhuangxiushangpin
-- ----------------------------

-- ----------------------------
-- Table structure for gongsixinxi
-- ----------------------------
DROP TABLE IF EXISTS `gongsixinxi`;
CREATE TABLE `gongsixinxi`  (
  `id` bigint NOT NULL,
  `gongsimingcheng` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `gongsizhaopian` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `gongsidizhi` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `jingyingfanwei` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL,
  `zixundianhua` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `gongsijieshao` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL,
  `addtime` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of gongsixinxi
-- ----------------------------
INSERT INTO `gongsixinxi` VALUES (1700001001001, '星辰装饰有限公司', '', '北京市朝阳区望京SOHO', '室内设计, 全屋定制, 工装改造', '010-88886666', '专注现代简约风格与商业空间改造，团队经验丰富。', '2025-12-01 10:00:00');
INSERT INTO `gongsixinxi` VALUES (1700001001002, '璞韵空间设计', '', '上海市浦东新区张江高科', '样板间打造, 软装搭配, 别墅整装', '021-66668888', '以“自然与质感”为核心，擅长高端私宅与样板房。', '2025-12-01 10:05:00');
INSERT INTO `gongsixinxi` VALUES (1700001001003, '匠心之家装饰', '', '广州市天河区珠江新城', '旧房翻新, 厨卫改造, 局部焕新', '020-77775555', '提供经济型改造方案，透明报价与快速交付。', '2025-12-01 10:10:00');
INSERT INTO `gongsixinxi` VALUES (1700001001004, '青禾艺筑', '', '杭州市滨江区物联网街区', '新中式定制, 原木整装, 软装美学', '0571-88990011', '融合东方美学与现代材料，打造沉浸式居住体验。', '2025-12-01 10:15:00');
INSERT INTO `gongsixinxi` VALUES (1700001001005, '北冥装饰工程', '', '深圳市南山区科技园', '办公空间规划, 展厅搭建, 商业照明', '0755-88001234', '专注企业空间形象升级，提供从规划到施工的一体化服务。', '2025-12-01 10:20:00');
INSERT INTO `gongsixinxi` VALUES (1700001001006, '晴川家居设计', '', '成都市高新区环球中心', '儿童房设计, 环保材料整装, 智能家居', '028-66007788', '强调环保与安全，适配多品牌智能家居生态。', '2025-12-01 10:25:00');
INSERT INTO `gongsixinxi` VALUES (1700001001007, '云庐建筑装饰', '', '重庆市渝中区解放碑', '景观小品, LOFT风, 工业风改造', '023-66998877', '擅长工业风与loft空间，提升空间层次与采光。', '2025-12-01 10:30:00');
INSERT INTO `gongsixinxi` VALUES (1700001001008, '拾光软装工作室', '', '南京市江宁区秣周东路', '软装配色, 家具选型, 窗帘地毯定制', '025-88116600', '以软装为核心，提供一站式搭配与现场落地。', '2025-12-01 10:35:00');

-- ----------------------------
-- Table structure for news
-- ----------------------------
DROP TABLE IF EXISTS `news`;
CREATE TABLE `news`  (
  `id` bigint NOT NULL,
  `title` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `introduction` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `picture` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `content` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL,
  `addtime` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of news
-- ----------------------------

-- ----------------------------
-- Table structure for orders
-- ----------------------------
DROP TABLE IF EXISTS `orders`;
CREATE TABLE `orders`  (
  `id` bigint NOT NULL,
  `orderid` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `tablename` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `userid` bigint NOT NULL,
  `goodid` bigint NOT NULL,
  `goodname` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `picture` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `buynumber` int NOT NULL,
  `price` decimal(10, 2) NOT NULL,
  `discountprice` decimal(10, 2) NULL DEFAULT NULL,
  `total` decimal(10, 2) NOT NULL,
  `discounttotal` decimal(10, 2) NULL DEFAULT NULL,
  `type` int NULL DEFAULT 0,
  `status` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '状态',
  `address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `tel` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `consignee` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `logistics` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `addtime` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `orderid`(`orderid` ASC) USING BTREE,
  INDEX `idx_orders_user`(`userid` ASC) USING BTREE,
  INDEX `idx_orders_good`(`goodid` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of orders
-- ----------------------------
INSERT INTO `orders` VALUES (1684652002001, '202305201001', 'zhuangxiushangpin', 101, 1001, '多乐士二代五合一墙面漆', '', 2, 358.00, 358.00, 716.00, 716.00, 1, '已支付', '北京市朝阳区朝阳北路101号', '13800138001', '张三', '请尽快发货，装修急用', '', '2023-05-20 10:05:00');
INSERT INTO `orders` VALUES (1684652002002, '202305201002', 'zhuangxiushangpin', 101, 1002, '欧普照明LED吸顶灯', '', 1, 199.00, 199.00, 199.00, 199.00, 1, '已发货', '北京市朝阳区朝阳北路101号', '13800138001', '张三', '', '顺丰速运 SF1234567890', '2023-05-20 14:20:00');
INSERT INTO `orders` VALUES (1684652002003, '202305211001', 'zhuangxiushangpin', 102, 1005, '圣象强化复合地板', '', 50, 128.00, 128.00, 6400.00, 6400.00, 1, '已完成', '上海市浦东新区张江高科路88号', '13900139002', '李四', '送货上楼', '京东物流 JD0987654321', '2023-05-21 09:15:00');
INSERT INTO `orders` VALUES (1684652002004, '202305221003', 'zhuangxiushangpin', 103, 1008, '科勒坐便器家用马桶', '', 1, 2599.00, 2599.00, 2599.00, 2599.00, 1, '已支付', '广州市天河区天河路200号', '13700137003', '王五', '', '', '2023-05-22 11:30:00');
INSERT INTO `orders` VALUES (1684652002005, '202305231005', 'zhuangxiushangpin', 102, 1010, '方太侧吸式抽油烟机', '', 1, 3299.00, 3299.00, 3299.00, 3299.00, 1, '已发货', '上海市浦东新区张江高科路88号', '13900139002', '李四', '周末配送', '中通快递 7788990011', '2023-05-23 16:45:00');

-- ----------------------------
-- Table structure for shangpinfenlei
-- ----------------------------
DROP TABLE IF EXISTS `shangpinfenlei`;
CREATE TABLE `shangpinfenlei`  (
  `id` bigint NOT NULL,
  `shangpinfenlei` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `addtime` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `shangpinfenlei`(`shangpinfenlei` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of shangpinfenlei
-- ----------------------------
INSERT INTO `shangpinfenlei` VALUES (1768500000001, '地板材料', '2026-01-15 11:35:10');
INSERT INTO `shangpinfenlei` VALUES (1768500000002, '墙面涂料', '2026-01-15 11:35:10');
INSERT INTO `shangpinfenlei` VALUES (1768500000003, '灯具照明', '2026-01-15 11:35:10');
INSERT INTO `shangpinfenlei` VALUES (1768500000004, '厨卫洁具', '2026-01-15 11:35:10');
INSERT INTO `shangpinfenlei` VALUES (1768500000005, '门窗配件', '2026-01-15 11:35:10');
INSERT INTO `shangpinfenlei` VALUES (1768500000006, '五金建材', '2026-01-15 11:35:10');
INSERT INTO `shangpinfenlei` VALUES (1768500000007, '软装饰品', '2026-01-15 11:35:10');
INSERT INTO `shangpinfenlei` VALUES (1768500000008, '防水材料', '2026-01-15 11:35:10');
INSERT INTO `shangpinfenlei` VALUES (1768600000009, '开关插座', '2026-01-15 14:14:58');
INSERT INTO `shangpinfenlei` VALUES (1768600000010, '辅材与胶类', '2026-01-15 14:14:58');

-- ----------------------------
-- Table structure for storeup
-- ----------------------------
DROP TABLE IF EXISTS `storeup`;
CREATE TABLE `storeup`  (
  `id` bigint NOT NULL,
  `userid` bigint NOT NULL,
  `refid` bigint NOT NULL,
  `tablename` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `picture` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `type` int NULL DEFAULT 0,
  `inteltype` int NULL DEFAULT 0,
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `addtime` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_storeup_user`(`userid` ASC) USING BTREE,
  INDEX `idx_storeup_ref`(`refid` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of storeup
-- ----------------------------

-- ----------------------------
-- Table structure for token
-- ----------------------------
DROP TABLE IF EXISTS `token`;
CREATE TABLE `token`  (
  `id` bigint NOT NULL,
  `userid` bigint NOT NULL,
  `username` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `tablename` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `role` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `token` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `expiratedtime` datetime NOT NULL,
  `addtime` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `token`(`token` ASC) USING BTREE,
  INDEX `idx_token_user`(`userid` ASC) USING BTREE,
  INDEX `idx_token_token`(`token` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of token
-- ----------------------------
INSERT INTO `token` VALUES (1768374567385, 1700000000001, 'admin', 'users', '管理员', 'kcidokiz4om3sl5gt30ux6tqb1ftozyz', '2026-01-15 16:52:41', '2026-01-14 15:09:27');

-- ----------------------------
-- Table structure for users
-- ----------------------------
DROP TABLE IF EXISTS `users`;
CREATE TABLE `users`  (
  `id` bigint NOT NULL,
  `username` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `password` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `role` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `mobile` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `addtime` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `username`(`username` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of users
-- ----------------------------
INSERT INTO `users` VALUES (1700000000001, 'admin', '123456', '管理员', '', '13800000000', '2026-01-14 14:58:20');
INSERT INTO `users` VALUES (1768442196774, 'xxx', '123456', '管理员', '', '17888888888', '2026-01-15 09:56:36');

-- ----------------------------
-- Table structure for yonghu
-- ----------------------------
DROP TABLE IF EXISTS `yonghu`;
CREATE TABLE `yonghu`  (
  `id` bigint NOT NULL,
  `yonghuming` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `mima` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `xingming` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `xingbie` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `shouji` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `touxiang` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `money` decimal(10, 2) NULL DEFAULT 0.00,
  `addtime` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_yonghu_yonghuming`(`yonghuming` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of yonghu
-- ----------------------------

-- ----------------------------
-- Table structure for yuangongxinxi
-- ----------------------------
DROP TABLE IF EXISTS `yuangongxinxi`;
CREATE TABLE `yuangongxinxi`  (
  `id` bigint NOT NULL,
  `gonghao` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `xingming` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `xingbie` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `touxiang` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `zhiwei` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `lianxifangshi` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `shenfenzhenghao` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `fuzexiangmu` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `addtime` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `gonghao`(`gonghao` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of yuangongxinxi
-- ----------------------------
INSERT INTO `yuangongxinxi` VALUES (1700001001001, 'EMP001', '张三', '男', '', '项目经理', '13800138001', '110101199001011234', '家装项目A', '2026-01-14 17:02:46');
INSERT INTO `yuangongxinxi` VALUES (1700001001002, 'EMP002', '李四', '女', '', '设计师', '13800138002', '110101199102021234', '工装项目B', '2026-01-14 17:02:46');
INSERT INTO `yuangongxinxi` VALUES (1700001001003, 'EMP003', '王五', '男', '', '施工主管', '13800138003', '110101199303031234', '别墅装修C', '2026-01-14 17:02:46');
INSERT INTO `yuangongxinxi` VALUES (1700001001004, 'EMP004', '赵六', '女', '', '预算员', '13800138004', '110101199404041234', '预算审核D', '2026-01-14 17:02:46');
INSERT INTO `yuangongxinxi` VALUES (1700001001005, 'EMP005', '钱七', '男', '', '材料采购', '13800138005', '110101199505051234', '材料供应E', '2026-01-14 17:02:46');
INSERT INTO `yuangongxinxi` VALUES (1700001001006, 'EMP006', '孙八', '女', '', '客服专员', '13800138006', '110101199606061234', '售后维护F', '2026-01-14 17:02:46');
INSERT INTO `yuangongxinxi` VALUES (1700001001007, 'EMP007', '周九', '男', '', '平面设计', '13800138007', '110101199707071234', '效果图设计G', '2026-01-14 17:02:46');
INSERT INTO `yuangongxinxi` VALUES (1700001001008, 'EMP008', '吴十', '女', '', '软装搭配', '13800138008', '110101199808081234', '软装方案H', '2026-01-14 17:02:46');

-- ----------------------------
-- Table structure for zhuangxiushangpin
-- ----------------------------
DROP TABLE IF EXISTS `zhuangxiushangpin`;
CREATE TABLE `zhuangxiushangpin`  (
  `id` bigint NOT NULL,
  `shangpinmingcheng` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `shangpinfenlei` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `shangpintupian` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `shangpinguige` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `shangpinpinpai` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `youhuihuodong` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `onelimittimes` int NULL DEFAULT 0,
  `alllimittimes` int NULL DEFAULT 0,
  `shangpinxiangqing` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL,
  `clicktime` datetime NULL DEFAULT NULL,
  `clicknum` int NULL DEFAULT 0,
  `price` decimal(10, 2) NOT NULL,
  `addtime` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_zxsp_fenlei`(`shangpinfenlei` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of zhuangxiushangpin
-- ----------------------------
INSERT INTO `zhuangxiushangpin` VALUES (1768458215988, '测试', '测试', '', '测试', '测试', '', 0, 0, '', NULL, 0, 4888.00, '2026-01-15 14:23:36');
INSERT INTO `zhuangxiushangpin` VALUES (1768700000001, '高级实木地板', '地板材料', '', '1200x200x18mm', '木之源', '满2000减200', 2, 5, '高密度实木，耐磨耐压，环保等级E0。', NULL, 0, 399.00, '2026-01-15 14:16:23');
INSERT INTO `zhuangxiushangpin` VALUES (1768700000002, '耐污内墙涂料', '墙面涂料', '', '20L', '彩涂坊', '买二送一', 3, 6, '抗霉耐污，低气味快干，适合室内墙面。', NULL, 0, 189.90, '2026-01-15 14:16:23');
INSERT INTO `zhuangxiushangpin` VALUES (1768700000003, '现代客厅吊灯', '灯具照明', '', '120W LED', '光语', '新品九折', 1, 3, '无极调光，暖白冷白切换，低蓝光护眼。', NULL, 0, 599.00, '2026-01-15 14:16:23');
INSERT INTO `zhuangxiushangpin` VALUES (1768700000004, '不锈钢淋浴花洒', '厨卫洁具', '', '套装', '洁瑞', '满500减50', 2, 4, '304不锈钢主体，三档出水，一键除垢。', NULL, 0, 329.00, '2026-01-15 14:16:23');
INSERT INTO `zhuangxiushangpin` VALUES (1768700000005, '断桥铝合金窗', '门窗配件', '', '1.2m x 1.5m', '宏窗', '安装包邮', 1, 2, '多腔体结构，隔音保温佳，安全五金。', NULL, 0, 1280.00, '2026-01-15 14:16:23');
INSERT INTO `zhuangxiushangpin` VALUES (1768700000006, '多功能五金套装', '五金建材', '', '25件套', '工匠屋', '第二件半价', 2, 5, '家装常用工具套件，镀铬耐腐蚀。', NULL, 0, 259.00, '2026-01-15 14:16:23');
INSERT INTO `zhuangxiushangpin` VALUES (1768700000007, '北欧风格窗帘', '软装饰品', '', '2.0m x 2.7m', '布语', '满300赠绑带', 3, 6, '遮光透气，简约纹理，支持定制尺寸。', NULL, 0, 1998.00, '2026-01-15 14:16:23');
INSERT INTO `zhuangxiushangpin` VALUES (1768700000008, '聚合物防水涂料', '防水材料', '', '18kg', '固盾', '满3桶减100', 2, 4, '柔韧型配方，粘结强，适用于厨卫阳台。', NULL, 0, 238.00, '2026-01-15 14:16:23');
INSERT INTO `zhuangxiushangpin` VALUES (1768700000009, '安全墙壁插座', '开关插座', '', '五孔', '安联电工', '满100减20', 5, 10, '阻燃PC面板，磷青铜片，童锁设计。', NULL, 0, 29.90, '2026-01-15 14:16:23');
INSERT INTO `zhuangxiushangpin` VALUES (1768700000010, '瓷砖粘结胶', '辅材与胶类', '', '5kg', '固力', '加1元多1kg', 3, 6, '初粘力强，耐水耐候，适合室内外。', NULL, 0, 45.00, '2026-01-15 14:16:23');

-- ----------------------------
-- Table structure for zhuangxiuzuopin
-- ----------------------------
DROP TABLE IF EXISTS `zhuangxiuzuopin`;
CREATE TABLE `zhuangxiuzuopin`  (
  `id` bigint NOT NULL,
  `zuopinmingcheng` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `fenggeleixing` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `fengmianxinxi` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `fanganjieshao` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `neirongxiangqing` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL,
  `faburiqi` datetime NULL DEFAULT NULL,
  `addtime` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of zhuangxiuzuopin
-- ----------------------------
INSERT INTO `zhuangxiuzuopin` VALUES (1684652001001, '现代简约·阳光海岸三居室', '现代简约', '', '本方案主打黑白灰极简色调，利用大面积落地窗引入自然光，打造通透舒适的居住空间。', '<p>客厅采用无主灯设计，通过磁吸轨道灯营造层次感；卧室采用温馨的木纹地板...</p>', '2023-05-20 00:00:00', '2023-05-20 10:00:00');
INSERT INTO `zhuangxiuzuopin` VALUES (1684652001002, '欧式奢华·皇家庭院别墅', '欧式风格', '', '汲取古典欧式建筑元素，运用大理石罗马柱与水晶吊灯，展现尊贵典雅的生活品质。', '<p>挑高客厅设计，搭配精致的石膏线条；餐厅设置长桌，适合家庭聚会...</p>', '2023-06-15 00:00:00', '2023-06-15 14:30:00');
INSERT INTO `zhuangxiuzuopin` VALUES (1684652001003, '新中式·雅致书香门第', '新中式', '', '将传统中式元素与现代材质巧妙兼柔，删繁就简，绘出富有禅意的东方美学。', '<p>书房采用原木色书柜，搭配博古架；客厅背景墙采用山水水墨画硬包...</p>', '2023-07-08 00:00:00', '2023-07-08 09:15:00');
INSERT INTO `zhuangxiuzuopin` VALUES (1684652001004, '北欧风情·清新小户型', '北欧风格', '', '以浅色调为主，搭配原木家具与绿植点缀，营造自然、舒适、年轻化的生活氛围。', '<p>开放式厨房设计增加互动性；阳台改造为休闲区，放置懒人沙发...</p>', '2023-08-22 00:00:00', '2023-08-22 16:45:00');
INSERT INTO `zhuangxiuzuopin` VALUES (1684652001005, '工业风·LOFT创意空间', '工业风格', '', '裸露的红砖墙与水泥顶面，搭配黑色金属铁艺，展现个性张扬的艺术气息。', '<p>二层设置为卧室私密空间，一层为开放式办公与娱乐区域，楼梯下方做储物设计...</p>', '2023-09-10 00:00:00', '2023-09-10 11:20:00');

SET FOREIGN_KEY_CHECKS = 1;
