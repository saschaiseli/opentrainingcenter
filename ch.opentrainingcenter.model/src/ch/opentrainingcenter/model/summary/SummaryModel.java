package ch.opentrainingcenter.model.summary;

import org.joda.time.Interval;

@SuppressWarnings("nls")
public class SummaryModel {

    private final float distanz;
    private final long dauerInSeconds;
    private final int maxHeart;
    private final int avgHeart;
    private final float trainingPerWeek;
    private final float trainingPerMonth;
    private final float kmPerWeek;
    private final float kmPerMonth;
    private final Interval startEnd;

    private SummaryModel(final SummaryBuilder builder) {
        distanz = builder.distance;
        dauerInSeconds = builder.dauerInSeconds;
        maxHeart = builder.maxHeart;
        avgHeart = builder.avgHeart;
        trainingPerWeek = Math.round(builder.trainingPerWeek * 100) / 100f;
        trainingPerMonth = Math.round(builder.trainingPerMonth * 100) / 100f;
        kmPerWeek = Math.round(builder.kmPerWeek * 100) / 100f;
        kmPerMonth = Math.round(builder.kmPerMonth * 100) / 100f;
        startEnd = builder.startEnd;
    }

    /**
     * @return Distanz in Meter
     */
    public float getDistanz() {
        return distanz;
    }

    public long getDauerInSeconds() {
        return dauerInSeconds;
    }

    public int getMaxHeart() {
        return maxHeart;
    }

    public int getAvgHeart() {
        return avgHeart;
    }

    public float getTrainingPerWeek() {
        return trainingPerWeek;
    }

    public float getTrainingPerMonth() {
        return trainingPerMonth;
    }

    public float getKmPerWeek() {
        return kmPerWeek;
    }

    public float getKmPerMonth() {
        return kmPerMonth;
    }

    public Interval getStartEnd() {
        return startEnd;
    }

    @Override
    public String toString() {
        return "SummaryModel [distanz=" + distanz + "]";
    }

    public static class SummaryBuilder {

        private float distance;
        private long dauerInSeconds;
        private int maxHeart;
        private int avgHeart;
        private float trainingPerWeek;
        private float trainingPerMonth;
        private float kmPerWeek;
        private float kmPerMonth;
        private Interval startEnd;

        public SummaryBuilder() {
        }

        public SummaryBuilder distance(final float distance) {
            this.distance = distance;
            return this;
        }

        public SummaryBuilder dauer(final long dauerInSeconds) {
            this.dauerInSeconds = dauerInSeconds;
            return this;
        }

        public SummaryBuilder maxHeart(final int maxHeart) {
            this.maxHeart = maxHeart;
            return this;
        }

        public SummaryBuilder avgHeart(final int avgHeart) {
            this.avgHeart = avgHeart;
            return this;
        }

        public SummaryBuilder trainingPerWeek(final float trainingPerWeek) {
            this.trainingPerWeek = trainingPerWeek;
            return this;
        }

        public SummaryBuilder trainingPerMonth(final float trainingPerMonth) {
            this.trainingPerMonth = trainingPerMonth;
            return this;
        }

        public SummaryBuilder kmPerWeek(final float kmPerWeek) {
            this.kmPerWeek = kmPerWeek;
            return this;
        }

        public SummaryBuilder kmPerMonth(final float kmPerMonth) {
            this.kmPerMonth = kmPerMonth;
            return this;
        }

        public SummaryBuilder interval(final Interval startEnd) {
            this.startEnd = startEnd;
            return this;
        }

        public SummaryModel build() {
            return new SummaryModel(this);
        }

    }
}
