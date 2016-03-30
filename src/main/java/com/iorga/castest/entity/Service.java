package com.iorga.castest.entity;

import com.iorga.castest.cas.*;
import org.jasig.cas.authentication.principal.Principal;
import org.jasig.cas.services.*;
import org.jasig.cas.services.RegisteredServiceUsernameAttributeProvider;

import java.net.URI;
import java.net.URL;
import java.util.*;
import java.util.regex.Pattern;

public class Service implements RegisteredService, Comparable<Service> {
    private static RegisteredServiceProxyPolicy PROXY_POLICY = new RefuseRegisteredServiceProxyPolicy();
    private static RegisteredServiceUsernameAttributeProvider USERNAME_ATTRIBUTE_PROVIDER = new com.iorga.castest.cas.RegisteredServiceUsernameAttributeProvider();
    private static final Set<String> REQUIRED_HANDLERS = new HashSet<>(Arrays.asList(AuthenticationHandler.class.getName()));

    private RegisteredServiceAccessStrategy accessStrategy = new RegisteredServiceAccessStrategy();
    private RegisteredServiceAttributeReleasePolicy attributeReleasePolicy = new RegisteredServiceAttributeReleasePolicy();

    private long id;
    private String url;
    private Pattern urlPattern;
    private Set<String> allowedUserNames;
    private Map<String, Map<String, Object>> attributesOverridesByUserName;
    private URL logoutUrl;
    private boolean acceptAnyUserName = true;


    private class RegisteredServiceAccessStrategy implements org.jasig.cas.services.RegisteredServiceAccessStrategy {

        @Override
        public boolean isServiceAccessAllowed() {
            return true;
        }

        @Override
        public boolean isServiceAccessAllowedForSso() {
            return false;
        }

        @Override
        public boolean doPrincipalAttributesAllowServiceAccess(String username, Map<String, Object> attributes) {
            return Service.this.acceptAnyUserName || Service.this.allowedUserNames.contains(username);
        }

        @Override
        public URI getUnauthorizedRedirectUrl() {
            return null;
        }
    }

    private class RegisteredServiceAttributeReleasePolicy implements org.jasig.cas.services.RegisteredServiceAttributeReleasePolicy {

        @Override
        public boolean isAuthorizedToReleaseCredentialPassword() {
            return false;
        }

        @Override
        public boolean isAuthorizedToReleaseProxyGrantingTicket() {
            return false;
        }

        @Override
        public void setAttributeFilter(RegisteredServiceAttributeFilter registeredServiceAttributeFilter) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Map<String, Object> getAttributes(Principal principal) {
            Map<String, Object> attributesOverridesForThisUser = Service.this.attributesOverridesByUserName.get(principal.getId());
            if (attributesOverridesForThisUser != null) {
                HashMap<String, Object> attributes = new HashMap<>(principal.getAttributes()); // copy the attributes in order to eventually modify them
                attributes.putAll(attributesOverridesForThisUser);
                return attributes;
            } else {
                return principal.getAttributes();
            }
        }
    }


    public void setUrl(String url) {
        this.url = url;
        this.urlPattern = Pattern.compile(url);
    }

    @Override
    public int compareTo(Service o) {
        return o.getEvaluationOrder() - getEvaluationOrder();
    }

    @Override
    public RegisteredServiceProxyPolicy getProxyPolicy() {
        return PROXY_POLICY;
    }

    @Override
    public String getServiceId() {
        return ""+getId();
    }

    @Override
    public long getId() {
        return id;
    }

    @Override
    public String getName() {
        return url;
    }

    @Override
    public String getTheme() {
        return null;
    }

    @Override
    public String getDescription() {
        return url;
    }

    @Override
    public int getEvaluationOrder() {
        return (int)getId();
    }

    @Override
    public void setEvaluationOrder(int evaluationOrder) {
        id = evaluationOrder;
    }

    @Override
    public RegisteredServiceUsernameAttributeProvider getUsernameAttributeProvider() {
        return USERNAME_ATTRIBUTE_PROVIDER;
    }

    @Override
    public Set<String> getRequiredHandlers() {
        return REQUIRED_HANDLERS;
    }

    @Override
    public RegisteredServiceAccessStrategy getAccessStrategy() {
        return accessStrategy;
    }

    @Override
    public boolean matches(org.jasig.cas.authentication.principal.Service service) {
        return urlPattern.matcher(service.getId()).matches();
    }

    @Override
    public RegisteredService clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException();
    }

    @Override
    public LogoutType getLogoutType() {
        return LogoutType.FRONT_CHANNEL;
    }

    @Override
    public RegisteredServiceAttributeReleasePolicy getAttributeReleasePolicy() {
        return attributeReleasePolicy;
    }

    @Override
    public URL getLogo() {
        return null;
    }

    @Override
    public URL getLogoutUrl() {
        return logoutUrl;
    }

    @Override
    public RegisteredServicePublicKey getPublicKey() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Map<String, RegisteredServiceProperty> getProperties() {
        return null;
    }

    public void setAllowedUserNames(Set<String> allowedUserNames) {
        this.allowedUserNames = allowedUserNames;
        this.setAcceptAnyUserName(allowedUserNames.isEmpty());
    }


    public Set<String> getAllowedUserNames() {
        return allowedUserNames;
    }

    public Map<String, Map<String, Object>> getAttributesOverridesByUserName() {
        return attributesOverridesByUserName;
    }

    public void setAttributesOverridesByUserName(Map<String, Map<String, Object>> attributesOverridesByUserName) {
        this.attributesOverridesByUserName = attributesOverridesByUserName;
    }

    public void setLogoutUrl(URL logoutUrl) {
        this.logoutUrl = logoutUrl;
    }

    public String getUrl() {
        return url;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isAcceptAnyUserName() {
        return acceptAnyUserName;
    }

    public void setAcceptAnyUserName(boolean acceptAnyUserName) {
        this.acceptAnyUserName = acceptAnyUserName;
    }
}
