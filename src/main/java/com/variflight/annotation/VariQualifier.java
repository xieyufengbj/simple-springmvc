package com.variflight.annotation;

import java.lang.annotation.*;

/**
 * @author XieYufeng
 * @ClassName: VariQualifier
 * @description:
 * @date 2019/5/5 17:10
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface VariQualifier {

    String value() default "";
}
