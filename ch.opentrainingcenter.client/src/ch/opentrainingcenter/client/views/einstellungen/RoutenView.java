package ch.opentrainingcenter.client.views.einstellungen;

import java.util.Collection;

import org.apache.log4j.Logger;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.part.ViewPart;

import ch.opentrainingcenter.client.cache.AthleteCache;
import ch.opentrainingcenter.core.cache.IRecordListener;
import ch.opentrainingcenter.transfer.IAthlete;

public class RoutenView extends ViewPart implements IRecordListener<IAthlete> {

    public static final String ID = "ch.opentrainingcenter.client.views.einstellungen.routen"; //$NON-NLS-1$

    private static final Logger LOGGER = Logger.getLogger(RoutenView.class);

    private static final AthleteCache athleteCache = AthleteCache.getInstance();

    private Composite parent;

    private FormToolkit toolkit;

    private ScrolledForm form;

    @Override
    public void createPartControl(final Composite parent) {
        LOGGER.info("create Route view"); //$NON-NLS-1$
        this.parent = parent;

        athleteCache.addListener(this);

        toolkit = new FormToolkit(this.parent.getDisplay());

        form = toolkit.createScrolledForm(this.parent);
        toolkit.decorateFormHeading(form.getForm());
        form.setText("Verwalten von Strecken");

    }

    @Override
    public void setFocus() {
    }

    @Override
    public void dispose() {
        super.dispose();
        athleteCache.removeListener(this);
    }

    @Override
    public void recordChanged(final Collection<IAthlete> entry) {

    }

    @Override
    public void deleteRecord(final Collection<IAthlete> entry) {

    }

}
