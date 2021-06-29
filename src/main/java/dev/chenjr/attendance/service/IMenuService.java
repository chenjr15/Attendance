package dev.chenjr.attendance.service;

import dev.chenjr.attendance.dao.entity.User;
import dev.chenjr.attendance.service.dto.MenuDTO;

import java.util.List;

public interface IMenuService {
  
  
  /**
   * 返回整个目录树
   *
   * @return 整个目录树
   */
  List<MenuDTO> listMenu();
  
  /**
   * 创建菜单
   *
   * @param menuDTO 创建的信息
   * @return 创建成功的菜单项
   */
  MenuDTO createMenu(MenuDTO menuDTO);
  
  /**
   * 删除菜单项
   *
   * @param menuId 菜单id
   */
  void deleteMenu(long menuId);
  
  /**
   * 修改菜单
   *
   * @param menuDTO 预修改的菜单
   * @return 修改后的结果
   */
  MenuDTO modifyMenu(MenuDTO menuDTO);
  
  /**
   * 按id获取菜单，不返回子节点
   *
   * @param menuId 菜单id
   * @return 菜单dto
   */
  MenuDTO getMenu(long menuId);
  
  /**
   * 返回某个用户的菜单树
   *
   * @param user 用户
   * @return 菜单树
   */
  List<MenuDTO> listUserMenu(User user);
  
  /**
   * 排序子菜单项, 如果给定的id列表不是完整的列表的话，会将给定的列表排在前面，剩下的按原来的顺序排在后面
   *
   * @param menuId 菜单id
   * @param orders 顺序
   * @return 排序后的子菜单
   */
  MenuDTO orderSubMenus(long menuId, List<Long> orders);
}
