package me.th.system.log.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 系统日志
 */
@Entity
@Getter
@Setter
@Table(name = "sys_log")
public class Log implements Serializable {

    private static final long serialVersionUID = -1L;

    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 描述
     */
    private String description;

    /**
     * 请求类型
     */
    private String logType;

    /**
     * 请求方法
     */
    private String method;

    /**
     * 请求参数
     */
    private String params;

    /**
     * 请求路径
     */
    private String url;

    /**
     * 请求IP
     */
    private String requestIp;

    /**
     * 请求耗时
     */
    private Long costTime;

    /**
     * 用户
     */
    private String username;

    /**
     * 地址
     */
    private String address;

    /**
     * 浏览器信息
     */
    private String browser;

    /**
     * 请求结果
     */
    private byte[] result;

    /**
     * 创建时间
     */
    @CreationTimestamp
    private Timestamp createTime;

    public Log() {
    }
}
