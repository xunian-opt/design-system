package com.dao;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.entity.RoleEntity;

public interface RoleDao extends BaseMapper<RoleEntity> {
    List<RoleEntity> selectListView(@Param("ew") Wrapper<RoleEntity> wrapper);
    List<RoleEntity> selectListView(Pagination page,@Param("ew") Wrapper<RoleEntity> wrapper);
}
