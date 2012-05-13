package ch.opentrainingcenter.client.charts;

public class PositionPace {
    private final double position;
    private final double pace;

    public PositionPace(final double position, final double pace) {
        this.position = position;
        this.pace = pace;
    }

    public double getPosition() {
        return position;
    }

    public double getPace() {
        return pace;
    }

}
