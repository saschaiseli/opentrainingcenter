package ch.opentrainingcenter.client.views.dialoge;

public interface IFilterDialog {

    public abstract String open();

    public abstract String[] getFileNames();

    public abstract String getFilterPath();

}