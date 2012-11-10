package ch.opentrainingcenter.model.navigation;

import java.util.Collection;

public interface IKalenderWocheNavigationModel {

    /**
     * Sortierte Liste. Der neuste Eintrag erscheint als erstes.
     */
    Collection<Integer> getParents();

    void addItems(Collection<? extends INavigationItem> items);

    void addItem(INavigationItem item);

    void removeItem(INavigationItem item);

    /**
     * Löscht alle Daten im Model.
     */
    void reset();

    Collection<INavigationParent> getWeeks(Integer jahr);
}
