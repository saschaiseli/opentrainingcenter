package ch.iseli.sportanalyzer.client.views.ahtlete;

import org.eclipse.core.databinding.validation.IValidator;
import org.eclipse.core.databinding.validation.ValidationStatus;
import org.eclipse.core.runtime.IStatus;

import ch.iseli.sportanalyzer.client.Messages;

public class NumberValidator implements IValidator {

    private final int max;
    private final int min;
    private final String messageWennLeer;

    public NumberValidator(final int min, final int max, final String messageWennLeer) {
        this.min = min;
        this.max = max;
        this.messageWennLeer = messageWennLeer;

    }

    @Override
    public IStatus validate(final Object value) {
        if (value instanceof Integer) {
            final String s = String.valueOf(value);
            if (s.matches("\\d*")) { //$NON-NLS-1$
                final int number = Integer.parseInt(s);
                if (number < min) {
                    return ValidationStatus.error(Messages.NumberValidator_0 + min + Messages.NumberValidator_1);
                } else if (number > max) {
                    return ValidationStatus.error(Messages.NumberValidator_2 + max + Messages.NumberValidator_3);
                } else {
                    return ValidationStatus.OK_STATUS;
                }
            }
        }
        return ValidationStatus.error(messageWennLeer);
    }
}
