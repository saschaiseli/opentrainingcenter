package ch.iseli.sportanalyzer.client.views.ahtlete;

import org.eclipse.core.databinding.validation.IValidator;
import org.eclipse.core.databinding.validation.ValidationStatus;
import org.eclipse.core.runtime.IStatus;

import ch.iseli.sportanalyzer.client.Messages;
import ch.iseli.sportanalyzer.db.DatabaseAccessFactory;
import ch.opentrainingcenter.transfer.IAthlete;

public class SportlerValidator implements IValidator {

    private final int minLength;

    public SportlerValidator(final int minLength) {
        this.minLength = minLength;

    }

    @Override
    public IStatus validate(final Object value) {
        if (value instanceof String) {
            final String name = String.valueOf(value);
            if (name != null && name.length() >= 4) {
                // in db schauen ob es den user bereits gibt
                final IAthlete athlete = DatabaseAccessFactory.getDatabaseAccess().getAthlete(name);
                if (athlete != null) {
                    return ValidationStatus.error(Messages.SportlerValidator_0);
                } else {
                    return ValidationStatus.OK_STATUS;
                }
            } else {
                return ValidationStatus.error(Messages.SportlerValidator_1 + minLength + Messages.SportlerValidator_2);
            }
        }
        return ValidationStatus.error(Messages.SportlerValidator_3);
    }

}
