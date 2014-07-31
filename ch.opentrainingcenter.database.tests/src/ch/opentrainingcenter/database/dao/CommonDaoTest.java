/**
 *    OpenTrainingCenter
 *
 *    Copyright (C) 2013 Sascha Iseli sascha.iseli(at)gmx.ch
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

package ch.opentrainingcenter.database.dao;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import ch.opentrainingcenter.core.cache.TrainingCache;
import ch.opentrainingcenter.transfer.IAthlete;
import ch.opentrainingcenter.transfer.ITraining;

public class CommonDaoTest {
    CommonDao dao;
    private TrainingCache trainingCache;
    private TrainingDao trainingDao;

    @Before
    public void setUp() {
        trainingDao = mock(TrainingDao.class);
        trainingCache = mock(TrainingCache.class);
        dao = new CommonDao(null, null, null, null, null, trainingDao, trainingCache);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testGetAllTrainingsIAthlete_Cache_Empty() {
        final IAthlete athlete = mock(IAthlete.class);
        when(trainingCache.isEmpty()).thenReturn(true);

        dao.getAllTrainings(athlete);

        verify(trainingDao).getAllTrainings(athlete);
        verify(trainingCache).addAll((List<ITraining>) any());
        verify(trainingCache).getAll(athlete);
    }

    @Test
    public void testGetAllTrainingsIAthlete_Cache_Not_Empty() {
        final IAthlete athlete = mock(IAthlete.class);
        when(trainingCache.size()).thenReturn(2);

        dao.getAllTrainings(athlete);

        verify(trainingCache).getAll(athlete);
        verifyZeroInteractions(trainingDao);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testGetAllTrainingsIAthlete_Limited_Cache_Empty() {
        final IAthlete athlete = mock(IAthlete.class);
        when(trainingCache.isEmpty()).thenReturn(true);

        dao.getAllTrainings(athlete);

        verify(trainingDao).getAllTrainings(athlete);
        verify(trainingCache).addAll((List<ITraining>) any());
        verify(trainingCache).getAll(athlete);
    }

    @Test
    public void testRemove() {
        dao.removeTrainingByDate(1000L);

        verify(trainingDao).removeTrainingByDate(1000L);
        verifyNoMoreInteractions(trainingDao);
        verify(trainingCache).remove(1000L);
    }

    @Test
    public void testSave() {
        final ITraining training = mock(ITraining.class);
        when(training.getDatum()).thenReturn(42L);

        dao.saveOrUpdate(training);

        verify(trainingDao).saveOrUpdate(training);

        final List<ITraining> models = new ArrayList<>();
        models.add(training);
        verify(trainingCache).addAll(models);
    }

    @Test
    public void testUpdateTraining() {
        final ITraining training = mock(ITraining.class);
        when(training.getDatum()).thenReturn(42L);
        final ITraining trainingWithNewType = mock(ITraining.class);

        when(trainingDao.getTrainingByDate(42L)).thenReturn(trainingWithNewType);
        dao.updateTrainingType(training, 2);

        verify(trainingDao).updateTrainingType(training, 2);
        verify(trainingDao).getTrainingByDate(42L);

        final List<ITraining> models = new ArrayList<>();
        models.add(trainingWithNewType);
        verify(trainingCache).addAll(models);
    }

    @Test
    public void testUpdateTrainingRoute() {
        final ITraining training = mock(ITraining.class);
        when(training.getDatum()).thenReturn(42L);
        final ITraining trainingWithNewRoute = mock(ITraining.class);
        when(trainingDao.getTrainingByDate(42L)).thenReturn(trainingWithNewRoute);

        dao.updateTrainingRoute(training, 2);

        verify(trainingDao).updateTrainingRoute(training, 2);
        verify(trainingDao).getTrainingByDate(42L);

        final List<ITraining> models = new ArrayList<>();
        models.add(trainingWithNewRoute);
        verify(trainingCache).addAll(models);
    }

    @Test
    public void testGetTrainingById_vom_cache() {
        final ITraining training = mock(ITraining.class);
        when(training.getDatum()).thenReturn(42L);
        when(trainingCache.get(42L)).thenReturn(training);

        dao.getTrainingById(42L);

        verify(trainingCache).get(42L);
        verifyNoMoreInteractions(trainingCache);
        verifyZeroInteractions(trainingDao);
    }

    @Test
    public void testGetTrainingById_von_db() {
        final ITraining training = mock(ITraining.class);
        when(training.getDatum()).thenReturn(42L);
        when(trainingDao.getTrainingByDate(42L)).thenReturn(training);
        when(trainingCache.get(42L)).thenReturn(null);

        dao.getTrainingById(42L);

        verify(trainingCache).get(42L);
        verify(trainingDao).getTrainingByDate(42L);

        final List<ITraining> models = new ArrayList<>();
        models.add(training);
        verify(trainingCache).addAll(models);
    }

    @Test
    public void testGetTrainingById_von_db_aber_nicht_gefunden() {
        final ITraining training = mock(ITraining.class);
        when(training.getDatum()).thenReturn(42L);
        when(trainingDao.getTrainingByDate(42L)).thenReturn(null);
        when(trainingCache.get(42L)).thenReturn(null);

        dao.getTrainingById(42L);

        verify(trainingCache).get(42L);
        verify(trainingDao).getTrainingByDate(42L);
        verifyNoMoreInteractions(trainingDao);
        verifyNoMoreInteractions(trainingCache);
    }
}
