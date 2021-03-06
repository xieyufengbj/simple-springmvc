package com.variflight.annotation;

import java.lang.annotation.*;

/**
 * @author XieYufeng
 * @ClassName: VariRequestMapping
 * @description:
 * @date 2019/5/5 17:11
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface VariRequestMapping {

    String value() default "";
}
