package com.example.org1.utils;

import com.example.org1.model.AppUser;
import org.hyperledger.fabric.gateway.Gateway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.util.HashMap;
import java.util.Map;

@Component
public class UserGatewayConnection implements AutoCloseable {
    private static Logger logger = LoggerFactory.getLogger(UserGatewayConnection.class);
    private Map<String, AppUser> appUsers = new HashMap<>();

    public void addUser(AppUser appUser) {
        appUsers.put(appUser.getUserName(), appUser);
    }

    public Gateway getGatewayConnection(String userName) {
        return appUsers.get(userName).getGatewayConnection();
    }

    @PreDestroy
    @Override
    public void close() throws Exception {
        for(AppUser appUser : appUsers.values()){
            appUser.getGatewayConnection().close();
        }
        logger.info("Gateway connection closed for "+appUsers.size()+" users");
    }
}
