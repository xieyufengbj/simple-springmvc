package com.variflight.service.impl;

import com.variflight.annotation.VariService;
import com.variflight.service.TestService;

/**
 * @author XieYufeng
 * @ClassName: TestServiceImpl
 * @description:
 * @date 2019/5/5 17:33
 */
@VariService("testServiceImpl")
public class TestServiceImpl implements TestService {

    @Override
    public String query(String name, String age) {

        return "{name="+ name +", age="+ age +"}";
    }
    @Override
    public String insert(String param) {

        return "insert successful ...";
    }
    @Override
    public String update(String param) {

        return "update successful ...";
    }
}
