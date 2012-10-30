package ch.opentrainingcenter.client.charts;

import java.util.List;
import java.util.Map;

import ch.opentrainingcenter.client.model.ISimpleTraining;

public interface IStatistikCreator {

    /**
     * @param all
     *            Alle Trainings
     * @return Eine Map mit einer Liste von Trainings welche einem Jahr
     *         zugeordnet sind. Der Key der Map ist das Jahr, der Value eine
     *         Liste mit den Trainings dazu.
     */
    Map<Integer, List<ISimpleTraining>> getTrainingsProJahr(List<ISimpleTraining> allTrainings);

    /**
     * Erster Key ist das Jahr. Zweiter Key ist der Monat, Value ist eine Liste
     * der Trainings der entsprechender KW.
     */
    Map<Integer, Map<Integer, List<ISimpleTraining>>> getTrainingsProMonat(List<ISimpleTraining> allTrainings);

    /**
     * Erster Key ist das Jahr. Zweiter Key ist die KW, Value ist eine Liste der
     * Trainings der entsprechender KW.
     */
    Map<Integer, Map<Integer, List<ISimpleTraining>>> getTrainingsProWoche(List<ISimpleTraining> allTrainings);

    List<ISimpleTraining> getTrainingsProTag(List<ISimpleTraining> allTrainings);
}
