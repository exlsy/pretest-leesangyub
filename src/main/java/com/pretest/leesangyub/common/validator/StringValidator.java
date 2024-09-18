package com.pretest.leesangyub.common.validator;


import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.ArrayList;
import java.util.List;


public class StringValidator implements ConstraintValidator<ValidateString, String> {

    private boolean nullPass = true;
    private List<String> valueList;

    @Override
    public void initialize(ValidateString constraintAnnotation) {
        valueList = new ArrayList<String>();
        for(String val : constraintAnnotation.acceptedValues()) {
            valueList.add(val.toUpperCase());
        }
        nullPass = constraintAnnotation.nullPass();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if(nullPass && value==null)
            return true;
        return (valueList!=null && value!=null && valueList.contains(value.toUpperCase()));
    }

}
