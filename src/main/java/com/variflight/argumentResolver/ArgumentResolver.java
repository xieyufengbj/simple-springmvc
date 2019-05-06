package com.variflight.argumentResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * @author XieYufeng
 * @ClassName: ArgumentResolver
 * @description:
 * @date 2019/5/5 17:20
 */
public interface ArgumentResolver {

    boolean support(Class<?> type, int paramIndex, Method method);

    // 参数解析方法  paramIndex表示蚕食索引下坐标，有很多注解，知道是哪个参数的注解，每个参数的索引顺序不一样
    Object argumentResolver(HttpServletRequest request, HttpServletResponse response, Class<?> type,
                            int paramIndex, Method method);
}
