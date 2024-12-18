package com.zhihu.zhihubackend.validation;

import com.zhihu.zhihubackend.anno.State;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class StateValidation implements ConstraintValidator<State, String> {

    /**
     *
     * @param s 将来要校验的数据
     * @param constraintValidatorContext
     * @return
     */
    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        // 提供校验规则
        if(s==null)
            return false;
        return s.equals("已发布") || s.equals("草稿");
    }
}
