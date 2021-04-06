package dev.chenjr.attendance.config.security.filter;

import dev.chenjr.attendance.utils.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@Component
public class JwtAuthTokenFilter extends OncePerRequestFilter {
    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private UserDetailsService userDetailServiceImpl;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //http  请求头中的token
        String token = request.getHeader(jwtTokenUtil.getHeader());

        if (token != null && token.length() > 7) {
            token = token.substring(7);
            String username = jwtTokenUtil.getUsernameFromToken(token);
            System.out.println(username);
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                try {

                    UserDetails userDetails = userDetailServiceImpl.loadUserByUsername(username);
                    if (jwtTokenUtil.validateToken(token, userDetails)) {
                        //给使用该JWT令牌的用户进行授权
                        UsernamePasswordAuthenticationToken authenticationToken =
                                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                        //设置用户身份授权
                        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                    }
                } catch (UsernameNotFoundException ignored) {

                }

            }
        }
        filterChain.doFilter(request, response);
    }
}
