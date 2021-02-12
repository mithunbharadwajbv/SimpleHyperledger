package com.example.org1.service.implementation;


import com.example.org1.model.IssueRequest;
import com.example.org1.model.UserWallet;
import com.example.org1.model.UserModel;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import org.hyperledger.fabric.gateway.*;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;


@Service
public class ChaincodeService {

    static {
        System.setProperty("org.hyperledger.fabric.sdk.service_discovery.as_localhost", "true");
    }


    private static Logger logger = LoggerFactory.getLogger(ChaincodeService.class);

//    public void TransferTokens(TransferRequest transferRequest) throws Exception{
//
//        Path walletPath = Paths.get("wallet");
//        Wallet wallet = Wallets.newFileSystemWallet(walletPath);
//
//        Path networkConfigPath = Paths.get("..", "test-network", "organizations", "peerOrganizations", "verizon.example.com", "connection-verizon.yaml");
//
//        Gateway.Builder builder = Gateway.createBuilder();
//        builder.identity(wallet, "admin").networkConfig(networkConfigPath).discovery(true);
//
//        String jsonString = new Gson().toJson(transferRequest);
//
//        logger.info("jsonString hty : "+ jsonString);
//
//        try (Gateway gateway = builder.connect()) {
//
//            // get the network and contract
//            Network network = gateway.getNetwork("mychannel");
//            Contract contract = network.getContract("vz");
//
//            //call chaincode
//            contract.submitTransaction("TransferTokens",jsonString);
//        }
//    }

//    public void MintTokens(ArrayList<VzCoin> vzCoinList) throws Exception{
//
//        Path walletPath = Paths.get("wallet");
//        Wallet wallet = Wallets.newFileSystemWallet(walletPath);
//
//        Path networkConfigPath = Paths.get("..", "test-network", "organizations", "peerOrganizations", "verizon.example.com", "connection-verizon.yaml");
//
//
//        Gateway.Builder builder = Gateway.createBuilder();
//        builder.identity(wallet, "admin").networkConfig(networkConfigPath).discovery(true);
//
//        JSONArray jsonArray = new JSONArray();
//
//        for(VzCoin tokenDlt : vzCoinList) {
//
//            String jsonString = new Gson().toJson(tokenDlt);
//            JSONObject jsonObject = new JSONObject(jsonString);
//            jsonArray.put(jsonObject);
//        }
//
//        logger.info("jsonArrayOfTransactionDLT gdsgd : "+ jsonArray.toString());
//
//        try (Gateway gateway = builder.connect()) {
//
//            // get the network and contract
//            Network network = gateway.getNetwork("mychannel");
//            Contract contract = network.getContract("vz");
//
//            //call chaincode
//            contract.submitTransaction("MintTokens",jsonArray.toString());
//
//        }
//
//    }

//    public byte[] GetTokenHistory(String username, String coinId) throws Exception{
//        Path walletPath = Paths.get("wallet");
//        Wallet wallet = Wallets.newFileSystemWallet(walletPath);
//        Path networkConfigPath = Paths.get("..", "test-network", "organizations", "peerOrganizations", "verizon.example.com", "connection-verizon.yaml");
//        Gateway.Builder builder = Gateway.createBuilder();
//        builder.identity(wallet, username).networkConfig(networkConfigPath).discovery(true);
//        try (Gateway gateway = builder.connect()) {
//            // get the network and contract
//            Network network = gateway.getNetwork("mychannel");
//            Contract contract = network.getContract("vz");
//            byte[] result = contract.evaluateTransaction("GetTokenHistory",coinId);
//            return result;
//        }
//    }

    public void issue(IssueRequest issueRequest) throws Exception {

        Path walletPath = Paths.get("wallet");
        Wallet wallet = Wallets.newFileSystemWallet(walletPath);

        Path networkConfigPath = Paths.get("..", "test-network", "organizations", "peerOrganizations", "org1.example.com", "connection-org1.yaml");

        Gateway.Builder builder = Gateway.createBuilder();
        builder.identity(wallet, "admin").networkConfig(networkConfigPath).discovery(true);

        String jsonString = new Gson().toJson(issueRequest);

        logger.info("jsonString hty : " + jsonString);

        try (Gateway gateway = builder.connect()) {

            // get the network and contract
            Network network = gateway.getNetwork("mychannel");
            Contract contract = network.getContract("fabcar");

            //call chaincode
            contract.submitTransaction("Issue", jsonString);
        }

    }

    public void sell(String paperId ,String newOwner , String newPrice) throws Exception {

        Path walletPath = Paths.get("wallet");
        Wallet wallet = Wallets.newFileSystemWallet(walletPath);

        Path networkConfigPath = Paths.get("..", "test-network", "organizations", "peerOrganizations", "org1.example.com", "connection-org1.yaml");

        Gateway.Builder builder = Gateway.createBuilder();
        builder.identity(wallet, "admin").networkConfig(networkConfigPath).discovery(true);

        logger.info("buy with data paperId %S : ", paperId);

        try (Gateway gateway = builder.connect()) {

            // get the network and contract
            Network network = gateway.getNetwork("mychannel");
            Contract contract = network.getContract("fabcar");

            //call chaincode
            contract.submitTransaction("Sell", paperId , newOwner , newPrice);
        }

    }

    public void redeem(String paperId, String todayDate) throws Exception {

        Path walletPath = Paths.get("wallet");
        Wallet wallet = Wallets.newFileSystemWallet(walletPath);

        Path networkConfigPath = Paths.get("..", "test-network", "organizations", "peerOrganizations", "org1.example.com", "connection-org1.yaml");

        Gateway.Builder builder = Gateway.createBuilder();
        builder.identity(wallet, "admin").networkConfig(networkConfigPath).discovery(true);

        logger.info("buy with data paperId %S newOwner %s: ", paperId, todayDate);

        try (Gateway gateway = builder.connect()) {

            // get the network and contract
            Network network = gateway.getNetwork("mychannel");
            Contract contract = network.getContract("fabcar");

            //call chaincode
            contract.submitTransaction("Redeem", paperId, todayDate);
        }
    }


    public byte[] getQueryResultForQueryString(String queryString) throws Exception{

        Path walletPath = Paths.get("wallet");
        Wallet wallet = Wallets.newFileSystemWallet(walletPath);

        Path networkConfigPath = Paths.get("..", "test-network", "organizations", "peerOrganizations", "org1.example.com", "connection-org1.yaml");


        Gateway.Builder builder = Gateway.createBuilder();
        builder.identity(wallet, "admin").networkConfig(networkConfigPath).discovery(true);

        try (Gateway gateway = builder.connect()) {

            // get the network and contract
            Network network = gateway.getNetwork("mychannel");
            Contract contract = network.getContract("fabcar");

            byte[] result = contract.evaluateTransaction("getQueryResultForQueryString",queryString);

            return result;
        }
    }

    public byte[] getPaperHistory(String paperId) throws Exception{

        Path walletPath = Paths.get("wallet");
        Wallet wallet = Wallets.newFileSystemWallet(walletPath);

        Path networkConfigPath = Paths.get("..", "test-network", "organizations", "peerOrganizations", "org1.example.com", "connection-org1.yaml");


        Gateway.Builder builder = Gateway.createBuilder();
        builder.identity(wallet, "admin").networkConfig(networkConfigPath).discovery(true);

        try (Gateway gateway = builder.connect()) {

            // get the network and contract
            Network network = gateway.getNetwork("mychannel");
            Contract contract = network.getContract("fabcar");

            byte[] result = contract.evaluateTransaction("GetPaperHistory" , paperId);

            return result;
        }
    }

}
