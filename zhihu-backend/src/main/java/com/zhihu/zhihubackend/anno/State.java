package com.zhihu.zhihubackend.anno;

import com.zhihu.zhihubackend.validation.StateValidation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented //元注解：能写入帮助文档
@Target({ElementType.FIELD}) //元注解： 标识作用在哪些对象上
@Retention(RetentionPolicy.RUNTIME)  //元注解：标识在哪个阶段保留（运行时阶段）
@Constraint(validatedBy = {StateValidation.class})  // 指定提供校验规则的类
public @interface State {

    // 提供校验失败后的提示信息
    String message() default "state参数的值只能是已发布或者草稿";

    // 指定分组
    Class<?>[] groups() default {};

    //负载 获取到State注解的附加信息
    Class<? extends Payload>[] payload() default {};
}
