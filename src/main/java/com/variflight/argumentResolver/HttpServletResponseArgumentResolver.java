package com.variflight.argumentResolver;

import com.variflight.annotation.VariService;

import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * @author XieYufeng
 * @ClassName: HttpServletResponseArgumentResolver
 * @description:
 * @date 2019/5/5 17:31
 */
@VariService("httpServletResponseArgumentResolver")
public class HttpServletResponseArgumentResolver implements ArgumentResolver {

    // 判断传进来到参数是否为response
    @Override
    public boolean support(Class<?> type, int paramIndex, Method method) {

        return ServletResponse.class.isAssignableFrom(type);
    }

    @Override
    public Object argumentResolver(HttpServletRequest request, HttpServletResponse response, Class<?> type, int paramIndex, Method method) {

        return response;
    }
}
