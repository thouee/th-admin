-- username 添加唯一索引
ALTER TABLE `sys_user` ADD UNIQUE (`username`);
-- phone 添加唯一索引
ALTER TABLE `sys_user` ADD UNIQUE (`phone`);
-- email 添加唯一索引
ALTER TABLE `sys_user` ADD UNIQUE (`email`);