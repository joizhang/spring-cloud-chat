package com.joizhang.chat.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.joizhang.chat.admin.api.entity.SysFile;
import com.joizhang.chat.common.core.util.R;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * 文件管理
 */
public interface SysFileService extends IService<SysFile> {

    /**
     * 上传文件
     *
     * @param file
     * @return
     */
    R<Map<String, String>> uploadFile(MultipartFile file);

    /**
     * 读取文件
     *
     * @param bucket   桶名称
     * @param fileName 文件名称
     * @param response 输出流
     */
    void getFile(String bucket, String fileName, HttpServletResponse response);

    /**
     * 删除文件
     *
     * @param id
     * @return
     */
    Boolean deleteFile(Long id);

    /**
     * 获取外网访问地址
     *
     * @param bucket
     * @param fileName
     * @return
     */
    String onlineFile(String bucket, String fileName);

}
