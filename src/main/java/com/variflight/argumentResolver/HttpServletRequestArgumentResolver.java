package com.variflight.argumentResolver;

import com.variflight.annotation.VariService;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * @author XieYufeng
 * @ClassName: HttpServletRequestArgumentResolver
 * @description:
 * @date 2019/5/5 17:30
 */
@VariService("httpServletRequestArgumentResolver")
public class HttpServletRequestArgumentResolver implements ArgumentResolver {

    // 判断传进来到参数是否为request
    @Override
    public boolean support(Class<?> type, int paramIndex, Method method) {

        return ServletRequest.class.isAssignableFrom(type);
    }
    // 如果返回到参数是request，则直接返回
    @Override
    public Object argumentResolver(HttpServletRequest request, HttpServletResponse response, Class<?> type, int paramIndex, Method method) {

        return request;
    }
}
