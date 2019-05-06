package com.variflight.service;

/**
 * @author XieYufeng
 * @ClassName: TestService
 * @description:
 * @date 2019/5/5 17:33
 */
public interface TestService {

    String query(String name, String age);

    String insert(String param);

    String update(String param);
}
