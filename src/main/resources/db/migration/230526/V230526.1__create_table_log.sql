create table `th-admin`.sys_log
(
    id               bigint       null comment '主键',
    description      varchar(255) null comment '描述',
    log_type         varchar(255) null comment '日志类型',
    method           varchar(255) null comment '请求方法',
    params           text         null comment '请求参数',
    url              varchar(255) null comment '请求路径',
    request_ip       varchar(255) null comment '请求IP',
    cost_time        bigint       null comment '请求耗时',
    result           text         null comment '请求结果',
    username         varchar(255) null comment '用户',
    address          varchar(255) null comment '地址',
    browser          varchar(255) null comment '浏览器信息',
    create_time      datetime     null comment '创建时间'
)
    comment '系统日志';