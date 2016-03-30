package com.iorga.castest.cas;

import com.iorga.castest.service.ConfigRegistry;
import org.jasig.cas.authentication.principal.Service;
import org.jasig.cas.services.RegisteredService;

import java.util.Collection;

public class ServicesManager implements org.jasig.cas.services.ServicesManager {
    private ConfigRegistry configRegistry;

    @Override
    public RegisteredService save(RegisteredService registeredService) {
        throw new UnsupportedOperationException();
    }

    @Override
    public RegisteredService delete(long id) {
        throw new UnsupportedOperationException();
    }

    @Override
    public RegisteredService findServiceBy(Service service) {
        if (configRegistry.isAcceptAnyService()) {
            com.iorga.castest.entity.Service registeredService = new com.iorga.castest.entity.Service();
            registeredService.setUrl(service.getId());
            return registeredService;
        } else {
            for (com.iorga.castest.entity.Service registeredService : configRegistry.getServices()) {
                if (registeredService.matches(service)) {
                    return registeredService;
                }
            }
        }
        return null;
    }

    @Override
    public RegisteredService findServiceBy(long id) {
        return configRegistry.getServicesById().get(id);
    }

    @Override
    public Collection<RegisteredService> getAllServices() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean matchesExistingService(Service service) {
        return findServiceBy(service) != null;
    }


    public ConfigRegistry getConfigRegistry() {
        return configRegistry;
    }

    public void setConfigRegistry(ConfigRegistry configRegistry) {
        this.configRegistry = configRegistry;
    }
}
