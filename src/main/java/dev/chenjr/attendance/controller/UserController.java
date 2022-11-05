package dev.chenjr.attendance.controller;

import dev.chenjr.attendance.dao.entity.User;
import dev.chenjr.attendance.exception.AccountExistsException;
import dev.chenjr.attendance.exception.UserNotFoundException;
import dev.chenjr.attendance.service.IAccountService;
import dev.chenjr.attendance.service.ISmsService;
import dev.chenjr.attendance.service.IUserService;
import dev.chenjr.attendance.service.dto.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;

@Slf4j
@RestController
@RequestMapping("/users")
@Tag(name = "用户", description = "用户CRUD")
public class UserController {
    
    @Autowired
    IAccountService accountService;
    @Autowired
    IUserService userService;
    
    @Autowired
    ISmsService smsService;
    
    @GetMapping("")
    @Operation(description = "获取用户列表")
    public RestResponse<PageWrapper<UserDTO>> listUsers(
            @ParameterObject PageSort pageSort) {
        PageWrapper<UserDTO> users = this.userService.listUser(pageSort);
        return RestResponse.okWithData(users);
    }
    
    
    @SecurityRequirements()
    @GetMapping("/init-setup")
    @Operation(description = "创建管理员, 仅一次")
    public RestResponse<TokenUidDTO> initSetup() {
        // 重复执行会报unique 索引冲突
        boolean exists = userService.userExists("initadmin");
        if (exists) {
            throw new AccountExistsException();
        }
        RegisterRequest admin = new RegisterRequest();
        admin.setRealName("初始管理员");
        admin.setLoginName("initadmin");
        admin.setPhone("13456789123");
        admin.setRoles(Arrays.asList("admin", "teacher"));
        return RestResponse.okWithData(accountService.register(admin));
    }
    
    @PostMapping("")
    @Operation(description = "创建用户，仅限管理员, 无密码")
    public RestResponse<UserDTO> createUser(@RequestBody @Validated UserDTO toCreate) {
        
        return RestResponse.okWithData(userService.createAndInitUser(toCreate));
    }
    
    @GetMapping("/me")
    @Operation(description = "获取指定用户的信息")
    public RestResponse<?> getCurrentUser(@Parameter(hidden = true) @AuthenticationPrincipal User user) {
        return RestResponse.okWithData(userService.user2dto(user));
    }
    
    @GetMapping("/{uid}")
    @Operation(description = "获取指定用户的信息")
    public RestResponse<UserDTO> getUser(@PathVariable Long uid) {
        UserDTO user = userService.getUser(uid);
        return RestResponse.okWithData(user);
    }
    
    @DeleteMapping("/{uid}")
    @Operation(description = "注销帐户")
    public RestResponse<?> deleteUser(@PathVariable Long uid) {
        userService.deleteByUid(uid);
        return RestResponse.okWithMsg("删除成功");
    }
    
    @DeleteMapping("/byPhone/{phone}")
    @Operation(description = "删除帐户")
    public RestResponse<?> deleteUser(@PathVariable String phone) {
        User userByPhone = userService.getUserByPhone(phone);
        if (userByPhone == null) {
            throw new UserNotFoundException();
        }
        userService.deleteByUid(userByPhone.getId());
        return RestResponse.okWithMsg("删除成功");
    }
    
    @PatchMapping("/{uid}")
    @Operation(description = "修改用户信息,部分修改")
    public RestResponse<UserDTO> modifyUser(@PathVariable Long uid, @RequestBody UserDTO desiredDto) {
        desiredDto.setId(uid);
        UserDTO modifiedDto = userService.modifyUser(desiredDto);
        return RestResponse.okWithData(modifiedDto);
    }
    
    @PutMapping(value = "/{uid}/avatar", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @Operation(description = "修改头像（上传）")
    public RestResponse<String> modifyAvatar(@PathVariable Long uid, @RequestParam("avatar") MultipartFile uploaded) {
        
        String storeName = userService.modifyAvatar(uid, uploaded);
        
        return RestResponse.okWithData(storeName);
    }
    
    @Operation(description = "直接返回指定用户的头像(文件)")
    @GetMapping(value = "/{uid}/avatar")
    public void getAvatar(@PathVariable Long uid, HttpServletResponse response) throws IOException {
        UserDTO userDTO = userService.getUser(uid);
        response.sendRedirect(userDTO.getAvatar());
    }
    
    @Operation(description = "直接返回当前用户的头像(文件)")
    @GetMapping(value = "/me/avatar")
    public void getCurrentAvatar(
            @Parameter(hidden = true) @AuthenticationPrincipal User user,
            HttpServletResponse response
    ) throws IOException {
        UserDTO userDTO = userService.user2dto(user);
        // 重定向到当前用户的文件
        response.sendRedirect(userDTO.getAvatar());
    }
    
    @PutMapping(value = "/me/avatar", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @Operation(description = "修改头像（上传）")
    public RestResponse<String> modifyCurrentAvatar(
            @Parameter(hidden = true) @AuthenticationPrincipal User user,
            @RequestPart("avatar") MultipartFile uploaded) {
        String storeName = userService.modifyAvatar(user.getId(), uploaded);
        return RestResponse.okWithData(storeName);
    }
    
}
