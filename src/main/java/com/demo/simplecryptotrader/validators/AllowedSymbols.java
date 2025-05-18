package com.demo.simplecryptotrader.validators;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = {AllowedSymbolsValidator.class, AllowedSymbolsListValidator.class})
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface AllowedSymbols {
    String message() default "Invalid ticker symbol. only BTCUSDT or ETHUSDT allowed.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
