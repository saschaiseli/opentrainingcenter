package ch.opentrainingcenter.model.navigation;

import java.util.Date;

/**
 * Item welches im Navigationsbaum dargestellt werden kann.
 * 
 * @author sascha
 * 
 */
public interface INavigationItem extends Comparable<INavigationItem> {
    /**
     * @return den String welcher im Tree dargestellt wird.
     */
    String getName();

    Date getDate();

    /**
     * Den String mit dem ein ImageIcon referenziert wird.
     */
    String getImage();
}
