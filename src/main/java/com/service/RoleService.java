package com.service;

import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.IService;
import com.entity.RoleEntity;
import com.utils.PageUtils;

public interface RoleService extends IService<RoleEntity> {
    PageUtils queryPage(Map<String, Object> params);
    List<RoleEntity> selectListView(Wrapper<RoleEntity> wrapper);
    PageUtils queryPage(Map<String, Object> params, Wrapper<RoleEntity> wrapper);
}
