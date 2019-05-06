package com.variflight.controller;

import com.variflight.annotation.VariController;
import com.variflight.annotation.VariQualifier;
import com.variflight.annotation.VariRequestMapping;
import com.variflight.annotation.VariRequestParam;
import com.variflight.service.TestService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author XieYufeng
 * @ClassName: SimpleController
 * @description:
 * @date 2019/5/5 17:32
 */
@VariController
@VariRequestMapping("/test")
public class SimpleController {

    @VariQualifier("testServiceImpl")
    private TestService testService;

    @VariRequestMapping("/query")
    public void query(HttpServletRequest request, HttpServletResponse response,
                      @VariRequestParam("name") String name,
                      @VariRequestParam("age") String age) {

        try {
            PrintWriter printWriter = response.getWriter();
            String result = testService.query(name, age);
            printWriter.write(result);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @VariRequestMapping("/insert")
    public void insert(HttpServletRequest request, HttpServletResponse response) {

        try {
            PrintWriter printWriter = response.getWriter();
            String result = testService.insert("insert");
            printWriter.write(result);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @VariRequestMapping("/update")
    public void update(HttpServletRequest request, HttpServletResponse response) {

        try {
            PrintWriter printWriter = response.getWriter();
            String result = testService.update("update");
            printWriter.write(result);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
