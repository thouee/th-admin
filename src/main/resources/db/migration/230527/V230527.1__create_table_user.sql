create table `th-admin`.sys_user
(
    id             bigint auto_increment comment '主键',
    username       varchar(255) not null comment '用户名',
    nick_name      varchar(255) not null comment '昵称',
    password       varchar(255) not null comment '密码',
    gender         tinyint      null comment '性别',
    phone          varchar(255) null comment '手机号码',
    email          varchar(255) null comment '邮箱',
    avatar_name    varchar(255) null comment '头像地址',
    avatar_path    varchar(255) null comment '头像真实路径',
    is_admin       bit          null comment '是否为admin账号',
    enabled        bit          null comment '状态',
    pwd_reset_time datetime     null comment '密码修改时间',
    created_by     varchar(255) null comment '创建者',
    updated_by     varchar(255) null comment '更新者',
    created_time   datetime     null comment '创建时间',
    updated_time   datetime     null comment '更新时间',
    constraint sys_user_pk
        primary key (id)
)
    comment '系统用户表';