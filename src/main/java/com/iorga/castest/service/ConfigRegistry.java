package com.iorga.castest.service;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.iorga.castest.entity.Service;
import com.iorga.castest.entity.User;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.*;

public class ConfigRegistry {
    private List<User> users;
    private Map<String, User> usersByUserName;
    private List<Service> services;
    private Map<Long, Service> servicesById;
    private boolean acceptAnyLoginAndPassword;
    private boolean acceptAnyService;

    @PostConstruct
    public void init() throws IOException {
        String casConfig = System.getenv("CAS_CONFIG");
        if (casConfig != null) {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
            objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
            objectMapper.setVisibility(PropertyAccessor.ALL, Visibility.ANY);
            objectMapper.setVisibility(PropertyAccessor.FIELD, Visibility.NONE);

            ConfigRegistry configRegistry = objectMapper.readValue(casConfig, ConfigRegistry.class);
            this.setUsers(configRegistry.getUsers());
            this.setAcceptAnyLoginAndPassword(configRegistry.isAcceptAnyLoginAndPassword() || this.getUsers().isEmpty());
            this.setServices(configRegistry.getServices());
            this.setAcceptAnyService(configRegistry.isAcceptAnyService() || this.getServices().isEmpty());
        } else {
            this.setAcceptAnyLoginAndPassword(true);
            this.setAcceptAnyService(true);
        }
    }

    public User getUserByUsername(String username) {
        return usersByUserName.get(username);
    }

    public void setUsers(List<User> users) {
        this.users = users;
        this.usersByUserName = new HashMap<>();
        if (users != null) {
            for (User user : users) {
                usersByUserName.put(user.getUserName(), user);
            }
        } else {
            this.users = Collections.emptyList();
        }
    }

    public void setServices(List<Service> services) {
        this.services = services;
        this.servicesById = new HashMap<>();
        if (services != null) {
            long i = 0;
            for (Service service : services) {
                if (service.getId() == 0) {
                    service.setId(i);
                }
                this.servicesById.put(service.getId(), service);
                i++;
            }
            Collections.sort(this.services, (s1, s2) -> s1.getEvaluationOrder() - s2.getEvaluationOrder());
        } else {
            this.services = Collections.emptyList();
        }
    }


    public boolean isAcceptAnyLoginAndPassword() {
        return acceptAnyLoginAndPassword;
    }

    public void setAcceptAnyLoginAndPassword(boolean acceptAnyLoginAndPassword) {
        this.acceptAnyLoginAndPassword = acceptAnyLoginAndPassword;
    }

    public List<Service> getServices() {
        return services;
    }

    public List<User> getUsers() {
        return users;
    }

    public Map<Long, Service> getServicesById() {
        return servicesById;
    }

    public boolean isAcceptAnyService() {
        return acceptAnyService;
    }

    public void setAcceptAnyService(boolean acceptAnyService) {
        this.acceptAnyService = acceptAnyService;
    }
}
