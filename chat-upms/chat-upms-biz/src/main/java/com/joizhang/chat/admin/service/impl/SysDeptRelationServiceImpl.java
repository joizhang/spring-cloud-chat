package com.joizhang.chat.admin.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.joizhang.chat.admin.api.entity.SysDept;
import com.joizhang.chat.admin.api.entity.SysDeptRelation;
import com.joizhang.chat.admin.mapper.SysDeptRelationMapper;
import com.joizhang.chat.admin.service.SysDeptRelationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 */
@Service
@RequiredArgsConstructor
public class SysDeptRelationServiceImpl extends ServiceImpl<SysDeptRelationMapper, SysDeptRelation>
        implements SysDeptRelationService {

    private final SysDeptRelationMapper sysDeptRelationMapper;

    /**
     * 维护部门关系
     *
     * @param sysDept 部门
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveDeptRelation(SysDept sysDept) {
        // 增加部门关系表
        List<SysDeptRelation> relationList = sysDeptRelationMapper.selectList(
                        Wrappers.<SysDeptRelation>query().lambda().eq(SysDeptRelation::getDescendant, sysDept.getParentId()))
                .stream().map(relation -> {
                    relation.setDescendant(sysDept.getDeptId());
                    return relation;
                }).collect(Collectors.toList());
        if (CollUtil.isNotEmpty(relationList)) {
            this.saveBatch(relationList);
        }

        // 自己也要维护到关系表中
        SysDeptRelation own = new SysDeptRelation();
        own.setDescendant(sysDept.getDeptId());
        own.setAncestor(sysDept.getDeptId());
        sysDeptRelationMapper.insert(own);
    }

    /**
     * 通过ID删除部门关系
     *
     * @param id
     */
    @Override
    public void removeDeptRelationById(Long id) {
        baseMapper.deleteDeptRelationsById(id);
    }

    /**
     * 更新部门关系
     *
     * @param relation
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateDeptRelation(SysDeptRelation relation) {
        baseMapper.deleteDeptRelations(relation);
        baseMapper.insertDeptRelations(relation);
    }

}
