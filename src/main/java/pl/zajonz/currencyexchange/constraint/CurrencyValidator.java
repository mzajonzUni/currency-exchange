package pl.zajonz.currencyexchange.constraint;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import pl.zajonz.currencyexchange.model.AvailableCurrencies;

@RequiredArgsConstructor
public class CurrencyValidator implements ConstraintValidator<CurrencyConstraint, String> {

    private final AvailableCurrencies availableCurrencies;

    @Override
    public boolean isValid(String currency, ConstraintValidatorContext constraintValidatorContext) {
        if(currency.equals("PLN")){
            return true;
        }

        return availableCurrencies.getCurrencies().contains(currency);
    }
}
