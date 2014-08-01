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

package ch.opentrainingcenter.transfer;

/**
 * Infos ueber eine Runde.
 */
public interface ILapInfo {

    int getId();

    void setId(int id);

    /**
     * @return die Nummer der Runde (1. Runde ist 0)
     */
    int getLap();

    void setLap(int lap);

    /**
     * Die Distanz Ursprung in Meter [m]. An diesem Punkt BEGINNT die Lap.
     * 
     * <pre>
     * 
     *                           LapInfo
     * |----------------------<--------->-----|
     * 0m                   140m       180m
     * 
     *                     Start       End
     * 
     * 
     * </pre>
     */
    int getStart();

    void setStart(int start);

    /**
     * Die Distanz Ursprung in Meter [m]. An diesem Punkt ENDET die Lap.
     * 
     * <pre>
     * 
     *                           LapInfo
     * |----------------------<--------->-----|
     * 0m                   140m       180m
     * 
     *                     Start       End
     * 
     * 
     * </pre>
     */
    int getEnd();

    void setEnd(int end);

    /**
     * @return Zeit in Millisekunden [ms]
     */
    long getTime();

    void setTime(long time);

    /**
     * @return durchschnittliche Herzfrequenz [bpm]
     */
    int getHeartBeat();

    void setHeartBeat(int heartBeat);

    /**
     * @return durchschnittliche Pace [min/km]
     */
    String getPace();

    void setPace(String pace);

    /**
     * @return durchschnittliche Geschwindigkeit [km/h]
     */
    String getGeschwindigkeit();

    void setGeschwindigkeit(String geschwindigkeit);

    ITraining getTraining();

    void setTraining(ITraining training);
}
