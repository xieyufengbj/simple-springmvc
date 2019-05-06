package com.variflight.annotation;

import java.lang.annotation.*;

/**
 * @author XieYufeng
 * @ClassName: VariRequestHeader
 * @description:
 * @date 2019/5/5 17:11
 */
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface VariRequestHeader {

    String value() default "";
}
