package ch.opentrainingcenter.client.views.einstellungen;

import org.eclipse.core.databinding.validation.IValidator;
import org.eclipse.core.databinding.validation.ValidationStatus;
import org.eclipse.core.runtime.IStatus;

import ch.opentrainingcenter.i18n.Messages;

public class SportlerValidator implements IValidator {

    private static final int MINIMALE_LAENGE = 4;
    private final int minLength;

    public SportlerValidator(final int minLength) {
        this.minLength = minLength;

    }

    @Override
    public IStatus validate(final Object value) {
        if (value instanceof String) {
            final String name = String.valueOf(value);
            if (name == null || name.length() < MINIMALE_LAENGE) {
                return ValidationStatus.error(Messages.SportlerValidator1 + minLength + Messages.SportlerValidator2);
            }
        }
        return ValidationStatus.ok();
    }
}
