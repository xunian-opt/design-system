package com.service;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.IService;
import com.utils.PageUtils;
import com.entity.CarouselEntity;
import java.util.Map;

/**
 * 轮播图服务接口
 */
public interface CarouselService extends IService<CarouselEntity> {

    /**
     * 分页查询
     * @param params 前端传递的查询参数（包含 page, limit 等）
     * @param wrapper MyBatisPlus 的条件构造器
     * @return 分页工具类
     */
    PageUtils queryPage(Map<String, Object> params, Wrapper<CarouselEntity> wrapper);
}