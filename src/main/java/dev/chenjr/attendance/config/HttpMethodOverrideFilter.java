package dev.chenjr.attendance.config;

import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.WebUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

/**
 * 修改 Spring 自带的 HiddenHttpMethodFilter的HTTP方法覆盖过滤器
 * <p>
 * 在某些奇怪的平台无法支持所有的HTTP方法，只能使用`GET`和`POST`方法(如uniapp 不支持PATCH方法)
 * 为了解决这个问题采取在客户端发出的POST请求并在Header里面指定方法的办法来曲线救国：
 * `X-HTTP-Method-Override: PATCH`
 *
 * @see org.springframework.web.filter.HiddenHttpMethodFilter
 */
@Component
public class HttpMethodOverrideFilter extends OncePerRequestFilter {
    
    private static final List<String> ALLOWED_METHODS =
            Collections.unmodifiableList(Arrays.asList(HttpMethod.PUT.name(),
                    HttpMethod.DELETE.name(), HttpMethod.PATCH.name()));
    
    /**
     * Default method parameter: {@code X-HTTP-Method-Override}.
     */
    public static final String DEFAULT_METHOD_HEADER = "X-HTTP-Method-Override";
    
    private String methodParam = DEFAULT_METHOD_HEADER;
    
    
    /**
     * Set the parameter name to look for HTTP methods.
     *
     * @see #DEFAULT_METHOD_HEADER
     */
    public void setMethodParam(String methodParam) {
        Assert.hasText(methodParam, "'methodParam' must not be empty");
        this.methodParam = methodParam;
    }
    
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        
        HttpServletRequest requestToUse = request;
        
        if ("POST".equals(request.getMethod()) && request.getAttribute(WebUtils.ERROR_EXCEPTION_ATTRIBUTE) == null) {
            String paramValue = request.getHeader(this.methodParam);
            if (StringUtils.hasLength(paramValue)) {
                String method = paramValue.toUpperCase(Locale.ENGLISH);
                if (ALLOWED_METHODS.contains(method)) {
                    requestToUse = new HttpMethodRequestWrapper(request, method);
                }
            }
        }
        
        filterChain.doFilter(requestToUse, response);
    }
    
    
    /**
     * Simple {@link HttpServletRequest} wrapper that returns the supplied method for
     * {@link HttpServletRequest#getMethod()}.
     */
    private static class HttpMethodRequestWrapper extends HttpServletRequestWrapper {
        
        private final String method;
        
        public HttpMethodRequestWrapper(HttpServletRequest request, String method) {
            super(request);
            this.method = method;
        }
        
        @Override
        public String getMethod() {
            return this.method;
        }
    }
    
}
