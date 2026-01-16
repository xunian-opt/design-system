package com.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.entity.RoleEntity;
import com.service.RoleService;
import com.utils.MPUtil;
import com.utils.PageUtils;
import com.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;

@RequestMapping("role")
@RestController
public class RoleController {
    @Autowired
    private RoleService roleService;

    @RequestMapping("/page")
    public R page(@RequestParam Map<String, Object> params, RoleEntity role) {
        EntityWrapper<RoleEntity> ew = new EntityWrapper<RoleEntity>();
        PageUtils page = roleService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.allLike(ew, role), params), params));
        return R.ok().put("data", page);
    }

    @RequestMapping("/list")
    public R list(RoleEntity role) {
        EntityWrapper<RoleEntity> ew = new EntityWrapper<RoleEntity>();
        ew.allEq(MPUtil.allEQMapPre(role, "role"));
        return R.ok().put("data", roleService.selectListView(ew));
    }

    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") String id) {
        RoleEntity role = roleService.selectById(id);
        return R.ok().put("data", role);
    }

    @PostMapping("/save")
    public R save(@RequestBody RoleEntity role) {
        if (roleService.selectOne(new EntityWrapper<RoleEntity>().eq("code", role.getCode())) != null) {
            return R.error("角色编码已存在");
        }
        role.setId(new Date().getTime() + new Double(Math.floor(Math.random() * 1000)).longValue());
        roleService.insert(role);
        return R.ok();
    }

    @RequestMapping("/update")
    public R update(@RequestBody RoleEntity role) {
        RoleEntity r = roleService.selectOne(new EntityWrapper<RoleEntity>().eq("code", role.getCode()));
        if (r != null && r.getId() != null && !r.getId().equals(role.getId())) {
            return R.error("角色编码已存在。");
        }
        roleService.updateById(role);
        return R.ok();
    }

    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids) {
        roleService.deleteBatchIds(Arrays.asList(ids));
        return R.ok();
    }
}
