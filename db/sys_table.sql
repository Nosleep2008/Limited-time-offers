DROP TABLE IF EXISTS `seckill`;
DROP TABLE IF EXISTS `seckill_order`;

CREATE TABLE `seckill`(
  `seckill_id` bigint NOT NULL AUTO_INCREMENT COMMENT ' items ID',
  `title` varchar (1000) DEFAULT NULL COMMENT 'title',
  `image` varchar (1000) DEFAULT NULL COMMENT 'picture',
  `price` decimal (10,2) DEFAULT NULL COMMENT 'old price',
  `cost_price` decimal (10,2) DEFAULT NULL COMMENT 'limited offer price',
  `stock_count` bigint DEFAULT NULL COMMENT 'quantity',
  `start_time` timestamp NOT NULL DEFAULT '1970-02-01 00:00:01' COMMENT 'offer start time',
  `end_time` timestamp NOT NULL DEFAULT '1970-02-01 00:00:01' COMMENT 'offer end time',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'create time',
  PRIMARY KEY (`seckill_id`),
  KEY `idx_start_time` (`start_time`),
  KEY `idx_end_time` (`end_time`),
  KEY `idx_create_time` (`end_time`)
) CHARSET=utf8 ENGINE=InnoDB COMMENT 'limited offer items table';

-- create offer items table
CREATE TABLE `seckill_order`(
  `seckill_id` bigint NOT NULL COMMENT 'offer items ID',
  `money` decimal (10, 2) DEFAULT NULL COMMENT 'payment amount',
  `user_phone` bigint NOT NULL COMMENT 'user phone number',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'create time',
  `state` tinyint NOT NULL DEFAULT -1 COMMENT 'status : -1 invalid 0 Success 1 Paid',
  PRIMARY KEY (`seckill_id`, `user_phone`) /*united primary keys , make sure only one offer item per user */
) CHARSET=utf8 ENGINE=InnoDB COMMENT 'limited offer orders table';
