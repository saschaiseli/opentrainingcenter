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

import org.junit.Before;
import org.junit.Test;

import ch.opentrainingcenter.core.helper.TimeHelper;
import ch.opentrainingcenter.transfer.ITraining;
import ch.opentrainingcenter.transfer.factory.CommonTransferFactory;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@SuppressWarnings("nls")
public class TrainingLabelProviderTest {

    TrainingLabelProvider provider;

    @Before
    public void setUp() {
        provider = new TrainingLabelProvider();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetTextObject() {
        final Object training = new Object();

        provider.getText(training);
    }

    @Test
    public void testGetTextFromTrainingNote() {
        final Object training = createTraining(2_000_000_000L, 10123, "");

        final String result = provider.getText(training);

        assertNotNull(result);
        assertEquals(TimeHelper.convertDateToString(2_000_000_000L) + " 10.123km", result);
    }

    @Test
    public void testGetTextFromTrainingMitNote() {
        final String note = "blabla";
        final Object training = createTraining(2_000_000_000L, 10123, note);

        final String result = provider.getText(training);

        assertNotNull(result);
        assertEquals(TimeHelper.convertDateToString(2_000_000_000L) + " 10.123km " + note, result);
    }

    private ITraining createTraining(final long dateOfStart, final double distance, final String note) {
        final ITraining training = CommonTransferFactory.createTraining(dateOfStart, 42, distance, 123, 180, 12);
        training.setNote(note);
        return training;
    }

}
