package dev.chenjr.attendance.filter;

import dev.chenjr.attendance.dao.entity.User;
import dev.chenjr.attendance.exception.TokenException;
import dev.chenjr.attendance.service.dto.RoleDTO;
import dev.chenjr.attendance.service.impl.RoleService;
import dev.chenjr.attendance.service.impl.UserService;
import dev.chenjr.attendance.utils.JwtTokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Slf4j
@Component
public class JwtAuthTokenFilter extends OncePerRequestFilter {
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    
    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;
    
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //http  请求头中的token
        String token = request.getHeader(jwtTokenUtil.getHeader());
        
        if (token == null || token.length() <= jwtTokenUtil.headerPrefix.length()) {
            filterChain.doFilter(request, response);
            return;
        }
        
        token = token.substring(jwtTokenUtil.headerPrefix.length());
        if (jwtTokenUtil.isTokenExpired(token)) {
            throw new TokenException(HttpStatus.UNAUTHORIZED, "token已过期");
        }
        Long uid = jwtTokenUtil.getUidFromToken(token);
        if (uid == null) {
            throw new TokenException(HttpStatus.UNAUTHORIZED, "无效的token");
        }
        User user = userService.getUserById(uid);
        
        List<GrantedAuthority> authorityList = AuthorityUtils.NO_AUTHORITIES;
        List<RoleDTO> userRole = roleService.getUserRole(uid);
        if (userRole.size() != 0) {
            String[] roles = new String[userRole.size()];
            
            for (int i = 0, userRoleSize = userRole.size(); i < userRoleSize; i++) {
                RoleDTO roleDTO = userRole.get(i);
                roles[i] = roleDTO.getCode();
            }
            //log.info("角色：{},roles:{}", userRole, roles);
            authorityList = AuthorityUtils.createAuthorityList(roles);
        }
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(user, null, authorityList);
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        filterChain.doFilter(request, response);
        
    }
}
