package dev.chenjr.attendance.controller;

import dev.chenjr.attendance.dao.entity.User;
import dev.chenjr.attendance.service.IMenuService;
import dev.chenjr.attendance.service.dto.MenuDTO;
import dev.chenjr.attendance.service.dto.RestResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/menus")
@Tag(name = "菜单", description = "菜单CRUD")
public class MenuController {
  @Autowired
  IMenuService menuService;
  
  @GetMapping("")
  @Operation(description = "返回整个菜单树")
  public RestResponse<List<MenuDTO>> listMenu() {
    List<MenuDTO> rootMenu = menuService.listMenu();
    return RestResponse.okWithData(rootMenu);
  }
  
  @GetMapping("/my")
  @Operation(description = "返回自己的菜单树")
  public RestResponse<List<MenuDTO>> listMyMenu(
          @Parameter(hidden = true) @AuthenticationPrincipal User user
  ) {
    List<MenuDTO> rootMenu = menuService.listUserMenu(user);
    return RestResponse.okWithData(rootMenu);
  }
  
  @GetMapping("/{menuId}")
  @Operation(description = "按id获取菜单，不返回子节点")
  public RestResponse<MenuDTO> getMenu(
          @PathVariable long menuId
  
  ) {
    MenuDTO menu = menuService.getMenu(menuId);
    return RestResponse.okWithData(menu);
  }
  
  @PostMapping("")
  @Operation(description = "创建菜单")
  public RestResponse<MenuDTO> createMenu(
          @RequestBody @Validated MenuDTO menuDTO
  ) {
    MenuDTO created = menuService.createMenu(menuDTO);
    return RestResponse.okWithData(created);
  }
  
  @DeleteMapping("/{menuId}")
  @Operation(description = "删除菜单项")
  public RestResponse<MenuDTO> deleteMenu(
          @PathVariable long menuId
  ) {
    menuService.deleteMenu(menuId);
    return RestResponse.okWithMsg("删除成功！");
  }
  
  @PatchMapping("/{menuId}")
  @Operation(description = "修改菜单")
  public RestResponse<MenuDTO> deleteMenu(
          @PathVariable long menuId,
          @RequestBody @Validated MenuDTO menuDTO
  ) {
    menuDTO.setId(menuId);
    MenuDTO modified = menuService.modifyMenu(menuDTO);
    return RestResponse.okWithData(modified);
  }
  
  @PutMapping("/{menuId}/orders")
  @Operation(description = "排序子菜单项, 如果给定的子菜单id列表不是完整的子菜单列表的话，" +
          "会将给定的id按给定顺序排在前面，剩下的按原来的顺序排在后面。\n" +
          "假如有原顺序：\n" +
          "```\n" +
          "A---\n" +
          "  |-B\n" +
          "  |-C\n" +
          "  |-D\n" +
          "  |-E\n" +
          "```\n" +
          "给定排序列表是:\n" +
          "```\n" +
          "E,C\n" +
          "```\n" +
          "则排序后的顺序应该是\n" +
          "```\n" +
          "A---\n" +
          "  |-E\n" +
          "  |-C\n" +
          "  |-B\n" +
          "  |-D\n" +
          "```\n" +
          "")
  public RestResponse<MenuDTO> orderMenu(
          @PathVariable long menuId,
          @RequestBody List<Long> orders
  ) {
    MenuDTO ordered = menuService.orderSubMenus(menuId, orders);
    return RestResponse.okWithData(ordered);
  }
}
