package com.example.org1.utils;

import org.hyperledger.fabric.gateway.Contract;
import org.hyperledger.fabric.gateway.Gateway;
import org.hyperledger.fabric.sdk.Channel;

import java.nio.charset.StandardCharsets;

public class FabricHelper {
    public static Contract getContract(Gateway gateway, String networkName, String chaincodeId){
        return gateway.getNetwork(networkName).getContract(chaincodeId);
    }
    public static Channel getChannel(Gateway gateway, String networkName, String chaincodeId){
        return gateway.getNetwork(networkName).getChannel();
    }

    public static String newString(byte[] bytes) throws Exception{
            return new String(bytes, StandardCharsets.UTF_8);
    }
}
