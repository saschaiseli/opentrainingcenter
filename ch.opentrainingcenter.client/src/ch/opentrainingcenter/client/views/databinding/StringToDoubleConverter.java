package ch.opentrainingcenter.client.views.databinding;

import org.eclipse.core.databinding.conversion.IConverter;

public class StringToDoubleConverter implements IConverter {

    @Override
    public Object getToType() {
        return Double.class;
    }

    @Override
    public Object getFromType() {
        return String.class;
    }

    @Override
    public Object convert(final Object fromObject) {
        return Double.parseDouble(fromObject.toString());
    }

}
