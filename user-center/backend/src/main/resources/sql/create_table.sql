-- 用户中心 · 数据库建表脚本
-- 对应网页书:实战篇/数据库设计、技术图鉴/MySQL
-- 设计三原则:敏感字段留足长度、逻辑删除不真删、每表带时间戳

CREATE DATABASE IF NOT EXISTS user_center
  CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE user_center;

CREATE TABLE IF NOT EXISTS user (
  -- 身份
  id           BIGINT       NOT NULL AUTO_INCREMENT COMMENT '用户ID(主键)',
  userAccount  VARCHAR(256)                          COMMENT '登录账号',
  userPassword VARCHAR(512) NOT NULL                 COMMENT '密码(BCrypt 密文)',
  -- 个人信息
  username     VARCHAR(256)                          COMMENT '昵称',
  avatarUrl    VARCHAR(1024)                         COMMENT '头像链接',
  gender       TINYINT                               COMMENT '性别:0 女 / 1 男',
  phone        VARCHAR(128)                          COMMENT '电话',
  email        VARCHAR(256)                          COMMENT '邮箱',
  -- 业务
  planetCode   VARCHAR(512)                          COMMENT '星球编号(身份标识)',
  userRole     TINYINT      NOT NULL DEFAULT 0       COMMENT '角色:0 普通用户 / 1 管理员',
  userStatus   INT          NOT NULL DEFAULT 0       COMMENT '状态:0 正常',
  -- 系统
  createTime   DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  updateTime   DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  isDelete     TINYINT      NOT NULL DEFAULT 0       COMMENT '逻辑删除:0 有效 / 1 已删',
  PRIMARY KEY (id),
  UNIQUE KEY uk_user_account (userAccount),
  UNIQUE KEY uk_planet_code (planetCode)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- ─────────────────────────────────────────────
-- 演示账号(仅本地/演示环境,生产请删除!)
-- root / 12345678  → 管理员
-- demo / 12345678  → 普通用户
-- 密码为 BCrypt 密文;INSERT IGNORE 保证脚本可重复执行
-- ─────────────────────────────────────────────
INSERT IGNORE INTO user (userAccount, userPassword, username, planetCode, userRole, userStatus)
VALUES
  ('root', '$2a$10$Wvj4b9mod8tAGKy.lbBCPeMp.zFg3TDf9V9.XK2iqYqdIethBtfzy', 'root', '1', 1, 0),
  ('demo', '$2a$10$qMQs.g604m2sIP3q0IEHeuiyZbzUPX181DTZN/D/Qt5eD6yEyl2eu', 'demo', '2', 0, 0);
