package com.joizhang.chat.admin.service;

import cn.hutool.core.lang.tree.Tree;
import com.baomidou.mybatisplus.extension.service.IService;
import com.joizhang.chat.admin.api.entity.SysDept;

import java.util.List;

/**
 * <p>
 * 部门管理 服务类
 * </p>
 */
public interface SysDeptService extends IService<SysDept> {

    /**
     * 查询部门树菜单
     *
     * @return 树
     */
    List<Tree<Long>> listDeptTrees();

    /**
     * 查询用户部门树
     *
     * @return
     */
    List<Tree<Long>> listCurrentUserDeptTrees();

    /**
     * 添加信息部门
     *
     * @param sysDept
     * @return
     */
    Boolean saveDept(SysDept sysDept);

    /**
     * 删除部门
     *
     * @param id 部门 ID
     * @return 成功、失败
     */
    Boolean removeDeptById(Long id);

    /**
     * 更新部门
     *
     * @param sysDept 部门信息
     * @return 成功、失败
     */
    Boolean updateDeptById(SysDept sysDept);

    /**
     * 查找指定部门的子部门id列表
     *
     * @param deptId 部门id
     * @return List<Long>
     */
    List<Long> listChildDeptId(Long deptId);

}
