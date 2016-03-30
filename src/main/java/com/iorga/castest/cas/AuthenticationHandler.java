package com.iorga.castest.cas;

import com.iorga.castest.entity.User;
import com.iorga.castest.service.ConfigRegistry;
import org.jasig.cas.authentication.HandlerResult;
import org.jasig.cas.authentication.PreventedException;
import org.jasig.cas.authentication.UsernamePasswordCredential;
import org.jasig.cas.authentication.handler.support.AbstractUsernamePasswordAuthenticationHandler;

import javax.security.auth.login.AccountNotFoundException;
import javax.security.auth.login.FailedLoginException;
import java.security.GeneralSecurityException;
import java.util.HashMap;

public class AuthenticationHandler extends AbstractUsernamePasswordAuthenticationHandler {
    private ConfigRegistry configRegistry;


    @Override
    protected HandlerResult authenticateUsernamePasswordInternal(UsernamePasswordCredential credential) throws GeneralSecurityException, PreventedException {
        final String username = getPrincipalNameTransformer().transform(credential.getUsername());
        final String password = this.getPasswordEncoder().encode(credential.getPassword());

        if (configRegistry.isAcceptAnyLoginAndPassword()) {
            User user = new User();
            user.setUserName(username);
            user.setPassword(password);
            HashMap<String, Object> attributes = new HashMap<>();
            attributes.put("username", username);
            user.setAttributes(attributes);
            return createHandlerResult(credential, user, null);
        } else {
            User user = configRegistry.getUserByUsername(username);
            if (user != null) {
                if (user.isAcceptAnyPassword() || password.equals(user.getPassword())) {
                    return createHandlerResult(credential, user, null);
                } else {
                    throw new FailedLoginException("Password does not match value on record.");
                }
            } else {
                throw new AccountNotFoundException();
            }
        }
    }


    public ConfigRegistry getConfigRegistry() {
        return configRegistry;
    }

    public void setConfigRegistry(ConfigRegistry configRegistry) {
        this.configRegistry = configRegistry;
    }
}
