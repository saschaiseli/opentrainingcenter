package ch.opentrainingcenter.client.views.weeks;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

import ch.opentrainingcenter.client.Activator;
import ch.opentrainingcenter.client.views.ApplicationContext;
import ch.opentrainingcenter.core.PreferenceConstants;
import ch.opentrainingcenter.core.db.IDatabaseAccess;
import ch.opentrainingcenter.core.service.IDatabaseService;
import ch.opentrainingcenter.transfer.IAthlete;
import ch.opentrainingcenter.transfer.Sport;

public class MonthlyWeekOverview extends ViewPart {

    public static final String ID = "ch.opentrainingcenter.client.weeks.monthlyOverview"; //$NON-NLS-1$

    List<MonthWeekTabItem> overviews = new ArrayList<>();

    private final Sport prefferedSport;

    public MonthlyWeekOverview() {
        final IDatabaseService service = (IDatabaseService) PlatformUI.getWorkbench().getService(IDatabaseService.class);
        final IDatabaseAccess databaseAccess = service.getDatabaseAccess();
        final IAthlete athlete = ApplicationContext.getApplicationContext().getAthlete();
        final IPreferenceStore store = Activator.getDefault().getPreferenceStore();
        prefferedSport = Sport.getByIndex(store.getInt(PreferenceConstants.CHART_SPORT));

        if (store.getBoolean(Sport.RUNNING.getMessage())) {
            overviews.add(new MonthWeekTabItem(Sport.RUNNING, athlete, databaseAccess));
        }
        if (store.getBoolean(Sport.BIKING.getMessage())) {
            overviews.add(new MonthWeekTabItem(Sport.BIKING, athlete, databaseAccess));
        }
        if (store.getBoolean(Sport.OTHER.getMessage())) {
            overviews.add(new MonthWeekTabItem(Sport.OTHER, athlete, databaseAccess));
        }
    }

    @Override
    public void createPartControl(final Composite parent) {
        final Composite container = new Composite(parent, SWT.NONE);
        GridLayoutFactory.swtDefaults().applyTo(container);
        GridDataFactory.swtDefaults().grab(true, true).align(SWT.FILL, SWT.FILL).applyTo(container);

        final CTabFolder folder = new CTabFolder(container, SWT.BORDER);
        GridLayoutFactory.swtDefaults().applyTo(folder);
        GridDataFactory.swtDefaults().grab(true, true).align(SWT.FILL, SWT.FILL).applyTo(folder);

        for (final MonthWeekTabItem labels : overviews) {
            labels.createPartControl(folder);
        }
        folder.setSelection(getPreferedSportIndex());
    }

    private int getPreferedSportIndex() {
        int index = 0;
        for (final MonthWeekTabItem monthWeekTabItem : overviews) {
            if (prefferedSport.equals(monthWeekTabItem.getSport())) {
                break;
            }
            index++;
        }
        return index;
    }

    @Override
    public void setFocus() {
        // do nothing
    }

}
