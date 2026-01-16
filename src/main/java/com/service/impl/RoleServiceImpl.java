package com.service.impl;

import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.dao.RoleDao;
import com.entity.RoleEntity;
import com.service.RoleService;
import com.utils.PageUtils;
import com.utils.Query;

@Service("roleService")
public class RoleServiceImpl extends ServiceImpl<RoleDao, RoleEntity> implements RoleService {
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page<RoleEntity> page = this.selectPage(
            new Query<RoleEntity>(params).getPage(),
            new EntityWrapper<RoleEntity>()
        );
        return new PageUtils(page);
    }

    @Override
    public List<RoleEntity> selectListView(Wrapper<RoleEntity> wrapper) {
        return baseMapper.selectListView(wrapper);
    }

    @Override
    public PageUtils queryPage(Map<String, Object> params, Wrapper<RoleEntity> wrapper) {
        Page<RoleEntity> page = new Query<RoleEntity>(params).getPage();
        page.setRecords(baseMapper.selectListView(page, wrapper));
        PageUtils pageUtil = new PageUtils(page);
        return pageUtil;
    }
}
