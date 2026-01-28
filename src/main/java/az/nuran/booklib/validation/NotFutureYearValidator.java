package az.nuran.booklib.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.Year;

public class NotFutureYearValidator implements ConstraintValidator<NotFutureYear, Integer> {

    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        return isNotInFuture(value);
    }

    private boolean isNotInFuture(Integer year) {
        return year <= Year.now().getValue();
    }
}
