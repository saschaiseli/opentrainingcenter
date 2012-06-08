package ch.opentrainingcenter.client.views.databinding;

import org.eclipse.core.databinding.validation.IValidator;
import org.eclipse.core.databinding.validation.ValidationStatus;
import org.eclipse.core.runtime.IStatus;

import ch.opentrainingcenter.client.Messages;

public class NumberValidator implements IValidator {

    private final double max;
    private final double min;
    private final String messageWennLeer;

    public NumberValidator(final double min, final double max, final String messageWennLeer) {
        this.min = min;
        this.max = max;
        this.messageWennLeer = messageWennLeer;
    }

    @Override
    public IStatus validate(final Object value) {
        if (value != null) {
            final String val = value.toString();
            try {
                final Double d = Double.parseDouble(val);
                if (d < min) {
                    return ValidationStatus.error(Messages.NumberValidator0 + min + Messages.NumberValidator1);
                } else if (d > max) {
                    return ValidationStatus.error(Messages.NumberValidator2 + max + Messages.NumberValidator3);
                } else {
                    return ValidationStatus.OK_STATUS;
                }
            } catch (final NumberFormatException nfe) {
                return ValidationStatus.error(messageWennLeer);
            }
        }

        return ValidationStatus.error(messageWennLeer);
    }
}
