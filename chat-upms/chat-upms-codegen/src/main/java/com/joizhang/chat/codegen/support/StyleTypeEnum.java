package com.joizhang.chat.codegen.support;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

/**
 * 代码生成风格
 *
 * @author lengleng
 * @since 2021/7/31
 */
@Getter
@AllArgsConstructor
public enum StyleTypeEnum {

    /**
     * 前端类型-avue 风格
     */
    AVUE("0", "avue"),

    /**
     * 前端类型-element 风格
     */
    ELEMENT("1", "element");

    /**
     * 类型
     */
    private String style;

    /**
     * 描述
     */
    private String description;

    public static String getDecs(String style) {
        return Arrays.stream(StyleTypeEnum.values())
                .filter(styleTypeEnum -> styleTypeEnum.getStyle().equals(style))
                .findFirst()
                .orElse(ELEMENT)
                .getDescription();
    }

}
