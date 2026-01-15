package com.controller;

import com.annotation.IgnoreAuth;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.entity.ShangpinfenleiEntity;
import com.entity.view.ShangpinfenleiView;
import com.service.ShangpinfenleiService;
import com.utils.MPUtil;
import com.utils.PageUtils;
import com.utils.R;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 商品分类
 * 后端接口
 * @author
 * @email
 * @date 2023-04-21 15:06:09
 */
@RestController
@RequestMapping("/shangpinfenlei")
public class ShangpinfenleiController {
    @Autowired
    private ShangpinfenleiService shangpinfenleiService;


    /**
     * 后端列表（分页查询）
     */
    @RequestMapping("/page")
    public R page(@RequestParam Map<String, Object> params, ShangpinfenleiEntity shangpinfenlei,
                  HttpServletRequest request){

        EntityWrapper<ShangpinfenleiEntity> ew = new EntityWrapper<ShangpinfenleiEntity>();

        // 构建查询条件
        if(StringUtils.isNotBlank(shangpinfenlei.getShangpinfenlei())) {
            ew.like("shangpinfenlei", shangpinfenlei.getShangpinfenlei());
        }

        // 排序
        String orderBy = (String) params.get("orderBy");
        if(StringUtils.isNotBlank(orderBy)) {
            ew.orderBy(orderBy);
        } else {
            ew.orderBy("addtime", false);
        }

        PageUtils page = shangpinfenleiService.queryPage(params, ew);

        return R.ok().put("data", page);
    }

    /**
     * 前端列表（分页查询）
     */
    @IgnoreAuth
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params, ShangpinfenleiEntity shangpinfenlei,
                  HttpServletRequest request){

        EntityWrapper<ShangpinfenleiEntity> ew = new EntityWrapper<ShangpinfenleiEntity>();

        // 构建查询条件
        if(StringUtils.isNotBlank(shangpinfenlei.getShangpinfenlei())) {
            ew.like("shangpinfenlei", shangpinfenlei.getShangpinfenlei());
        }

        // 排序
        ew.orderBy("addtime", false);

        PageUtils page = shangpinfenleiService.queryPage(params, ew);

        return R.ok().put("data", page);
    }

    /**
     * 列表（不分页）
     */
    @RequestMapping("/lists")
    public R list(ShangpinfenleiEntity shangpinfenlei){
        EntityWrapper<ShangpinfenleiEntity> ew = new EntityWrapper<ShangpinfenleiEntity>();
        ew.allEq(MPUtil.allEQMapPre(shangpinfenlei, "shangpinfenlei"));
        return R.ok().put("data", shangpinfenleiService.selectListView(ew));
    }

    /**
     * 查询
     */
    @RequestMapping("/query")
    public R query(ShangpinfenleiEntity shangpinfenlei){
        EntityWrapper<ShangpinfenleiEntity> ew = new EntityWrapper<ShangpinfenleiEntity>();
        ew.allEq(MPUtil.allEQMapPre(shangpinfenlei, "shangpinfenlei"));
        ShangpinfenleiView shangpinfenleiView = shangpinfenleiService.selectView(ew);
        return R.ok("查询商品分类成功").put("data", shangpinfenleiView);
    }

    /**
     * 后端详情
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
        ShangpinfenleiEntity shangpinfenlei = shangpinfenleiService.selectById(id);
        if(shangpinfenlei == null) {
            return R.error("商品分类不存在");
        }
        return R.ok().put("data", shangpinfenlei);
    }

    /**
     * 前端详情
     */
    @IgnoreAuth
    @RequestMapping("/detail/{id}")
    public R detail(@PathVariable("id") Long id){
        ShangpinfenleiEntity shangpinfenlei = shangpinfenleiService.selectById(id);
        if(shangpinfenlei == null) {
            return R.error("商品分类不存在");
        }
        return R.ok().put("data", shangpinfenlei);
    }




    /**
     * 后端保存
     */
    @RequestMapping("/save")
    @Transactional
    public R save(@RequestBody ShangpinfenleiEntity shangpinfenlei, HttpServletRequest request){
        // 验证必填字段
        if(StringUtils.isBlank(shangpinfenlei.getShangpinfenlei())) {
            return R.error("商品分类名称不能为空");
        }

        // 设置ID和添加时间
        shangpinfenlei.setId(new Date().getTime() + new Double(Math.floor(Math.random()*1000)).longValue());
        shangpinfenlei.setAddtime(new Date());

        // 验证分类名称是否已存在
        EntityWrapper<ShangpinfenleiEntity> ew = new EntityWrapper<ShangpinfenleiEntity>();
        ew.eq("shangpinfenlei", shangpinfenlei.getShangpinfenlei());
        List<ShangpinfenleiEntity> list = shangpinfenleiService.selectList(ew);
        if(list != null && list.size() > 0) {
            return R.error("该商品分类已存在");
        }

        shangpinfenleiService.insert(shangpinfenlei);
        return R.ok("保存成功");
    }
    /**
     * 前端保存
     */
    @RequestMapping("/add")
    @Transactional
    public R add(@RequestBody ShangpinfenleiEntity shangpinfenlei, HttpServletRequest request){
        // 验证必填字段
        if(StringUtils.isBlank(shangpinfenlei.getShangpinfenlei())) {
            return R.error("商品分类名称不能为空");
        }

        // 设置ID和添加时间
        shangpinfenlei.setId(new Date().getTime() + new Double(Math.floor(Math.random()*1000)).longValue());
        shangpinfenlei.setAddtime(new Date());

        // 验证分类名称是否已存在
        EntityWrapper<ShangpinfenleiEntity> ew = new EntityWrapper<ShangpinfenleiEntity>();
        ew.eq("shangpinfenlei", shangpinfenlei.getShangpinfenlei());
        List<ShangpinfenleiEntity> list = shangpinfenleiService.selectList(ew);
        if(list != null && list.size() > 0) {
            return R.error("该商品分类已存在");
        }

        shangpinfenleiService.insert(shangpinfenlei);
        return R.ok("保存成功");
    }



    /**
     * 修改
     */
    @RequestMapping("/update")
    @Transactional
    public R update(@RequestBody ShangpinfenleiEntity shangpinfenlei, HttpServletRequest request){
        // 验证ID
        if(shangpinfenlei.getId() == null) {
            return R.error("ID不能为空");
        }

        // 验证必填字段
        if(StringUtils.isBlank(shangpinfenlei.getShangpinfenlei())) {
            return R.error("商品分类名称不能为空");
        }

        // 验证分类名称是否已存在（排除自己）
        EntityWrapper<ShangpinfenleiEntity> ew = new EntityWrapper<ShangpinfenleiEntity>();
        ew.eq("shangpinfenlei", shangpinfenlei.getShangpinfenlei());
        ew.ne("id", shangpinfenlei.getId());
        List<ShangpinfenleiEntity> list = shangpinfenleiService.selectList(ew);
        if(list != null && list.size() > 0) {
            return R.error("该商品分类已存在");
        }

        // 执行更新
        shangpinfenleiService.updateById(shangpinfenlei);
        return R.ok("修改成功");
    }



    /**
     * 删除
     */
    @RequestMapping("/delete")
    @Transactional
    public R delete(@RequestBody Long[] ids){
        if(ids == null || ids.length == 0) {
            return R.error("请选择要删除的数据");
        }

        shangpinfenleiService.deleteBatchIds(Arrays.asList(ids));
        return R.ok("删除成功");
    }

    /**
     * 根据ID删除
     */
    @RequestMapping("/delete/{id}")
    @Transactional
    public R deleteById(@PathVariable("id") Long id){
        if(id == null) {
            return R.error("ID不能为空");
        }

        ShangpinfenleiEntity shangpinfenlei = shangpinfenleiService.selectById(id);
        if(shangpinfenlei == null) {
            return R.error("商品分类不存在");
        }

        shangpinfenleiService.deleteById(id);
        return R.ok("删除成功");
    }

    /**
     * 根据条件查询总数
     */
    @RequestMapping("/count")
    public R count(@RequestParam Map<String, Object> params, ShangpinfenleiEntity shangpinfenlei){
        EntityWrapper<ShangpinfenleiEntity> ew = new EntityWrapper<ShangpinfenleiEntity>();

        // 构建查询条件
        if(StringUtils.isNotBlank(shangpinfenlei.getShangpinfenlei())) {
            ew.like("shangpinfenlei", shangpinfenlei.getShangpinfenlei());
        }

        int count = shangpinfenleiService.selectCount(ew);
        return R.ok().put("data", count);
    }

    /**
     * 根据分类名称查询
     */
    @RequestMapping("/selectByName")
    public R selectByName(@RequestParam String shangpinfenlei){
        if(StringUtils.isBlank(shangpinfenlei)) {
            return R.error("商品分类名称不能为空");
        }

        EntityWrapper<ShangpinfenleiEntity> ew = new EntityWrapper<ShangpinfenleiEntity>();
        ew.eq("shangpinfenlei", shangpinfenlei);
        ShangpinfenleiEntity entity = shangpinfenleiService.selectOne(ew);

        if(entity == null) {
            return R.error("商品分类不存在");
        }

        return R.ok().put("data", entity);
    }


    /**
     * 模糊搜索
     */
    @RequestMapping("/search")
    public R search(@RequestParam Map<String, Object> params){
        String keyword = (String) params.get("keyword");

        if(StringUtils.isBlank(keyword)) {
            return R.error("搜索关键词不能为空");
        }

        EntityWrapper<ShangpinfenleiEntity> ew = new EntityWrapper<ShangpinfenleiEntity>();
        ew.like("shangpinfenlei", keyword);

        PageUtils page = shangpinfenleiService.queryPage(params, ew);

        return R.ok().put("data", page);
    }








}
