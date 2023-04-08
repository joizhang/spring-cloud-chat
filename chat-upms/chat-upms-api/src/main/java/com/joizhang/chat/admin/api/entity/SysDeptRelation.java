package com.joizhang.chat.admin.api.entity;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 部门关系表
 * </p>
 */
@Schema(description = "部门关系")
@Data
@EqualsAndHashCode(callSuper = true)
public class SysDeptRelation extends Model<SysDeptRelation> {

    private static final long serialVersionUID = 1L;

    /**
     * 祖先节点
     */
    @Schema(description = "祖先节点")
    private Long ancestor;

    /**
     * 后代节点
     */
    @Schema(description = "后代节点")
    private Long descendant;

}
