create table `th-admin`.sys_role
(
    id           bigint auto_increment comment '主键',
    name         varchar(255) not null comment '名称',
    description  varchar(255) null comment '描述',
    created_by   varchar(255) null comment '创建者',
    updated_by   varchar(255) null comment '更新者',
    created_time datetime     null comment '创建时间',
    updated_time datetime     null comment '更新时间',
    constraint sys_role_pk
        primary key (id)
)
    comment '系统角色表';
