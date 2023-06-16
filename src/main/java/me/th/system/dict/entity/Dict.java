package me.th.system.dict.entity;

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
@Table(name = "sys_dict")
public class Dict extends BaseEntity {

    private static final long serialVersionUID = -1L;

    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 父级ID
     */
    private Long pid;

    /**
     * 字典名称
     */
    private String name;

    /**
     * 字典键
     */
    private String key;

    /**
     * 字典值
     */
    private String value;

    /**
     * 描述
     */
    private String description;

    /**
     * 排序
     */
    private Long sort;

    /**
     * 是否隐藏，默认为 false
     */
    private Boolean hidden = false;

    /**
     * 是否可删除，默认为 true
     */
    private Boolean deletable = true;

    /**
     * 使用次数，默认为 0L
     */
    private Long useCount = 0L;

    /**
     * 隐藏的同时设置不可删除
     */
    public void hiddenAndNotDeletable() {
        this.setDeletable(false);
        this.setHidden(true);
    }
}
