package ch.opentrainingcenter.client.action.job;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.joda.time.DateTime;

import ch.opentrainingcenter.core.db.IDatabaseAccess;
import ch.opentrainingcenter.model.navigation.INavigationItem;
import ch.opentrainingcenter.model.navigation.INavigationParent;
import ch.opentrainingcenter.transfer.IAthlete;
import ch.opentrainingcenter.transfer.ITraining;

/**
 * Wertet die Navigation Selection aus und
 */
public class NavigationSelectionSupport<T> {

    private final IDatabaseAccess databaseAccess;
    private final IAthlete athlete;
    private final Class<T> type;

    public NavigationSelectionSupport(final IDatabaseAccess databaseAccess, final IAthlete athlete, final Class<T> type) {
        this.databaseAccess = databaseAccess;
        this.athlete = athlete;
        this.type = type;
    }

    @SuppressWarnings("unchecked")
    public List<T> getSelectedTrainings(final List<?> objects) {
        final List<T> trainings = new ArrayList<>();
        for (final Object obj : objects) {
            if (obj instanceof Integer) {
                final DateTime von = new DateTime((int) obj, 1, 1, 0, 0);
                final DateTime bis = new DateTime((int) obj, 12, 31, 23, 59);
                if (type.isAssignableFrom(ITraining.class)) {
                    trainings.addAll((Collection<? extends T>) databaseAccess.getTrainingsByAthleteAndDate(athlete, von, bis));
                }
            }
            if (obj instanceof INavigationParent) {
                final INavigationParent parent = (INavigationParent) obj;
                final List<INavigationItem> childs = parent.getChilds();
                for (final INavigationItem child : childs) {
                    if (type.isAssignableFrom(child.getClass())) {
                        trainings.add(((T) child));
                    }
                }
            }
            if (type.isAssignableFrom(obj.getClass())) {
                trainings.add((T) obj);
            }
        }
        return trainings;
    }
}
