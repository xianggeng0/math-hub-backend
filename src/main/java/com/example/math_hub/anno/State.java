package com.example.math_hub.anno;
import static  java.lang.annotation.ElementType.*;
import static  java.lang.annotation.RetentionPolicy.RUNTIME;

import com.example.math_hub.Validation.StateValidation;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.NotEmpty;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = {StateValidation.class})
@Target({FIELD})
@Retention(RUNTIME)
public @interface State {
    String message() default "参数只能是已发布或草稿";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
