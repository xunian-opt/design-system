package com.controller;

import java.util.Arrays;
import java.util.Map;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.entity.CarouselEntity;
import com.service.CarouselService; // 需自行创建对应的 Service 接口
import com.utils.MPUtil;
import com.utils.PageUtils;
import com.utils.R;

/**
 * 轮播图管理接口
 */
@RestController
@RequestMapping("carousel")
public class CarouselController {

    @Autowired
    private CarouselService carouselService;

    /**
     * 分页查询列表
     * 支持按 name 模糊查询，结果按 sort 升序排列
     */
    @RequestMapping("/page")
    public R page(@RequestParam Map<String, Object> params, CarouselEntity carousel) {
        EntityWrapper<CarouselEntity> ew = new EntityWrapper<CarouselEntity>();

        // 模糊查询名称
        if(carousel.getName() != null) {
            ew.like("name", carousel.getName());
            carousel.setName(null); // 避免 MPUtil 再次通过 eq 查询
        }

        // 默认按 sort 升序，sort 相同按创建时间降序
        ew.orderBy("sort", true).orderBy("create_time", false);

        PageUtils page = carouselService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, carousel), params), params));
        return R.ok().put("data", page);
    }

    /**
     * 前端展示列表 (通常用于首页轮播，不需要鉴权)
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params) {
        EntityWrapper<CarouselEntity> ew = new EntityWrapper<CarouselEntity>();
        // 仅查询启用的轮播图
        ew.eq("status", 1);
        ew.orderBy("sort", true);

        return R.ok().put("data", carouselService.selectList(ew));
    }

    /**
     * 详情
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id) {
        CarouselEntity carousel = carouselService.selectById(id);
        return R.ok().put("data", carousel);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    public R save(@RequestBody CarouselEntity carousel) {
        if(carousel.getSort() == null) {
            carousel.setSort(0); // 默认排序
        }
        if(carousel.getStatus() == null) {
            carousel.setStatus(1); // 默认启用
        }
        carousel.setCreateTime(new Date());
        carouselService.insert(carousel);
        return R.ok();
    }

    /**
     * 修改
     */
    @PostMapping("/update")
    public R update(@RequestBody CarouselEntity carousel) {
        carousel.setUpdateTime(new Date());
        carouselService.updateById(carousel);
        return R.ok();
    }

    /**
     * 删除
     */
    @PostMapping("/delete")
    public R delete(@RequestBody Long[] ids) {
        carouselService.deleteBatchIds(Arrays.asList(ids));
        return R.ok();
    }
}