package ch.iseli.sportanalyzer.db;

import ch.opentrainingcenter.transfer.IAthlete;

public interface IAthleteDao {
    IAthlete getAthleteByName(String name);
}
