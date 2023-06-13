package me.th.system.log.rest;

import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import me.th.share.common.PageData;
import me.th.share.common.R;
import me.th.share.util.SecurityUtils;
import me.th.system.log.annotation.Logging;
import me.th.system.log.service.LogService;
import me.th.system.log.service.dto.LogDto;
import me.th.system.log.service.query.LogQueryCriteria;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "系统：日志模块")
@ApiSupport(author = "thou")
@RestController
@RequestMapping("/log")
@RequiredArgsConstructor
public class LogController {

    private final LogService logService;

    @ApiOperation(value = "日志查询")
    @GetMapping("/page")
    public R<PageData<LogDto>> queryLog(@RequestBody LogQueryCriteria criteria) {
        PageData<LogDto> pageData = logService.queryAll(criteria);
        return R.ok(pageData);
    }

    @ApiOperation(value = "用户日志查询")
    @GetMapping("/page/user")
    public R<PageData<LogDto>> queryUserLog(@RequestBody LogQueryCriteria criteria) {
        criteria.setUsername(SecurityUtils.getCurrentUsername());
        PageData<LogDto> pageData = logService.queryAll(criteria);
        return R.ok(pageData);
    }

    @Logging("日志删除")
    @ApiOperation(value = "日志删除")
    @DeleteMapping("/delete/{id}")
    public R<Void> deleteLog(@PathVariable Long id) {
        logService.delete(id);
        return R.ok();
    }

    @Logging("删除全部INFO日志")
    @ApiOperation(value = "删除全部INFO日志")
    @DeleteMapping("/delete/info")
    public R<Void> deleteAllInfoLog() {
        logService.deleteAllInfo();
        return R.ok();
    }

    @Logging("删除全部ERROR日志")
    @ApiOperation(value = "删除全部ERROR日志")
    @DeleteMapping("/delete/error")
    public R<Void> deleteAllErrorLog() {
        logService.deleteAllError();
        return R.ok();
    }
}
