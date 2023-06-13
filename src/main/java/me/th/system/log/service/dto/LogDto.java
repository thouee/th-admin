package me.th.system.log.service.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.sql.Timestamp;

@Getter
@Setter
@ApiModel(value = "日志传输类")
public class LogDto implements Serializable {

    private static final long serialVersionUID = -1690536325254811476L;

    @ApiModelProperty(value = "ID")
    private Long id;

    @ApiModelProperty(value = "描述")
    private String description;

    @ApiModelProperty(value = "请求类型")
    private String logType;

    @ApiModelProperty(value = "请求方法")
    private String method;

    @ApiModelProperty(value = "请求参数")
    private String params;

    @ApiModelProperty(value = "请求路径")
    private String url;

    @ApiModelProperty(value = "请求IP")
    private String requestIp;

    @ApiModelProperty(value = "请求耗时")
    private Long costTime;

    @ApiModelProperty(value = "用户")
    private String username;

    @ApiModelProperty(value = "地址")
    private String address;

    @ApiModelProperty(value = "浏览器信息")
    private String browser;

    @ApiModelProperty(value = "请求结果")
    private byte[] result;

    @ApiModelProperty(value = "记录时间")
    private Timestamp createTime;
}
