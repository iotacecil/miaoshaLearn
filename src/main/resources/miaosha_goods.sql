# Host: 10.1.18.133  (Version 5.6.41)
# Date: 2018-10-21 20:17:42
# Generator: MySQL-Front 6.0  (Build 2.20)


#
# Structure for table "miaosha_goods"
#

DROP TABLE IF EXISTS `miaosha_goods`;
CREATE TABLE `miaosha_goods` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '商品ID',
  `goods_name` varchar(16) NOT NULL COMMENT '商品名称',
  `goods_title` varchar(64) DEFAULT NULL COMMENT '商品标题',
  `goods_img` varchar(64) DEFAULT NULL COMMENT '商品图片',
  `goods_detail` longtext COMMENT '商品详情介绍',
  `goods_price` decimal(10,2) DEFAULT '0.00' COMMENT '商品单价',
  `goods_stock` int(11) DEFAULT '0' COMMENT '商品库存，-1表示没有限制',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

#
# Data for table "miaosha_goods"
#

INSERT INTO `miaosha_goods` VALUES (1,'iphone','iphonex 64G','/img/iphonex.png','ipx',5000.00,50);
