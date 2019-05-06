package com.variflight.annotation;

import java.lang.annotation.*;

/**
 * @author XieYufeng
 * @ClassName: VariRequestParam
 * @description:
 * @date 2019/5/5 17:12
 */
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface VariRequestParam {

    String value() default "";
}
