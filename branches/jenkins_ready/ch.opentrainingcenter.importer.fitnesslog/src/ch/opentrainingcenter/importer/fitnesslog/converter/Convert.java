package ch.opentrainingcenter.importer.fitnesslog.converter;

public interface Convert<I, O> {
    O convert(final I input);
}
