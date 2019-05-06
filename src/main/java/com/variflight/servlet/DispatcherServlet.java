package com.variflight.servlet;

import com.variflight.annotation.VariController;
import com.variflight.annotation.VariQualifier;
import com.variflight.annotation.VariRequestMapping;
import com.variflight.annotation.VariService;
import com.variflight.controller.SimpleController;
import com.variflight.handlerAdapter.IHandlerAdapterService;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.*;

/**
 * @author XieYufeng
 * @ClassName: DispatcherServlet
 * @description:
 * @date 2019/5/5 16:45
 */
public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    List<String> classNames = new ArrayList<String>();

    Map<String, Object> beans = new HashMap<String, Object>();

    Map<String, Object> handlerMap = new HashMap<String, Object>();

    Properties properties = null;

    private static String HANDLERADAPTER = "handlerAdapterServiceImpl";

    public DispatcherServlet() {

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 获取到请求路径 /simple-springmvc/test/query
        String uri = req.getRequestURI();
        //
        String context = req.getContextPath();

        String path = uri.replace(context, "");
        // 根据请求路径获取要执行到方法
        Method method = (Method) handlerMap.get(path);

        // 拿到控制类
        SimpleController instance = (SimpleController) beans.get("/" + path.split("/")[1]);

        // 处理器
        IHandlerAdapterService handler = (IHandlerAdapterService) beans.get(HANDLERADAPTER);

        Object[] args = handler.handler(req, resp, method, beans);

        try {
            method.invoke(instance, args);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doPost(req, resp);
    }

    @Override
    public void init(ServletConfig config) throws ServletException {

        // 1、根据一个基础包进行扫描，扫描里面到子包以及子包下到类
        this.scanPackage("com.variflight");
        classNames.forEach(key -> {
            System.out.println(key);
        });

        // 2、将扫描出来到类进行实例化
        this.instance();
        beans.forEach((key, value) -> {
            System.out.println(key + ":" + value);
        });

        // 3、依赖注入，把service层实例注入到controller
        this.ioc();

        // 4、建立一个path与method的映射关系
        this.handlerMapping();
        handlerMap.forEach((key, value) -> {
            System.out.println(key + ":" + value);
        });
    }

    private void scanPackage(String basePackage) {
        // 扫描编译好的类路径下所有的类
        URL url = this.getClass().getClassLoader().getResource("/" + replaceTo(basePackage));

        String fileStr = url.getFile();

        File file = new File(fileStr);
        // 拿到basePackage下的文件夹
        String[] filesStr = file.list();

        for (String path : filesStr) {
            // 拿到basePackage + path下所有class类
            File filePath = new File(fileStr + path);

            if (filePath.isDirectory()) {
                scanPackage(basePackage + "." + path);
            } else {
                classNames.add(basePackage + "." + filePath.getName());
            }
        }
    }

    private String replaceTo(String basePackage) {

        return basePackage.replaceAll("\\.", "/");
    }

    private void instance() {
        if (classNames.size() <= 0) {
            System.out.println("包扫描失败");
            return;
        }
        classNames.forEach((key) -> {
           String cn = key.replace(".class", "");
            try {
                Class<?> clazz = Class.forName(cn);
                if (clazz.isAnnotationPresent(VariController.class)) {
                    VariController controller = clazz.getAnnotation(VariController.class);
                    Object instance = clazz.newInstance();

                    VariRequestMapping requestMapping = clazz.getAnnotation(VariRequestMapping.class);
                    String rmvalue = requestMapping.value();
                    beans.put(rmvalue, instance);
                } else if (clazz.isAnnotationPresent(VariService.class)) {
                    VariService service = clazz.getAnnotation(VariService.class);
                    Object instance = clazz.newInstance();
                    beans.put(service.value(), instance);
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            }
        });
    }

    // 初始化ioc容器
    private void ioc() {
        if (beans.entrySet().size() <= 0) {
            System.out.println("没有类都实例化");
            return;
        }

        beans.forEach((key, val) -> {
            // 获取bean实例
            Object instance = val;
            // 获取类，用来判断类里声明了哪些注解
            Class<?> clazz = instance.getClass();
            if (clazz.isAnnotationPresent(VariController.class)) {
                // 拿到类里面都属性
                Field[] fields = clazz.getDeclaredFields();
                // 判断是否声明了自动装配（依赖注入）注解
                for (Field field : fields) {
                    if (field.isAnnotationPresent(VariQualifier.class)) {
                        VariQualifier qualifier = field.getAnnotation(VariQualifier.class);
                        // 拿到@VariQualifier("testServiceImpl")里面指定要注入的bean名字testServiceImpl
                        String value = qualifier.value();
                        field.setAccessible(true);

                        // 从map容器中获取testServiceImpl对应的bean，并注入实例控制层bean，解决依赖注入
                        try {
                            field.set(instance, beans.get(value));
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }
                    } else {
                        continue;
                    }
                }
            }
        });
    }

    private void handlerMapping() {
        if (beans.entrySet().size() <= 0) {
            System.out.println("没有类的实例化");
            return;
        }

        beans.forEach((key, value) -> {
            Object instance = value;

            Class<?> clazz = instance.getClass();
            // 拿所有controller的类
            if (clazz.isAnnotationPresent(VariController.class)) {

                VariRequestMapping requestMapping = clazz.getAnnotation(VariRequestMapping.class);

                // 获取Controller类上面的VariRequestMapping注解里的请求路径
                String classPath = requestMapping.value();
                // 获取控制类里的所有方法
                Method[] methods = clazz.getMethods();
                // 获取方法上面的VariRequestMapping注解里的请求路径
                for (Method method : methods) {
                    if (method.isAnnotationPresent(VariRequestMapping.class)) {
                        VariRequestMapping methodrm = method.getAnnotation(VariRequestMapping.class);
                        String methodPath = methodrm.value();
                        // 将方法上与路径建立映射关系
                        handlerMap.put(classPath + methodPath, method);
                    } else {
                        continue;
                    }
                }
            }
        });
    }
}
