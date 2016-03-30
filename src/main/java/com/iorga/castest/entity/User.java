package com.iorga.castest.entity;


import org.jasig.cas.authentication.principal.Principal;

import java.util.Map;

public class User implements Principal {
    private boolean acceptAnyPassword;
    private String password;
    private String userName;
    private Map<String, Object> attributes;

    @Override
    public String getId() {
        return getUserName();
    }


    public void setAttributes(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    public boolean isAcceptAnyPassword() {
        return acceptAnyPassword;
    }

    public void setAcceptAnyPassword(boolean acceptAnyPassword) {
        this.acceptAnyPassword = acceptAnyPassword;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
