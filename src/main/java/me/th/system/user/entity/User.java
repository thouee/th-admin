package me.th.system.user.entity;

import lombok.Getter;
import lombok.Setter;
import me.th.share.base.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;

@Entity
@Getter
@Setter
@Table(name = "sys_user")
public class User extends BaseEntity {

    private static final long serialVersionUID = -1L;

    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 昵称
     */
    private String nickName;

    /**
     * 密码
     */
    private String password;

    /**
     * 性别：1-男 0-女
     */
    private Integer gender;

    /**
     * 手机号码
     */
    private String phone;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 头像地址
     */
    private String avatarName;

    /**
     * 头像真实路径
     */
    private String avatarPath;

    /**
     * 是否为admin账号
     */
    private Boolean isAdmin = false;

    /**
     * 是否启用
     */
    private Boolean enabled = true;

    /**
     * 密码修改时间
     */
    private Timestamp pwdResetTime;
}
