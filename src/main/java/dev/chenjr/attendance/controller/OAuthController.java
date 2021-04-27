package dev.chenjr.attendance.controller;


import dev.chenjr.attendance.dao.entity.User;
import dev.chenjr.attendance.exception.SuperException;
import dev.chenjr.attendance.service.dto.InputBindThirdPartyDTO;
import dev.chenjr.attendance.service.dto.RestResponse;
import dev.chenjr.attendance.service.impl.AccountService;
import dev.chenjr.attendance.service.impl.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.util.MimeTypeUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Slf4j
@RestController
@RequestMapping("/oauth")
@Tag(name = "OAuth第三方登陆")
public class OAuthController {
    @Autowired
    UserService userService;
    @Autowired
    AccountService accountService;

    @GetMapping("/callback/{type}")
    public void callback(@PathVariable String type, @Parameter String code, @Parameter String state, HttpServletResponse resp) throws IOException {
        log.info("state:{},code:{}", state, code);
        resp.setContentType(MimeTypeUtils.TEXT_HTML_VALUE);
        PrintWriter writer = resp.getWriter();
        writer.printf("type:%s state:%s code:%s%n", type, state, code);
    }

    @PostMapping("/{uid}")
    @Operation(description = "绑定第三方账号")
    public RestResponse<?> bindThirdParty(@Validated @RequestBody InputBindThirdPartyDTO thirdPartyDTO,
                                          @PathVariable Long uid,
                                          @AuthenticationPrincipal User user
    ) {

        if (uid != 0 && !user.getId().equals(uid)) {
            throw new SuperException("没有操作权限！");
        }
        thirdPartyDTO.setUid(uid);
        accountService.bindThirdParty(thirdPartyDTO);
        return RestResponse.ok();
    }
}
