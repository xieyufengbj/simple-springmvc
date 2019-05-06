package com.variflight.handlerAdapter;

import com.variflight.annotation.VariService;
import com.variflight.argumentResolver.ArgumentResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * @author XieYufeng
 * @ClassName: HandlerAdapterServiceImpl
 * @description:
 * @date 2019/5/5 17:25
 */
@VariService("handlerAdapterServiceImpl")
public class HandlerAdapterServiceImpl implements IHandlerAdapterService {

    /**
     * 对method方法里对参数进行处理
     * @param request 需要传入request，拿请求的参数
     * @param response
     * @param method 执行的方法，可以拿到当前待执行的方法有哪些参数
     * @param beans
     * @return
     */
    @Override
    public Object[] handler(HttpServletRequest request, HttpServletResponse response, Method method, Map<String, Object> beans) {

        // 拿到当前待执行的方法有哪些参数
        Class<?>[] paramClazzs = method.getParameterTypes();

        Object[] args = new Object[paramClazzs.length];

        // 拿到所有实现了ArgumentResolver这个接口的实现类
        Map<String, Object> argumentResolvers = getBeanOfType(beans, ArgumentResolver.class);

        int paramIndex = 0;
        int i = 0;
        // 对每一个参数进行循环，每个参数都有特殊处理
        for (Class<?> paramClazz : paramClazzs) {
            // 哪个参数对应了哪个参数解析类，用策略模式来找
            for (Map.Entry<String, Object> entry : argumentResolvers.entrySet()) {
                ArgumentResolver ar = (ArgumentResolver) entry.getValue();

                if (ar.support(paramClazz, paramIndex, method)) {
                    args[i++] = ar.argumentResolver(request, response, paramClazz, paramIndex, method);
                }
            }
            paramIndex++;
        }
        return args;
    }

    // 获取实现了ArgumentResolver接口的所有实例（其实就是每个参数的注解实例）
    private Map<String,Object> getBeanOfType(Map<String,Object> beans, Class<?> intfType) {

        Map<String, Object> resultBeans = new HashMap<>();

        beans.forEach((key, value) -> {
            // 拿到实例 -》反射对象 -》接口
            Class<?>[] intfs = value.getClass().getInterfaces();

            if (intfs != null && intfs.length > 0) {
                for (Class<?> intf : intfs) {
                    // 接口的类型与传入进来的类型一样，把实例加到resultBeans里来
                    if (intf.isAssignableFrom(intfType)) {
                        resultBeans.put(key, value);
                    }
                }
            }
         });

        return resultBeans;
    }
}
