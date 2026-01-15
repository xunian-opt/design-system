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

import com.entity.ZhuangxiuzuopinEntity;
import com.entity.view.ZhuangxiuzuopinView;

import com.service.ZhuangxiuzuopinService;
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
 * 装修作品
 * 后端接口
 * @author 
 * @email 
 * @date 2023-04-21 15:06:09
 */
@RestController
@RequestMapping("/zhuangxiuzuopin")
public class ZhuangxiuzuopinController {
    @Autowired
    private ZhuangxiuzuopinService zhuangxiuzuopinService;

    @Autowired
    private StoreupService storeupService;


    /**
     * 后端列表 (分页 + 模糊查询)
     * 对应前端: loadWorks()
     */
    @RequestMapping("/page")
    public R page(@RequestParam Map<String, Object> params, ZhuangxiuzuopinEntity zhuangxiuzuopin,
                  HttpServletRequest request) {
        EntityWrapper<ZhuangxiuzuopinEntity> ew = new EntityWrapper<ZhuangxiuzuopinEntity>();

        // 1. 获取查询参数
        String zuopinmingcheng = (String) params.get("zuopinmingcheng");
        String fenggeleixing = (String) params.get("fenggeleixing");

        // 2. 构建模糊查询条件 (LIKE)
        if (StringUtils.isNotBlank(zuopinmingcheng)) {
            ew.like("zuopinmingcheng", zuopinmingcheng);
        }
        if (StringUtils.isNotBlank(fenggeleixing)) {
            ew.like("fenggeleixing", fenggeleixing);
        }

        // 3. 排序：按 ID 倒序，保证新添加的在前面
        ew.orderBy("id", false);

        // 4. 执行分页查询
        PageUtils page = zhuangxiuzuopinService.queryPage(params, ew);

        return R.ok().put("data", page);
    }

    /**
     * 前端列表 (忽略权限，通常用于用户端展示)
     */
    @IgnoreAuth
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params, ZhuangxiuzuopinEntity zhuangxiuzuopin,
                  HttpServletRequest request){
        EntityWrapper<ZhuangxiuzuopinEntity> ew = new EntityWrapper<ZhuangxiuzuopinEntity>();

        // 同样支持模糊查询
        String zuopinmingcheng = (String) params.get("zuopinmingcheng");
        String fenggeleixing = (String) params.get("fenggeleixing");

        if (StringUtils.isNotBlank(zuopinmingcheng)) {
            ew.like("zuopinmingcheng", zuopinmingcheng);
        }
        if (StringUtils.isNotBlank(fenggeleixing)) {
            ew.like("fenggeleixing", fenggeleixing);
        }
        ew.orderBy("id", false);

        PageUtils page = zhuangxiuzuopinService.queryPage(params, ew);
        return R.ok().put("data", page);
    }

    /**
     * 列表 (不分页，用于下拉框等)
     */
    @RequestMapping("/lists")
    public R list(ZhuangxiuzuopinEntity zhuangxiuzuopin){
        EntityWrapper<ZhuangxiuzuopinEntity> ew = new EntityWrapper<ZhuangxiuzuopinEntity>();
        ew.allEq(MPUtil.allEQMapPre(zhuangxiuzuopin, "zhuangxiuzuopin"));
        return R.ok().put("data", zhuangxiuzuopinService.selectListView(ew));
    }

    /**
     * 查询单个视图对象
     */
    @RequestMapping("/query")
    public R query(ZhuangxiuzuopinEntity zhuangxiuzuopin){
        EntityWrapper<ZhuangxiuzuopinEntity> ew = new EntityWrapper<ZhuangxiuzuopinEntity>();
        ew.allEq(MPUtil.allEQMapPre(zhuangxiuzuopin, "zhuangxiuzuopin"));
        ZhuangxiuzuopinView zhuangxiuzuopinView = zhuangxiuzuopinService.selectView(ew);
        return R.ok("查询装修作品成功").put("data", zhuangxiuzuopinView);
    }

    /**
     * 后端详情
     * 对应前端: viewDetail(id) 和 editWork(id) 回显
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
        ZhuangxiuzuopinEntity zhuangxiuzuopin = zhuangxiuzuopinService.selectById(id);
        return R.ok().put("data", zhuangxiuzuopin);
    }

    /**
     * 前端详情
     */
    @IgnoreAuth
    @RequestMapping("/detail/{id}")
    public R detail(@PathVariable("id") Long id){
        ZhuangxiuzuopinEntity zhuangxiuzuopin = zhuangxiuzuopinService.selectById(id);
        return R.ok().put("data", zhuangxiuzuopin);
    }

    /**
     * 后端保存 (新增)
     * 对应前端: saveWork() -> add
     */
    @RequestMapping("/save")
    public R save(@RequestBody ZhuangxiuzuopinEntity zhuangxiuzuopin, HttpServletRequest request){
        // 生成唯一ID (时间戳+随机数)
        zhuangxiuzuopin.setId(new Date().getTime() + new Double(Math.floor(Math.random()*1000)).longValue());

        // 设置添加时间 (如果前端没传)
        if (zhuangxiuzuopin.getAddtime() == null) {
            zhuangxiuzuopin.setAddtime(new Date());
        }

        zhuangxiuzuopinService.insert(zhuangxiuzuopin);
        return R.ok();
    }

    /**
     * 前端保存 (通常用于用户投稿)
     */
    @RequestMapping("/add")
    public R add(@RequestBody ZhuangxiuzuopinEntity zhuangxiuzuopin, HttpServletRequest request){
        zhuangxiuzuopin.setId(new Date().getTime() + new Double(Math.floor(Math.random()*1000)).longValue());
        if (zhuangxiuzuopin.getAddtime() == null) {
            zhuangxiuzuopin.setAddtime(new Date());
        }
        zhuangxiuzuopinService.insert(zhuangxiuzuopin);
        return R.ok();
    }

    /**
     * 修改
     * 对应前端: saveWork() -> edit
     */
    @RequestMapping("/update")
    @Transactional
    public R update(@RequestBody ZhuangxiuzuopinEntity zhuangxiuzuopin, HttpServletRequest request){
        // updateById 会根据 ID 更新非空字段
        zhuangxiuzuopinService.updateById(zhuangxiuzuopin);
        return R.ok();
    }

    /**
     * 删除
     * 对应前端: deleteWork() / deleteSelected()
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids){
        zhuangxiuzuopinService.deleteBatchIds(Arrays.asList(ids));
        return R.ok();
    }






}
