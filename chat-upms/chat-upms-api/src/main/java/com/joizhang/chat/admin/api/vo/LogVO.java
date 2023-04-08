package com.joizhang.chat.admin.api.vo;

import com.joizhang.chat.admin.api.entity.SysLog;
import lombok.Data;

import java.io.Serializable;

/**
 * 日志
 */
@Data
public class LogVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private SysLog sysLog;

    private String username;

}
