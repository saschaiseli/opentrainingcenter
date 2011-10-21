package ch.iseli.sportanalyzer.client.cache;

public class TrainingCenterDatabaseTChild {

    private final TrainingCenterDatabaseTParent parent;
    private final String                        label;
    private final ChildTyp                      typ;

    public TrainingCenterDatabaseTChild(TrainingCenterDatabaseTParent databaseT, String label, ChildTyp typ) {
        parent = databaseT;
        this.label = label;
        this.typ = typ;
    }

    @Override
    public String toString() {
        return label;
    }

    public TrainingCenterDatabaseTParent getParent() {
        return parent;
    }

    public ChildTyp getTyp() {
        return typ;
    }
}
