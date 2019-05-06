package com.variflight.argumentResolver;

import com.variflight.annotation.VariRequestParam;
import com.variflight.annotation.VariService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * @author XieYufeng
 * @ClassName: RequestParamArgumentResolver
 * @description: 解析声明注解为RequestParam，获取注解的值
 * @date 2019/5/5 17:31
 */
@VariService("requestParamArgumentResolver")
public class RequestParamArgumentResolver implements ArgumentResolver {

    // 判断传进来的参数是否为VariRequestParam
    @Override
    public boolean support(Class<?> type, int paramIndex, Method method) {

        Annotation[][] an = method.getParameterAnnotations();
        Annotation[] paramAns = an[paramIndex];

        // 判断传进去的paramAn.getClass() 是不是 VariRequestParam 类型
        for (Annotation paramAn : paramAns) {
            if (VariRequestParam.class.isAssignableFrom(paramAn.getClass())) {
                return true;
            }
        }
        return false;
    }

    // 参数解析，并获取注解的值
    @Override
    public Object argumentResolver(HttpServletRequest request, HttpServletResponse response, Class<?> type, int paramIndex, Method method) {

        Annotation[][] an = method.getParameterAnnotations();
        Annotation[] paramAns = an[paramIndex];

        for (Annotation paramAn : paramAns) {
            if (VariRequestParam.class.isAssignableFrom(paramAn.getClass())) {
                VariRequestParam requestParam = (VariRequestParam) paramAn;

                String value = requestParam.value();
                return request.getParameter(value);
            }
        }
        return null;
    }
}
