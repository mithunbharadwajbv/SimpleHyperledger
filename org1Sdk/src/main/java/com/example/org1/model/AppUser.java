package com.example.org1.model;

import org.hyperledger.fabric.gateway.Gateway;

public class AppUser {
    private String userName;
    private Gateway gatewayConnection;

    public AppUser(String userName, Gateway gatewayConnection) {
        this.userName = userName;
        this.gatewayConnection = gatewayConnection;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Gateway getGatewayConnection() {
        return gatewayConnection;
    }

    public void setGatewayConnection(Gateway gatewayConnection) {
        this.gatewayConnection = gatewayConnection;
    }
}
