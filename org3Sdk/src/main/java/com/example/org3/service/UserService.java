package com.example.org3.service;

import org.hyperledger.fabric_ca.sdk.HFCAClient;

public interface UserService {
    void addConnection(String userName) throws Exception;
    void initializeConnectionForExistingUsers() throws Exception;
    void reConnection(String userName) throws Exception;
    HFCAClient getCAClient() throws Exception;
    void enrollAdmin() throws Exception;
    void registerUser(String userName) throws Exception;
}
