package dev.chenjr.attendance.service;

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
}
