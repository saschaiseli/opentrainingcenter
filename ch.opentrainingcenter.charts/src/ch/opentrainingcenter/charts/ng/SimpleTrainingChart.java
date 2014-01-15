/**
 *    OpenTrainingCenter
 *
 *    Copyright (C) 2014 Sascha Iseli sascha.iseli(at)gmx.ch
 *
 *    This program is free software: you can redistribute it and/or modify
 *    it under the terms of the GNU General Public License as published by
 *    the Free Software Foundation, either version 3 of the License, or
 *    (at your option) any later version.
 *
 *    This program is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    GNU General Public License for more details.
 *
 *    You should have received a copy of the GNU General Public License
 *    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package ch.opentrainingcenter.charts.ng;

import ch.opentrainingcenter.i18n.Messages;


public enum SimpleTrainingChart {

    /**
     * Distanz = f(zeit)
     */
    DISTANZ(0, Messages.SimpleTrainingChart_0, Messages.SimpleTrainingChart_1, Messages.SimpleTrainingChart_2, Messages.SimpleTrainingChart_3), //
    /**
     * Herzfrequenz = f(zeit)
     */
    HERZ(1, Messages.SimpleTrainingChart_4, Messages.SimpleTrainingChart_5, Messages.SimpleTrainingChart_6, Messages.SimpleTrainingChart_7);//

    private final int index;
    private final String title;
    private final String xAchse;
    private final String yAchse;
    private final String comboTitle;

    private SimpleTrainingChart(final int index, final String title, final String xAchse, final String yAchse, final String comboTitle) {
        this.index = index;
        this.title = title;
        this.xAchse = xAchse;
        this.yAchse = yAchse;
        this.comboTitle = comboTitle;
    }

    public int getIndex() {
        return index;
    }

    public String getTitle() {
        return title;
    }

    public String getxAchse() {
        return xAchse;
    }

    public String getyAchse() {
        return yAchse;
    }

    public String getComboTitle() {
        return comboTitle;
    }

    /**
     * @return den ChartFilter mit dem angegebenen Index.
     */
    public static SimpleTrainingChart getByIndex(final int index) {
        switch (index) {
        case 0:
            return SimpleTrainingChart.DISTANZ;
        case 1:
            return SimpleTrainingChart.HERZ;
        default:
            throw new IllegalArgumentException(String.format("SimpleTrainingChart mit Index %s ist unbekannt", index)); //$NON-NLS-1$
        }
    }

    /**
     * @return String Array mit allen ChartFilter Namen.
     */
    public static String[] items() {
        final String[] result = new String[SimpleTrainingChart.values().length];
        for (final SimpleTrainingChart item : SimpleTrainingChart.values()) {
            result[item.index] = item.comboTitle;
        }
        return result;
    }
}