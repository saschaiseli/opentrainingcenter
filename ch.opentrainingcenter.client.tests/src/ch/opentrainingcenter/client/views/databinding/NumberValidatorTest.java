package ch.opentrainingcenter.client.views.databinding;

import org.eclipse.core.databinding.validation.ValidationStatus;
import org.eclipse.core.runtime.IStatus;
import org.junit.Before;
import org.junit.Test;

import ch.opentrainingcenter.client.Messages;
import static org.junit.Assert.assertEquals;

public class NumberValidatorTest {
    private static final double MIN = 0;
    private static final double MAX = 100;
    private static final String JUNIT_LEER = "junit leer"; //$NON-NLS-1$
    private NumberValidator validator;

    @Before
    public void before() {
        validator = new NumberValidator(MIN, MAX, JUNIT_LEER);
    }

    @Test
    public void testValidateNull() {
        final IStatus status = validator.validate(null);

        assertEquals("Leer text muss im Status sein", JUNIT_LEER, status.getMessage()); //$NON-NLS-1$
    }

    @Test
    public void testValidateLeer() {
        final IStatus status = validator.validate(""); //$NON-NLS-1$

        assertEquals("Leer text muss im Status sein", JUNIT_LEER, status.getMessage()); //$NON-NLS-1$
    }

    @Test
    public void testValidateZuKlein() {
        final IStatus status = validator.validate(-1.0d);

        assertEquals("zu klein", Messages.NumberValidator0 + MIN + Messages.NumberValidator1, status.getMessage()); //$NON-NLS-1$
    }

    @Test
    public void testValidateZuGross() {
        final IStatus status = validator.validate(1000.0d);

        assertEquals("zu gross", Messages.NumberValidator2 + MAX + Messages.NumberValidator3, status.getMessage()); //$NON-NLS-1$
    }

    @Test
    public void testValidateOk() {
        final IStatus status = validator.validate(10.0d);

        assertEquals("okey status", ValidationStatus.OK_STATUS, status); //$NON-NLS-1$
    }
}
