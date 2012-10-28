package ch.opentrainingcenter.client.model.navigation;

import java.util.List;

import ch.opentrainingcenter.client.model.KalenderWoche;

public interface INavigationParent {

    String getName();

    /**
     * @return eine sortierte Liste. Der neuste Eintrag erscheint als erstes.
     */
    List<INavigationItem> getChilds();

    void add(INavigationItem item);

    KalenderWoche getKalenderWoche();

    void remove(INavigationItem item);
}
