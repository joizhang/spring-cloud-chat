package com.joizhang.chat.common.xss.core;

/**
 * xss 数据处理类型
 */
public enum XssType {

    /**
     * 表单
     */
    FORM() {
        @Override
        public RuntimeException getXssException(String input, String message) {
            return new FromXssException(input, message);
        }
    },

    /**
     * body json
     */
    JACKSON() {
        @Override
        public RuntimeException getXssException(String input, String message) {
            return new RuntimeException(message);
        }
    };

    /**
     * 获取 xss 异常
     *
     * @param input   input
     * @param message message
     * @return XssException
     */
    public abstract RuntimeException getXssException(String input, String message);

}
