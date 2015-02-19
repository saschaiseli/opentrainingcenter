package ch.opentrainingcenter.core.charts;

import java.util.List;
import java.util.Map;

import ch.opentrainingcenter.transfer.ITraining;

public interface IStatistikCreator {

    /**
     * @param all
     *            Alle Trainings
     * @return Eine Map mit einer Liste von Trainings welche einem Jahr
     *         zugeordnet sind. Der Key der Map ist das Jahr, der Value eine
     *         Liste mit den Trainings dazu.
     */
    Map<Integer, List<ITraining>> getTrainingsProJahr(List<ITraining> allTrainings);

    /**
     * Erster Key ist das Jahr. Zweiter Key ist der Monat, Value ist eine Liste
     * der Trainings der entsprechender KW.
     */
    Map<Integer, Map<Integer, List<ITraining>>> getTrainingsProMonat(List<ITraining> allTrainings);

    /**
     * Erster Key ist das Jahr. Zweiter Key ist die KW, Value ist eine Liste der
     * Trainings der entsprechender KW.
     */
    Map<Integer, Map<Integer, List<ITraining>>> getTrainingsProWoche(List<ITraining> allTrainings);

    List<ITraining> getTrainingsProTag(List<ITraining> allTrainings);
}
