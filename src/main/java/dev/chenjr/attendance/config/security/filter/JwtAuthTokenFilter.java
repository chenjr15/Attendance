package dev.chenjr.attendance.config.security.filter;

import dev.chenjr.attendance.dao.entity.User;
import dev.chenjr.attendance.exception.TokenException;
import dev.chenjr.attendance.service.impl.UserService;
import dev.chenjr.attendance.utils.JwtTokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
public class JwtAuthTokenFilter extends OncePerRequestFilter {
    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private UserService userService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //http  请求头中的token
        String token = request.getHeader(jwtTokenUtil.getHeader());

        if (token == null || token.length() <= jwtTokenUtil.headerPrefix.length()) {
            throw new TokenException(HttpStatus.UNAUTHORIZED);
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
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(user, null, AuthorityUtils.NO_AUTHORITIES);
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        filterChain.doFilter(request, response);

    }
}
