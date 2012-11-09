package ch.opentrainingcenter.client.views.planung;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.swt.graphics.Color;

import ch.opentrainingcenter.client.PreferenceConstants;
import ch.opentrainingcenter.client.helper.ColorFromPreferenceHelper;

public class ValueComparableColumnLabelProvider extends ColumnLabelProvider {

    private final Integer geplant;
    private final Integer effektive;
    private final String value;
    private final Color erfuellt;
    private final Color nichtErfuellt;

    public ValueComparableColumnLabelProvider(final String value) {
        this(value, null, null, null);
    }

    public ValueComparableColumnLabelProvider(final String value, final Integer geplant, final Integer effektive, final IPreferenceStore store) {
        this.value = value;
        this.geplant = geplant;
        this.effektive = effektive;
        erfuellt = ColorFromPreferenceHelper.getSwtColor(store, PreferenceConstants.ZIEL_ERFUELLT_COLOR);
        nichtErfuellt = ColorFromPreferenceHelper.getSwtColor(store, PreferenceConstants.ZIEL_NICHT_ERFUELLT_COLOR);
    }

    @Override
    public String getText(final Object element) {
        return value;
    }

    @Override
    public Color getBackground(final Object element) {
        if (geplant == null || effektive == null) {
            return super.getBackground(element);
        } else {
            if (geplant.intValue() > effektive.intValue()) {
                return nichtErfuellt;
            } else {
                return erfuellt;
            }
        }
    }
}
