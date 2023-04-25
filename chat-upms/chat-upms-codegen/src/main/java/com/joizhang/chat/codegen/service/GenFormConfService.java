package com.joizhang.chat.codegen.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.joizhang.chat.codegen.entity.GenFormConf;

/**
 * 表单管理
 *
 * @author lengleng
 *  @since 2019-08-12 15:55:35
 */
public interface GenFormConfService extends IService<GenFormConf> {

    /**
     * 获取表单信息
     *
     * @param dsName    数据源ID
     * @param tableName 表名称
     * @return
     */
    String getForm(String dsName, String tableName);

}
