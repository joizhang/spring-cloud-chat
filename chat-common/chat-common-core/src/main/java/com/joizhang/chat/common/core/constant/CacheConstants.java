package com.joizhang.chat.common.core.constant;

/**
 * 缓存的 key 常量
 *
 * @author lengleng
 * @author joizhang
 */
public interface CacheConstants {

    /**
     * oauth 缓存前缀
     */
    String PROJECT_OAUTH_ACCESS = "token::access_token";

    /**
     * oauth 缓存令牌前缀
     */
    String PROJECT_OAUTH_TOKEN = "chat_oauth:token:";

    /**
     * 验证码前缀
     */
    String DEFAULT_CODE_KEY = "DEFAULT_CODE_KEY:";

    /**
     * 菜单信息缓存
     */
    String MENU_DETAILS = "menu_details";

    /**
     * 用户信息缓存
     */
    String USER_DETAILS = "user_details";

    /**
     * 字典信息缓存
     */
    String DICT_DETAILS = "dict_details";

    /**
     * oauth 客户端信息
     */
    String CLIENT_DETAILS_KEY = "client:details";

    /**
     * 参数缓存
     */
    String PARAMS_DETAILS = "params_details";

}
