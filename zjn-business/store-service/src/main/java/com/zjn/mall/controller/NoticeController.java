package com.zjn.mall.controller;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zjn.mall.domain.Notice;
import com.zjn.mall.model.Result;
import com.zjn.mall.service.NoticeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author 张健宁
 * @ClassName NoticeController
 * @Description 公告管理控制层
 * @createTime 2024年09月11日 17:22:00
 */
@Api("公告管理控制层")
@RestController
@RequestMapping("shop/notice")
@RequiredArgsConstructor
public class NoticeController {

    private final NoticeService noticeService;

    @ApiOperation("多条件分页查询公告信息")
    @GetMapping("page")
    @PreAuthorize("hasAnyAuthority('shop:notice:page')")
    public Result<Page<Notice>> loadShopNoticePage(@RequestParam Long current,
                                             @RequestParam Long size,
                                             @RequestParam(required = false) String title,
                                             @RequestParam(required = false) Integer status,
                                             @RequestParam(required = false) Integer isTop) {
        Page<Notice> noticePage = new Page<>(current, size);
        noticePage = noticeService.page(noticePage,
                new LambdaQueryWrapper<Notice>()
                        .eq(ObjectUtil.isNotNull(status), Notice::getStatus, status)
                        .eq(ObjectUtil.isNotNull(isTop), Notice::getIsTop, isTop)
                        .like(StringUtils.hasText(title), Notice::getTitle, title)
                        .orderByDesc(Notice::getCreateTime)
                );

        return Result.success(noticePage);
    }

    @ApiOperation("新增公告信息")
    @PostMapping
    @PreAuthorize("hasAnyAuthority('shop:notice:save')")
    public Result<String> saveShopNotice(@RequestBody Notice notice) {
        Boolean save = noticeService.saveShopNotice(notice);
        return Result.handle(save);
    }

    @ApiOperation("根据id查询公告信息")
    @GetMapping("info/{id}")
    @PreAuthorize("hasAnyAuthority('shop:notice:info')")
    public Result<Notice> loadShopNoticeById(@PathVariable Long id) {
        Notice notice = noticeService.getById(id);
        return Result.success(notice);
    }

    @ApiOperation("修改公告信息")
    @PutMapping
    @PreAuthorize("hasAnyAuthority('shop:notice:update')")
    public Result<String> modifyShopNotice(@RequestBody Notice notice) {
        Boolean modify = noticeService.modifyShopNotice(notice);
        return Result.handle(modify);
    }

    @ApiOperation("删除公告信息")
    @DeleteMapping("{id}")
    @PreAuthorize("hasAnyAuthority('shop:notice:delete')")
    public Result<String> removeShopNotice(@PathVariable Long id) {
        boolean remove = noticeService.removeById(id);
        return Result.handle(remove);
    }

    @ApiOperation("小程序查询置顶的公告信息")
    @GetMapping("topNoticeList")
    public Result<List<Notice>> loadWxTopNoticeList() {
        List<Notice> noticeList = noticeService.queryWxTopNoticeList();
        return Result.success(noticeList);
    }
}
