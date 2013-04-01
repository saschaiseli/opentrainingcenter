package ch.opentrainingcenter.transfer;

import java.util.List;

public interface IStrecke {

    IAthlete getAthlete();

    void setAthlete(IAthlete athelte);

    List<IStreckenPunkt> getStreckenPunkte();

    void setStreckenPunkte(List<IStreckenPunkt> streckenPunkte);
}
