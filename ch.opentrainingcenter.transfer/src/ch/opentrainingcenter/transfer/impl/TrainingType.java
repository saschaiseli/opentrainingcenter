package ch.opentrainingcenter.transfer.impl;

import java.util.HashSet;
import java.util.Set;

import ch.opentrainingcenter.transfer.IImported;
import ch.opentrainingcenter.transfer.ITrainingType;

public class TrainingType implements ITrainingType {

    private int id;
    private String title;
    private String description;
    private Set<IImported> importeds = new HashSet<IImported>(0);

    public TrainingType() {
        // for hibernate
    }

    public TrainingType(final int id, final String title, final String description) {
        super();
        this.id = id;
        this.title = title;
        this.description = description;
    }

    @Override
    public int getId() {
        return id;
    }

    public void setId(final int id) {
        this.id = id;
    }

    @Override
    public String getTitle() {
        return title;
    }

    public void setTitle(final String title) {
        this.title = title;
    }

    @Override
    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    @Override
    public Set<IImported> getImporteds() {
        return this.importeds;
    }

    public void setImporteds(final Set<IImported> importeds) {
        this.importeds = importeds;
    }
}
