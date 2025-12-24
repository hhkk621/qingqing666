package com.javasm.qingqing.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.javasm.qingqing.common.exception.ExceptionEnum;
import com.javasm.qingqing.common.exception.JavasmException;
import com.javasm.qingqing.product.dao.GgGoodsTypeDao;
import com.javasm.qingqing.product.entity.GgGoodsType;
import com.javasm.qingqing.product.service.GgGoodsTypeService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * 商品管理-商品类型表(GgGoodsType)表服务实现类
 *
 * @author makejava
 * @since 2025-12-24 10:12:02
 */

@Service("ggGoodsTypeService")
public class GgGoodsTypeServiceImpl extends ServiceImpl<GgGoodsTypeDao, GgGoodsType>
        implements GgGoodsTypeService {

    @Override
    public List<GgGoodsType> getGoodsTypeTree() {
        try {
            // 查询所有商品类型
            List<GgGoodsType> allTypes = this.list();
            if (CollectionUtils.isEmpty(allTypes)) {
                return new ArrayList<>();
            }

            // 使用Map存储所有类型，key为id，value为类型对象
            Map<String, GgGoodsType> typeMap = new HashMap<>();
            List<GgGoodsType> rootTypes = new ArrayList<>();

            // 第一遍遍历，创建所有节点
            for (GgGoodsType type : allTypes) {
                // 创建新的对象，避免影响缓存
                GgGoodsType node = new GgGoodsType();
                BeanUtils.copyProperties(type, node);
                node.setChildren(new ArrayList<>());
                typeMap.put(node.getId(), node);
            }

            // 第二遍遍历，构建树形结构
            for (GgGoodsType type : allTypes) {
                GgGoodsType node = typeMap.get(type.getId());

                if ("-1".equals(type.getPid()) || type.getPid() == null) {
                    // 根节点
                    rootTypes.add(node);
                } else {
                    // 子节点，找到父节点
                    GgGoodsType parent = typeMap.get(type.getPid());
                    if (parent != null) {
                        parent.getChildren().add(node);
                    } else {
                        // 如果父节点不存在，也作为根节点
                        rootTypes.add(node);
                    }
                }
            }

            return rootTypes;
        } catch (Exception e) {
            throw new JavasmException(ExceptionEnum.GOODS_TYPE_TREE_FAILED);
        }
    }

    @Override
    public List<GgGoodsType> getChildrenByPid(String pid) {
        try {
            if (pid == null) {
                return new ArrayList<>();
            }

            LambdaQueryWrapper<GgGoodsType> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(GgGoodsType::getPid, pid)
                    .orderByAsc(GgGoodsType::getCreateTime);
            return this.list(queryWrapper);
        } catch (Exception e) {
            throw new JavasmException(ExceptionEnum.GOODS_TYPE_CHILDREN_QUERY_FAILED);
        }
    }

    @Override
    public boolean hasChildren(String id) {
        if (!StringUtils.hasText(id)) {
            return false;
        }

        LambdaQueryWrapper<GgGoodsType> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(GgGoodsType::getPid, id);
        return this.count(queryWrapper) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean addGoodsType(GgGoodsType goodsType) {
        // 参数校验
        if (goodsType == null) {
            throw new JavasmException(ExceptionEnum.Parameter_NUll);
        }

        if (!StringUtils.hasText(goodsType.getTypeName())) {
            throw new JavasmException(ExceptionEnum.GOODS_TYPE_NAME_NULL);
        }

        // 设置创建时间和更新时间
        Date now = new Date();
        goodsType.setCreateTime(now);
        goodsType.setUpdateTime(now);

        // 如果pid为空，则设置为-1（根节点）
        if (!StringUtils.hasText(goodsType.getPid())) {
            goodsType.setPid("-1");
        }

        // 保存到数据库
        boolean saved = this.save(goodsType);
        if (!saved) {
            throw new JavasmException(ExceptionEnum.GOODS_TYPE_SAVE_FAILED);
        }

        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateGoodsType(GgGoodsType goodsType) {
        // 参数校验
        if (goodsType == null || !StringUtils.hasText(goodsType.getId())) {
            throw new JavasmException(ExceptionEnum.GOODS_TYPE_ID_NULL);
        }

        if (!StringUtils.hasText(goodsType.getTypeName())) {
            throw new JavasmException(ExceptionEnum.GOODS_TYPE_NAME_NULL);
        }

        // 检查是否存在
        GgGoodsType existing = this.getById(goodsType.getId());
        if (existing == null) {
            throw new JavasmException(ExceptionEnum.GOODS_TYPE_NOT_FOUND);
        }

        // 禁止将节点移动到自己的子节点下
        if (StringUtils.hasText(goodsType.getPid()) &&
                !checkValidParent(goodsType.getId(), goodsType.getPid())) {
            throw new JavasmException(ExceptionEnum.GOODS_TYPE_PARENT_INVALID);
        }

        // 保留创建时间，只更新更新时间
        goodsType.setCreateTime(existing.getCreateTime());
        goodsType.setUpdateTime(new Date());

        // 更新数据库
        boolean updated = this.updateById(goodsType);
        if (!updated) {
            throw new JavasmException(ExceptionEnum.GOODS_TYPE_UPDATE_FAILED);
        }

        return true;
    }

    /**
     * 检查父节点是否有效（防止循环引用）
     */
    private boolean checkValidParent(String id, String pid) {
        // 不能设置自己为自己的父节点
        if (id.equals(pid)) {
            return false;
        }

        // 获取所有子节点ID
        List<String> allChildrenIds = getAllChildIds(id);

        // 检查pid是否在自己的子节点中
        return !allChildrenIds.contains(pid);
    }

    /**
     * 递归获取所有子节点ID
     */
    private List<String> getAllChildIds(String pid) {
        List<String> allIds = new ArrayList<>();
        getChildIdsRecursive(pid, allIds);
        return allIds;
    }

    private void getChildIdsRecursive(String pid, List<String> allIds) {
        List<GgGoodsType> children = getChildrenByPid(pid);
        if (!CollectionUtils.isEmpty(children)) {
            for (GgGoodsType child : children) {
                allIds.add(child.getId());
                getChildIdsRecursive(child.getId(), allIds);
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteGoodsType(String id) {
        if (!StringUtils.hasText(id)) {
            throw new JavasmException(ExceptionEnum.GOODS_TYPE_ID_NULL);
        }

        // 检查是否存在
        GgGoodsType existing = this.getById(id);
        if (existing == null) {
            throw new JavasmException(ExceptionEnum.GOODS_TYPE_NOT_FOUND);
        }

        // 检查是否存在子节点
        if (hasChildren(id)) {
            throw new JavasmException(ExceptionEnum.GOODS_TYPE_HAS_CHILDREN);
        }

        boolean deleted = this.removeById(id);
        if (!deleted) {
            throw new JavasmException(ExceptionEnum.GOODS_TYPE_DELETE_FAILED);
        }

        return true;
    }

    @Override
    public List<GgGoodsType> searchGoodsTypes(String keyword, String pid) {
        try {
            LambdaQueryWrapper<GgGoodsType> queryWrapper = new LambdaQueryWrapper<>();

            if (StringUtils.hasText(keyword)) {
                queryWrapper.like(GgGoodsType::getTypeName, keyword)
                        .or()
                        .like(GgGoodsType::getDesc, keyword);
            }

            if (StringUtils.hasText(pid)) {
                queryWrapper.eq(GgGoodsType::getPid, pid);
            }

            queryWrapper.orderByAsc(GgGoodsType::getCreateTime);

            return this.list(queryWrapper);
        } catch (Exception e) {
            throw new JavasmException(ExceptionEnum.GOODS_TYPE_QUERY_FAILED);
        }
    }

    @Override
    public List<GgGoodsType> getAllParentOptions() {
        try {
            LambdaQueryWrapper<GgGoodsType> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.select(GgGoodsType::getId, GgGoodsType::getTypeName, GgGoodsType::getPid)
                    .orderByAsc(GgGoodsType::getCreateTime);

            return this.list(queryWrapper);
        } catch (Exception e) {
            throw new JavasmException(ExceptionEnum.GOODS_TYPE_QUERY_FAILED);
        }
    }
}

