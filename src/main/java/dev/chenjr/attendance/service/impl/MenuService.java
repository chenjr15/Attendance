package dev.chenjr.attendance.service.impl;

import dev.chenjr.attendance.dao.entity.Menu;
import dev.chenjr.attendance.dao.entity.User;
import dev.chenjr.attendance.dao.mapper.MenuMapper;
import dev.chenjr.attendance.exception.HttpStatusException;
import dev.chenjr.attendance.service.IMenuService;
import dev.chenjr.attendance.service.dto.MenuDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
public class MenuService implements IMenuService {
  
  @Autowired
  MenuMapper menuMapper;
  
  @Autowired
  RoleService roleService;
  
  
  /**
   * 返回整个目录树
   *
   * @return 整个目录树
   */
  @Override
  public List<MenuDTO> listMenu() {
    
    return getSubMenus(0);
  }
  
  /**
   * 返回某个用户的菜单树
   *
   * @param user 用户
   * @return 菜单树
   */
  @Override
  public List<MenuDTO> listUserMenu(User user) {
    Collection<Long> menuId = roleService.getUserMenuId(user.getId());
    return getSubMenus(0, menuId);
  }
  
  
  private List<MenuDTO> getSubMenus(long id) {
    return this.getSubMenus(id, null);
  }
  
  private List<MenuDTO> getSubMenus(long id, Collection<Long> allowedId) {
    List<Menu> children = menuMapper.getChildren(id);
    List<MenuDTO> menuList = new ArrayList<>(children.size());
    
    for (Menu child : children) {
      if (child.getId() == id) {
        continue;
      }
      if (allowedId != null && !allowedId.contains(child.getId())) {
        // 不允许该权限，pass
        continue;
      }
      MenuDTO childDTO = menu2dto(child);
      
      /* 递归 */
      List<MenuDTO> subMenus = getSubMenus(child.getId(), allowedId);
      
      childDTO.setSubs(subMenus);
      childDTO.setChildrenCount(subMenus.size());
      menuList.add(childDTO);
    }
    
    return menuList;
  }
  
  /**
   * 创建菜单
   *
   * @param menuDTO 创建的信息
   * @return 创建成功的菜单项
   */
  @Override
  public MenuDTO createMenu(MenuDTO menuDTO) {
    Menu toCreate = dto2menu(menuDTO);
    menuMapper.insert(toCreate);
    return getMenu(toCreate.getId());
  }
  
  
  /**
   * 删除菜单项
   *
   * @param menuId 菜单id
   */
  @Override
  public void deleteMenu(long menuId) {
    Optional<Boolean> exists = menuMapper.exists(menuId);
    if (!exists.isPresent()) {
      throw HttpStatusException.notFound();
    }
    int i = menuMapper.childrenCount(menuId);
    if (i != 0) {
      throw HttpStatusException.conflict("删除失败，请先删除子节点！");
    }
    menuMapper.deleteById(menuId);
  }
  
  /**
   * 修改菜单 （无法修改子项）
   *
   * @param menuDTO 预修改的菜单
   * @return 修改后的结果
   */
  @Override
  public MenuDTO modifyMenu(MenuDTO menuDTO) {
    Optional<Boolean> exists = menuMapper.exists(menuDTO.getId());
    if (!exists.isPresent()) {
      throw HttpStatusException.notFound();
    }
    Menu newOne = dto2menu(menuDTO);
    menuMapper.updateById(newOne);
    
    return getMenu(newOne.getId());
  }
  
  /**
   * 按id获取菜单，不返回子节点
   *
   * @param menuId 菜单id
   * @return 菜单dto
   */
  @Override
  public MenuDTO getMenu(long menuId) {
    Menu menu = menuMapper.selectById(menuId);
    if (menu == null) {
      throw HttpStatusException.notFound();
    }
    MenuDTO menuDTO = menu2dto(menu);
    menuDTO.setChildrenCount(menuMapper.childrenCount(menuId));
    return menuDTO;
  }
  
  
  private Menu dto2menu(MenuDTO dto) {
    Menu menu = new Menu();
    menu.setId(dto.getId());
    menu.setName(dto.getTitle());
    menu.setParentId(dto.getParentId());
    menu.setIcon(dto.getIcon());
    menu.setOrderValue(dto.getOrderValue());
    menu.setPath(dto.getIndex());
    menu.setType(dto.getType());
    
    return menu;
  }
  
  private MenuDTO menu2dto(Menu menu) {
    MenuDTO menuDTO = new MenuDTO();
    menuDTO.setId(menu.getId());
    menuDTO.setTitle(menu.getName());
    menuDTO.setParentId(menu.getParentId());
    menuDTO.setIcon(menu.getIcon());
    menuDTO.setOrderValue(menu.getOrderValue());
    menuDTO.setIndex(menu.getPath());
    menuDTO.setType(menu.getType());
    
    return menuDTO;
  }
}
