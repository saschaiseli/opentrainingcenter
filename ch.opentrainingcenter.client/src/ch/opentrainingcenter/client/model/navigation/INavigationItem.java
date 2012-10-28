package ch.opentrainingcenter.client.model.navigation;

import java.util.Date;

/**
 * Item welches im Navigationsbaum dargestellt werden kann.
 * 
 * @author sascha
 * 
 */
public interface INavigationItem extends Comparable<INavigationItem> {
    String getName();

    Date getDate();

    /**
     * Den String mit dem ein ImageIcon referenziert wird.
     */
    String getImage();
}
