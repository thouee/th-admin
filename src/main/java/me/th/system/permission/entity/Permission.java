package me.th.system.permission.entity;

import lombok.Getter;
import lombok.Setter;
import me.th.share.base.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Getter
@Setter
@Table(name = "sys_permission")
public class Permission extends BaseEntity {

    private static final long serialVersionUID = -1L;

    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 上级权限ID
     */
    private Long pid;

    /**
     * 权限菜单类型
     */
    private Integer type;

    /**
     * 权限菜单标题
     */
    private String title;

    /**
     * 权限菜单名称
     */
    private String name;

    /**
     * 前端组件
     */
    private String component;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 图标
     */
    private String icon;

    /**
     * 链接地址
     */
    private String path;

    /**
     * 是否隐藏
     */
    private Boolean hidden = false;

    /**
     * 权限码
     */
    private String permissionCode;
}
