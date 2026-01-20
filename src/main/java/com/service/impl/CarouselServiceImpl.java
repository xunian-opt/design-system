package com.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.utils.PageUtils;
import com.utils.Query;

import com.dao.CarouselDao;
import com.entity.CarouselEntity;
import com.service.CarouselService;

/**
 * 轮播图服务实现类
 */
@Service("carouselService")
public class CarouselServiceImpl extends ServiceImpl<CarouselDao, CarouselEntity> implements CarouselService {

    @Override
    public PageUtils queryPage(Map<String, Object> params, Wrapper<CarouselEntity> wrapper) {
        // 1. 利用 Query 工具类解析 params 中的 page 和 limit 参数，构建 MyBatis Plus 的 Page 对象
        Page<CarouselEntity> page = new Query<CarouselEntity>(params).getPage();

        // 2. 执行分页查询，将结果设置回 page 对象 records 中
        page.setRecords(baseMapper.selectPage(page, wrapper));

        // 3. 将结果封装成统一的 PageUtils 返回给 Controller
        return new PageUtils(page);
    }
}