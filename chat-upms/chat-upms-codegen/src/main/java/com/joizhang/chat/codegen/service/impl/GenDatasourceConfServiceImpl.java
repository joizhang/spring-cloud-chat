package com.joizhang.chat.codegen.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.dynamic.datasource.DynamicRoutingDataSource;
import com.baomidou.dynamic.datasource.creator.DataSourceCreator;
import com.baomidou.dynamic.datasource.spring.boot.autoconfigure.DataSourceProperty;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.joizhang.chat.codegen.entity.GenDatasourceConf;
import com.joizhang.chat.codegen.mapper.GenDatasourceConfMapper;
import com.joizhang.chat.codegen.service.GenDatasourceConfService;
import com.joizhang.chat.common.core.util.SpringContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jasypt.encryption.StringEncryptor;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * 数据源表
 *
 * @author lengleng
 * @since 2019-03-31 16:00:20
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class GenDatasourceConfServiceImpl extends ServiceImpl<GenDatasourceConfMapper, GenDatasourceConf>
        implements GenDatasourceConfService {

    private final StringEncryptor stringEncryptor;

    private final DataSourceCreator hikariDataSourceCreator;

    /**
     * 保存数据源并且加密
     *
     * @param conf
     * @return
     */
    @Override
    public Boolean saveDsByEnc(GenDatasourceConf conf) {
        // 校验配置合法性
        if (!checkDataSource(conf)) {
            return Boolean.FALSE;
        }

        // 添加动态数据源
        addDynamicDataSource(conf);

        // 更新数据库配置
        conf.setPassword(stringEncryptor.encrypt(conf.getPassword()));
        this.baseMapper.insert(conf);
        return Boolean.TRUE;
    }

    /**
     * 更新数据源
     *
     * @param conf 数据源信息
     * @return
     */
    @Override
    public Boolean updateDsByEnc(GenDatasourceConf conf) {
        if (!checkDataSource(conf)) {
            return Boolean.FALSE;
        }
        // 先移除
        SpringContextHolder.getBean(DynamicRoutingDataSource.class)
                .removeDataSource(baseMapper.selectById(conf.getId()).getName());

        // 再添加
        addDynamicDataSource(conf);

        // 更新数据库配置
        if (StrUtil.isNotBlank(conf.getPassword())) {
            conf.setPassword(stringEncryptor.encrypt(conf.getPassword()));
        }
        this.baseMapper.updateById(conf);
        return Boolean.TRUE;
    }

    /**
     * 通过数据源名称删除
     *
     * @param dsId 数据源ID
     * @return
     */
    @Override
    public Boolean removeByDsId(Long dsId) {
        SpringContextHolder.getBean(DynamicRoutingDataSource.class)
                .removeDataSource(baseMapper.selectById(dsId).getName());
        this.baseMapper.deleteById(dsId);
        return Boolean.TRUE;
    }

    /**
     * 添加动态数据源
     *
     * @param conf 数据源信息
     */
    @Override
    public void addDynamicDataSource(GenDatasourceConf conf) {
        DataSourceProperty dataSourceProperty = new DataSourceProperty();
        dataSourceProperty.setPoolName(conf.getName());
        dataSourceProperty.setUrl(conf.getUrl());
        dataSourceProperty.setUsername(conf.getUsername());
        dataSourceProperty.setPassword(conf.getPassword());
        dataSourceProperty.setLazy(true);
        DataSource dataSource = hikariDataSourceCreator.createDataSource(dataSourceProperty);
        SpringContextHolder.getBean(DynamicRoutingDataSource.class).addDataSource(dataSourceProperty.getPoolName(),
                dataSource);
    }

    /**
     * 校验数据源配置是否有效
     *
     * @param conf 数据源信息
     * @return 有效/无效
     */
    @Override
    public Boolean checkDataSource(GenDatasourceConf conf) {
        try {
            DriverManager.getConnection(conf.getUrl(), conf.getUsername(), conf.getPassword());
        } catch (SQLException e) {
            log.error("数据源配置 {} , 获取链接失败", conf.getName(), e);
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

}
