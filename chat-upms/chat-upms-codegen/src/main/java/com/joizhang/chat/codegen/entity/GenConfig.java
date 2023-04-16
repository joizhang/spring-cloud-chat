package com.joizhang.chat.codegen.entity;

import lombok.Data;

/**
 * 生成配置
 */
@Data
public class GenConfig {

    /**
     * 数据源name
     */
    private String dsName;

    /**
     * 包名
     */
    private String packageName;

    /**
     * 作者
     */
    private String author;

    /**
     * 模块名称
     */
    private String moduleName;

    /**
     * 表前缀
     */
    private String tablePrefix;

    /**
     * 表名称
     */
    private String tableName;

    /**
     * 表备注
     */
    private String comments;

    /**
     * 代码风格 0 - avue 1 - element
     */
    private String style;

}
