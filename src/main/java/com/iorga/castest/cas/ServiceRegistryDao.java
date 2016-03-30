package com.iorga.castest.cas;

import com.iorga.castest.service.ConfigRegistry;
import org.jasig.cas.services.RegisteredService;

import java.util.List;

public class ServiceRegistryDao implements org.jasig.cas.services.ServiceRegistryDao {
    private ConfigRegistry configRegistry;

    @Override
    public RegisteredService save(RegisteredService registeredService) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean delete(RegisteredService registeredService) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<RegisteredService> load() {
        return (List<RegisteredService>) (List) getConfigRegistry().getServices();
    }

    @Override
    public RegisteredService findServiceById(long id) {
        return getConfigRegistry().getServicesById().get(id);
    }


    public ConfigRegistry getConfigRegistry() {
        return configRegistry;
    }

    public void setConfigRegistry(ConfigRegistry configRegistry) {
        this.configRegistry = configRegistry;
    }
}
