package com.joizhang.chat.codegen.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.joizhang.chat.codegen.entity.GenDatasourceConf;
import com.joizhang.chat.codegen.service.GenDatasourceConfService;
import com.joizhang.chat.common.core.util.R;
import com.joizhang.chat.common.log.annotation.RecordSysLog;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 数据源管理
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/dsconf")
@Tag(name = "数据源管理模块")
@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
public class GenDsConfController {

    private final GenDatasourceConfService datasourceConfService;

    /**
     * 分页查询
     *
     * @param page           分页对象
     * @param datasourceConf 数据源表
     * @return
     */
    @GetMapping("/page")
    public R<IPage<GenDatasourceConf>> getSysDatasourceConfPage(Page page, GenDatasourceConf datasourceConf) {
        return R.ok(datasourceConfService.page(page, Wrappers.query(datasourceConf)));
    }

    /**
     * 查询全部数据源
     *
     * @return
     */
    @GetMapping("/list")
    public R<List<GenDatasourceConf>> list() {
        return R.ok(datasourceConfService.list());
    }

    /**
     * 通过id查询数据源表
     *
     * @param id id
     * @return R
     */
    @GetMapping("/{id}")
    public R<GenDatasourceConf> getById(@PathVariable("id") Integer id) {
        return R.ok(datasourceConfService.getById(id));
    }

    /**
     * 新增数据源表
     *
     * @param datasourceConf 数据源表
     * @return R
     */
    @RecordSysLog("新增数据源表")
    @PostMapping
    public R<Boolean> save(@RequestBody GenDatasourceConf datasourceConf) {
        return R.ok(datasourceConfService.saveDsByEnc(datasourceConf));
    }

    /**
     * 修改数据源表
     *
     * @param conf 数据源表
     * @return R
     */
    @RecordSysLog("修改数据源表")
    @PutMapping
    public R<Boolean> updateById(@RequestBody GenDatasourceConf conf) {
        return R.ok(datasourceConfService.updateDsByEnc(conf));
    }

    /**
     * 通过id删除数据源表
     *
     * @param id id
     * @return R
     */
    @RecordSysLog("删除数据源表")
    @DeleteMapping("/{id}")
    public R<Boolean> removeById(@PathVariable Long id) {
        return R.ok(datasourceConfService.removeByDsId(id));
    }

}
