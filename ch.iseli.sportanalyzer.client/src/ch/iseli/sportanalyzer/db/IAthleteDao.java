package ch.iseli.sportanalyzer.db;

import java.util.List;

import ch.opentrainingcenter.transfer.IAthlete;

public interface IAthleteDao {

    IAthlete getAthleteByName(String name);

    List<IAthlete> getAllAthletes();

}
