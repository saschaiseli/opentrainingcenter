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

package ch.opentrainingcenter.client.views.dialoge;

import org.eclipse.jface.viewers.LabelProvider;

import ch.opentrainingcenter.core.helper.DistanceHelper;
import ch.opentrainingcenter.core.helper.TimeHelper;
import ch.opentrainingcenter.transfer.ITraining;

public class TrainingLabelProvider extends LabelProvider {
    @Override
    public String getText(final Object element) {
        if (element instanceof ITraining) {
            final ITraining training = (ITraining) element;
            final String datum = TimeHelper.convertDateToString(training.getDatum());
            final String distanz = DistanceHelper.roundDistanceFromMeterToKmMitEinheit(training.getLaengeInMeter());
            final String dauer = TimeHelper.convertTimeToString(1000L * (long) training.getDauer());
            String format;
            if (distanz.length() < 9) {
                format = "%s     %2$9s    %3$-5s"; //$NON-NLS-1$
            } else {
                format = "%s    %2$9s    %3$-5s"; //$NON-NLS-1$
            }
            return String.format(format, datum, distanz, dauer);
        } else {
            throw new IllegalArgumentException(String.format("Der Typ %s wird in diesem LabelProvider nicht unterstützt", element)); //$NON-NLS-1$
        }
    }
}
