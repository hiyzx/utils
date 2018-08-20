package com.zero.util.file.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author yezhaoxing
 * @description 配置excel格式化注解
 * @since 2018/6/13
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CellFormat {

    String value() default "";

    String filterClass() default "";

    String filterMethod() default "";

    String datePatten() default "";

    String firstTitle() default "";
}
