package com.iorga.castest.cas;

import org.jasig.cas.authentication.principal.Principal;
import org.jasig.cas.authentication.principal.Service;

public class RegisteredServiceUsernameAttributeProvider implements org.jasig.cas.services.RegisteredServiceUsernameAttributeProvider {
    @Override
    public String resolveUsername(Principal principal, Service service) {
        return principal.getId();
    }
}
