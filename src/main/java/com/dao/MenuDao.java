package com.dao;
import com.entity.MenuEntity;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import java.util.List;

public interface MenuDao extends BaseMapper<MenuEntity> {
    // 根据角色ID查询菜单列表
    @Select("SELECT m.* FROM menu m LEFT JOIN role_menu rm ON m.id = rm.menu_id WHERE rm.role_id = #{roleId} ORDER BY m.order_num")
    List<MenuEntity> selectMenusByRoleId(@Param("roleId") Long roleId);
}