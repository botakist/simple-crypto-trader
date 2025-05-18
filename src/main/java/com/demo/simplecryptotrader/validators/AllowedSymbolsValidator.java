package com.demo.simplecryptotrader.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Set;

public class AllowedSymbolsValidator implements ConstraintValidator<AllowedSymbols, String> {
    private static final Set<String> ALLOWED_SYMBOLS = Set.of("BTCUSDT", "ETHUSDT");
    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        return s == null || ALLOWED_SYMBOLS.contains(s);
    }
}
