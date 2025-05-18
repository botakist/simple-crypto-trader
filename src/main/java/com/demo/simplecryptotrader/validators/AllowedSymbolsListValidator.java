package com.demo.simplecryptotrader.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.List;
import java.util.Set;

public class AllowedSymbolsListValidator implements ConstraintValidator<AllowedSymbols, List<String>> {
    private static final Set<String> ALLOWED_SYMBOLS = Set.of("BTCUSDT", "ETHUSDT");


    @Override
    public boolean isValid(List<String> stringList, ConstraintValidatorContext constraintValidatorContext) {
        if (stringList == null || stringList.isEmpty()) {
            return true;
        }
        return ALLOWED_SYMBOLS.containsAll(stringList);
    }
}
