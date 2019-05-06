package com.variflight.annotation;

import java.lang.annotation.*;

/**
 * @author XieYufeng
 * @ClassName: VariController
 * @description:
 * @date 2019/5/5 17:09
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface VariController {

    String value() default "";
}
