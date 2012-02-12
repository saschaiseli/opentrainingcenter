package ch.opentrainingcenter.transfer;

import java.util.Set;

public interface ITrainingType {

    public abstract int getId();

    public abstract String getTitle();

    public abstract String getDescription();

    public abstract Set<IImported> getImporteds();
}
