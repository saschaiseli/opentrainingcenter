package ch.opentrainingcenter.core.service;

import org.eclipse.ui.services.IServiceLocator;

import ch.opentrainingcenter.core.service.internal.DatabaseService;

public class ServiceFactory extends org.eclipse.ui.services.AbstractServiceFactory {

    public ServiceFactory() {
    }

    @Override
    public Object create(@SuppressWarnings("rawtypes") final Class serviceInterface, final IServiceLocator parentLocator, final IServiceLocator locator) {
        if (serviceInterface.equals(IDatabaseService.class)) {
            return DatabaseService.getInstance();
        }
        return null;
    }

}
