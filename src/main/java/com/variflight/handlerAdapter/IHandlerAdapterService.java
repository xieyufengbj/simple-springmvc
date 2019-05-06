package com.variflight.handlerAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * @author XieYufeng
 * @ClassName: HandlerAdapterService
 * @description:
 * @date 2019/5/5 17:22
 */
public interface IHandlerAdapterService {

    Object[] handler(HttpServletRequest request, HttpServletResponse response, Method method, Map<String, Object> beans);
}
