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

//    @RequestMapping("/page")
//    public R page(@RequestParam Map<String, Object> params, RoleEntity role) {
//        EntityWrapper<RoleEntity> ew = new EntityWrapper<RoleEntity>();
//        PageUtils page = roleService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.allLike(ew, role), params), params));
//        return R.ok().put("data", page);
//    }

    /**
     * 列表 (修改版：简化查询逻辑，便于调试)
     */
    @RequestMapping("/page")
    public R page(@RequestParam Map<String, Object> params, RoleEntity role) {
        // 1. 手动构建查询条件，不使用 MPUtil.allLike，避免自动注入错误的 WHERE 条件
        EntityWrapper<RoleEntity> ew = new EntityWrapper<RoleEntity>();

        // 2. 如果前端传了 name 参数，才进行模糊查询
        String name = (String)params.get("name");
        if(name != null && !name.isEmpty()){
            ew.like("name", name);
        }

        // 3. 直接调用 service 查询
        PageUtils page = roleService.queryPage(params, ew);

        // 4. 打印日志帮助调试 (查看控制台输出)
        System.out.println("角色查询结果数量: " + page.getTotalCount());

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
