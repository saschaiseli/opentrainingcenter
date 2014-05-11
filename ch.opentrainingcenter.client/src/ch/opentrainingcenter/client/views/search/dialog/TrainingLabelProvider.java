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

package ch.opentrainingcenter.client.views.search.dialog;

import org.eclipse.jface.viewers.LabelProvider;

import ch.opentrainingcenter.core.helper.DistanceHelper;
import ch.opentrainingcenter.core.helper.TimeHelper;
import ch.opentrainingcenter.transfer.ITraining;

public class TrainingLabelProvider extends LabelProvider {
    @Override
    public String getText(final Object element) {
        if (element instanceof ITraining) {
            final ITraining training = (ITraining) element;
            final StringBuilder str = new StringBuilder();
            str.append(TimeHelper.convertDateToString(training.getDatum()));
            str.append(' ');
            str.append(DistanceHelper.roundDistanceFromMeterToKmMitEinheit(training.getLaengeInMeter()));
            final String note = training.getNote();
            if (note != null && note.length() > 0) {
                str.append(' ');
                str.append(note);
            }
            return str.toString();
        } else {
            throw new IllegalArgumentException(String.format("Der Typ %s wird in diesem LabelProvider nicht unterstützt", element)); //$NON-NLS-1$
        }
    }
}
