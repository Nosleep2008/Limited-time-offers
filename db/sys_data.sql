/*
 *  mysql-v: 5.7.22
 */

-- create database
-- CREATE DATABASE seckill DEFAULT CHARACTER SET utf8;


-- 添加秒杀商品表数据
INSERT INTO seckill (title, image, price, cost_price, start_time, end_time, stock_count) VALUES('Apple iPhone 7 Plus 4G 5.5 inch 64GB', 'https://g-search3.alicdn.com/img/bao/uploaded/i4/i3/2249262840/O1CN011WqlHkrSuPEiHxd_!!2249262840.jpg_230x230.jpg', 660.00, 350.00, '2020-11-12 16:30:00', '2020-11-12 18:30:00', 10);
INSERT INTO seckill (title, image, price, cost_price, start_time, end_time, stock_count) VALUES('ins new brand Down jacket Korean style', 'https://gw.alicdn.com/bao/uploaded/i3/2007932029/TB1vdlyaVzqK1RjSZFzXXXjrpXa_!!0-item_pic.jpg_180x180xz.jpg', 100.00, 50.00, '2020-11-12 16:30:00', '2020-11-12 18:30:00', 10);
INSERT INTO seckill (title, image, price, cost_price, start_time, end_time, stock_count) VALUES('Cute rabbit plush toy lop-eared rabbit doll', 'https://g-search3.alicdn.com/img/bao/uploaded/i4/i2/3828650009/TB22CvKkeOSBuNjy0FdXXbDnVXa_!!3828650009.jpg_230x230.jpg', 50.00, 20.00, '2020-11-12 16:30:00', '2020-11-12 18:30:00', 20);
