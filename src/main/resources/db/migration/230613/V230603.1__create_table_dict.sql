create table `th-admin`.sys_dict
(
    id           bigint auto_increment comment '主键',
    pid          bigint       not null comment '父级ID',
    name         varchar(255) null comment '字典名称',
    `key`        varchar(255) null comment '字典键',
    value        varchar(255) null comment '字典值',
    description  varchar(255) null comment '描述',
    sort         bigint       null comment '排序',
    hidden       bit          null comment '是否隐藏',
    deletable    bit          null comment '是否可删除',
    use_count    bigint       null comment '使用次数',
    created_by   varchar(255) null comment '创建者',
    updated_by   varchar(255) null comment '更新者',
    created_time datetime     null comment '创建时间',
    updated_time datetime     null comment '更新时间',
    constraint sys_dict_pk
        primary key (id)
)
    comment '系统字典表';

-- 添加索引
create index idx_key_value
    on `th-admin`.sys_dict (`key`, value);