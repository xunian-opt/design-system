package com.controller;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;

import com.utils.ValidatorUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.annotation.IgnoreAuth;

import com.entity.GongsixinxiEntity;
import com.entity.view.GongsixinxiView;

import com.service.GongsixinxiService;
import com.service.TokenService;
import com.utils.PageUtils;
import com.utils.R;
import com.utils.MD5Util;
import com.utils.MPUtil;
import com.utils.CommonUtil;
import java.io.IOException;
import com.service.StoreupService;
import com.entity.StoreupEntity;

/**
 * 公司信息
 * 后端接口
 * @author 
 * @email 
 * @date 2023-04-21 15:06:09
 */
@RestController
@RequestMapping("/gongsixinxi")
public class GongsixinxiController {
    @Autowired
    private GongsixinxiService gongsixinxiService;

    @Autowired
    private StoreupService storeupService;


    /**
     * 后端列表（分页查询）
     */
    @RequestMapping("/page")
    public R page(@RequestParam Map<String, Object> params, GongsixinxiEntity gongsixinxi,
                  HttpServletRequest request){

        EntityWrapper<GongsixinxiEntity> ew = new EntityWrapper<GongsixinxiEntity>();

        // 构建查询条件
        if(StringUtils.isNotBlank(gongsixinxi.getGongsimingcheng())) {
            ew.like("gongsimingcheng", gongsixinxi.getGongsimingcheng());
        }
        if(StringUtils.isNotBlank(gongsixinxi.getGongsidizhi())) {
            ew.like("gongsidizhi", gongsixinxi.getGongsidizhi());
        }
        if(StringUtils.isNotBlank(gongsixinxi.getJingyingfanwei())) {
            ew.like("jingyingfanwei", gongsixinxi.getJingyingfanwei());
        }
        if(StringUtils.isNotBlank(gongsixinxi.getZixundianhua())) {
            ew.like("zixundianhua", gongsixinxi.getZixundianhua());
        }

        // 排序
        String orderBy = (String) params.get("orderBy");
        if(StringUtils.isNotBlank(orderBy)) {
            ew.orderBy(orderBy);
        } else {
            ew.orderBy("addtime", false);
        }

        PageUtils page = gongsixinxiService.queryPage(params, ew);

        return R.ok().put("data", page);
    }

    /**
     * 前端列表（分页查询）
     */
    @IgnoreAuth
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params, GongsixinxiEntity gongsixinxi,
                  HttpServletRequest request){

        EntityWrapper<GongsixinxiEntity> ew = new EntityWrapper<GongsixinxiEntity>();

        // 构建查询条件
        if(StringUtils.isNotBlank(gongsixinxi.getGongsimingcheng())) {
            ew.like("gongsimingcheng", gongsixinxi.getGongsimingcheng());
        }
        if(StringUtils.isNotBlank(gongsixinxi.getGongsidizhi())) {
            ew.like("gongsidizhi", gongsixinxi.getGongsidizhi());
        }

        // 排序
        ew.orderBy("addtime", false);

        PageUtils page = gongsixinxiService.queryPage(params, ew);

        return R.ok().put("data", page);
    }

	/**
     * 列表（不分页）
     */
    @RequestMapping("/lists")
    public R list( GongsixinxiEntity gongsixinxi){
       	EntityWrapper<GongsixinxiEntity> ew = new EntityWrapper<GongsixinxiEntity>();
      	ew.allEq(MPUtil.allEQMapPre( gongsixinxi, "gongsixinxi")); 
        return R.ok().put("data", gongsixinxiService.selectListView(ew));
    }

	 /**
     * 查询
     */
    @RequestMapping("/query")
    public R query(GongsixinxiEntity gongsixinxi){
        EntityWrapper< GongsixinxiEntity> ew = new EntityWrapper< GongsixinxiEntity>();
 		ew.allEq(MPUtil.allEQMapPre( gongsixinxi, "gongsixinxi")); 
		GongsixinxiView gongsixinxiView =  gongsixinxiService.selectView(ew);
		return R.ok("查询公司信息成功").put("data", gongsixinxiView);
    }

    /**
     * 后端详情
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
        GongsixinxiEntity gongsixinxi = gongsixinxiService.selectById(id);
        if(gongsixinxi == null) {
            return R.error("公司信息不存在");
        }
        return R.ok().put("data", gongsixinxi);
    }

    /**
     * 前端详情
     */
    @IgnoreAuth
    @RequestMapping("/detail/{id}")
    public R detail(@PathVariable("id") Long id){
        GongsixinxiEntity gongsixinxi = gongsixinxiService.selectById(id);
        if(gongsixinxi == null) {
            return R.error("公司信息不存在");
        }
        return R.ok().put("data", gongsixinxi);
    }




    /**
     * 后端保存
     */
    @RequestMapping("/save")
    @Transactional
    public R save(@RequestBody GongsixinxiEntity gongsixinxi, HttpServletRequest request){
        // 验证必填字段
        if(StringUtils.isBlank(gongsixinxi.getGongsimingcheng())) {
            return R.error("公司名称不能为空");
        }

        // 设置ID和添加时间
        gongsixinxi.setId(new Date().getTime() + new Double(Math.floor(Math.random()*1000)).longValue());
        gongsixinxi.setAddtime(new Date());

        // 验证公司名称是否已存在
        EntityWrapper<GongsixinxiEntity> ew = new EntityWrapper<GongsixinxiEntity>();
        ew.eq("gongsimingcheng", gongsixinxi.getGongsimingcheng());
        List<GongsixinxiEntity> list = gongsixinxiService.selectList(ew);
        if(list != null && list.size() > 0) {
            return R.error("公司名称已存在");
        }

        gongsixinxiService.insert(gongsixinxi);
        return R.ok("保存成功");
    }

    /**
     * 前端保存
     */
    @RequestMapping("/add")
    @Transactional
    public R add(@RequestBody GongsixinxiEntity gongsixinxi, HttpServletRequest request){
        // 验证必填字段
        if(StringUtils.isBlank(gongsixinxi.getGongsimingcheng())) {
            return R.error("公司名称不能为空");
        }

        // 设置ID和添加时间
        gongsixinxi.setId(new Date().getTime() + new Double(Math.floor(Math.random()*1000)).longValue());
        gongsixinxi.setAddtime(new Date());

        // 验证公司名称是否已存在
        EntityWrapper<GongsixinxiEntity> ew = new EntityWrapper<GongsixinxiEntity>();
        ew.eq("gongsimingcheng", gongsixinxi.getGongsimingcheng());
        List<GongsixinxiEntity> list = gongsixinxiService.selectList(ew);
        if(list != null && list.size() > 0) {
            return R.error("公司名称已存在");
        }

        gongsixinxiService.insert(gongsixinxi);
        return R.ok("保存成功");
    }



    /**
     * 修改
     */
    @RequestMapping("/update")
    @Transactional
    public R update(@RequestBody GongsixinxiEntity gongsixinxi, HttpServletRequest request){
        // 验证ID
        if(gongsixinxi.getId() == null) {
            return R.error("ID不能为空");
        }

        // 验证必填字段
        if(StringUtils.isBlank(gongsixinxi.getGongsimingcheng())) {
            return R.error("公司名称不能为空");
        }

        // 验证公司名称是否已存在（排除自己）
        EntityWrapper<GongsixinxiEntity> ew = new EntityWrapper<GongsixinxiEntity>();
        ew.eq("gongsimingcheng", gongsixinxi.getGongsimingcheng());
        ew.ne("id", gongsixinxi.getId());
        List<GongsixinxiEntity> list = gongsixinxiService.selectList(ew);
        if(list != null && list.size() > 0) {
            return R.error("公司名称已存在");
        }

        // 执行更新
        gongsixinxiService.updateById(gongsixinxi);
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

        gongsixinxiService.deleteBatchIds(Arrays.asList(ids));
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

        GongsixinxiEntity gongsixinxi = gongsixinxiService.selectById(id);
        if(gongsixinxi == null) {
            return R.error("公司信息不存在");
        }

        gongsixinxiService.deleteById(id);
        return R.ok("删除成功");
    }


    /**
     * 根据条件查询总数
     */
    @RequestMapping("/count")
    public R count(@RequestParam Map<String, Object> params, GongsixinxiEntity gongsixinxi){
        EntityWrapper<GongsixinxiEntity> ew = new EntityWrapper<GongsixinxiEntity>();

        // 构建查询条件
        if(StringUtils.isNotBlank(gongsixinxi.getGongsimingcheng())) {
            ew.like("gongsimingcheng", gongsixinxi.getGongsimingcheng());
        }
        if(StringUtils.isNotBlank(gongsixinxi.getGongsidizhi())) {
            ew.like("gongsidizhi", gongsixinxi.getGongsidizhi());
        }

        int count = gongsixinxiService.selectCount(ew);
        return R.ok().put("data", count);
    }

    /**
     * 根据公司名称查询
     */
    @RequestMapping("/selectByName")
    public R selectByName(@RequestParam String gongsimingcheng){
        if(StringUtils.isBlank(gongsimingcheng)) {
            return R.error("公司名称不能为空");
        }

        EntityWrapper<GongsixinxiEntity> ew = new EntityWrapper<GongsixinxiEntity>();
        ew.eq("gongsimingcheng", gongsimingcheng);
        GongsixinxiEntity gongsixinxi = gongsixinxiService.selectOne(ew);

        if(gongsixinxi == null) {
            return R.error("公司信息不存在");
        }

        return R.ok().put("data", gongsixinxi);
    }


    /**
     * 模糊搜索（支持多个字段）
     */
    @RequestMapping("/search")
    public R search(@RequestParam Map<String, Object> params){
        String keyword = (String) params.get("keyword");

        if(StringUtils.isBlank(keyword)) {
            return R.error("搜索关键词不能为空");
        }

        EntityWrapper<GongsixinxiEntity> ew = new EntityWrapper<GongsixinxiEntity>();
        ew.like("gongsimingcheng", keyword)
                .or()
                .like("gongsidizhi", keyword)
                .or()
                .like("jingyingfanwei", keyword)
                .or()
                .like("gongsijieshao", keyword);

        PageUtils page = gongsixinxiService.queryPage(params, ew);

        return R.ok().put("data", page);
    }
}


