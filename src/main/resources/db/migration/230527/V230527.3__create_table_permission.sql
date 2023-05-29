create table `th-admin`.sys_permission
(
    id              bigint auto_increment comment '主键',
    pid             bigint       null comment '上级权限ID',
    type            int          null comment '权限菜单类型',
    title           varchar(255) null comment '权限菜单标题',
    name            varchar(255) null comment '权限菜单名称',
    component       varchar(255) null comment '前端组件',
    sort            int          null comment '排序',
    icon            varchar(255) null comment '图标',
    path            varchar(255) null comment '链接地址',
    hidden          bit          null comment '是否隐藏',
    permission_code varchar(255) null comment '权限码',
    created_by      varchar(255) null comment '创建者',
    updated_by      varchar(255) null comment '更新者',
    created_time    datetime     null comment '创建时间',
    updated_time    datetime     null comment '更新时间',
    constraint sys_permission_pk
        primary key (id)
)
    comment '系统权限表';

