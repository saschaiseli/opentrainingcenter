package ch.opentrainingcenter.client.views.databinding;

import org.eclipse.core.databinding.conversion.IConverter;

public class StringToIntegerConverter implements IConverter {
    @Override
    public Object getToType() {
        return Integer.class;
    }

    @Override
    public Object getFromType() {
        return String.class;
    }

    @Override
    public Object convert(final Object fromObject) {
        return Integer.parseInt(fromObject.toString());
    }

}
