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

package ch.opentrainingcenter.transfer.impl;

import ch.opentrainingcenter.transfer.ILapInfo;
import ch.opentrainingcenter.transfer.ITraining;

public class LapInfo implements ILapInfo {

    private int id;
    private int lap;
    private int start;
    private int end;
    private long time;
    private int heartBeat;
    private String pace;
    private ITraining training;
    private String geschwindigkeit;

    public LapInfo() {

    }

    /**
     * Start und End beziehen sich immer auf die kilometrierung der strecke.
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
     * 
     * @param lap
     *            Runde [Anzahl]
     * @param start
     *            start in Meter [m]
     * @param end
     *            ende der runde in [m]
     * @param time
     *            Zeit in Millisekunden [ms]
     * @param heartBeat
     *            Herzschlag in [Bpm]
     * @param pace
     *            Pace in [min/km]
     * @param geschwindigkeit
     *            durchschnittliche Geschwindigkeit [km/h]
     */
    public LapInfo(final int lap, final int start, final int end, final long time, final int heartBeat, final String pace, final String geschwindigkeit) {
        this.lap = lap;
        this.start = start;
        this.end = end;
        this.time = time;
        this.heartBeat = heartBeat;
        this.pace = pace;
        this.geschwindigkeit = geschwindigkeit;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(final int id) {
        this.id = id;
    }

    @Override
    public int getLap() {
        return lap;
    }

    @Override
    public void setLap(final int lap) {
        this.lap = lap;
    }

    @Override
    public int getStart() {
        return start;
    }

    @Override
    public void setStart(final int start) {
        this.start = start;
    }

    @Override
    public int getEnd() {
        return end;
    }

    @Override
    public void setEnd(final int end) {
        this.end = end;
    }

    @Override
    public long getTime() {
        return time;
    }

    @Override
    public void setTime(final long time) {
        this.time = time;
    }

    @Override
    public int getHeartBeat() {
        return heartBeat;
    }

    @Override
    public void setHeartBeat(final int heartBeat) {
        this.heartBeat = heartBeat;
    }

    @Override
    public String getPace() {
        return pace;
    }

    @Override
    public void setPace(final String pace) {
        this.pace = pace;
    }

    @Override
    public String getGeschwindigkeit() {
        return geschwindigkeit;
    }

    @Override
    public void setGeschwindigkeit(final String geschwindigkeit) {
        this.geschwindigkeit = geschwindigkeit;
    }

    @Override
    public ITraining getTraining() {
        return training;
    }

    @Override
    public void setTraining(final ITraining training) {
        this.training = training;
    }

    @SuppressWarnings("nls")
    @Override
    public String toString() {
        return "LapInfo [id=" + id + ", lap=" + lap + ", start=" + start + ", end=" + end + ", time=" + time + ", heartBeat=" + heartBeat + ", pace=" + pace
                + "]";
    }

}
