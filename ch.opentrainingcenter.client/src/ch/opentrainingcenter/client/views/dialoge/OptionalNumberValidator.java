package ch.opentrainingcenter.client.views.dialoge;

import org.eclipse.core.databinding.validation.IValidator;
import org.eclipse.core.databinding.validation.ValidationStatus;
import org.eclipse.core.runtime.IStatus;

import ch.opentrainingcenter.i18n.Messages;

public class OptionalNumberValidator implements IValidator {

    private final int min;
    private final int max;

    public OptionalNumberValidator(final int min, final int max) {
        this.min = min;
        this.max = max;
    }

    @Override
    public IStatus validate(final Object value) {
        if (value == null) {
            return ValidationStatus.OK_STATUS;
        }

        if (value instanceof Integer) {
            final String s = String.valueOf(value);
            if (s.matches("\\d*")) { //$NON-NLS-1$
                final int number = Integer.parseInt(s);
                if (number < min) {
                    return ValidationStatus.error(Messages.NumberValidator0 + min + Messages.NumberValidator1);
                } else if (number > max) {
                    return ValidationStatus.error(Messages.NumberValidator2 + max + Messages.NumberValidator3);
                } else {
                    return ValidationStatus.OK_STATUS;
                }
            }
        }
        return ValidationStatus.error(Messages.OptionalNumberValidator_0);
    }
}
