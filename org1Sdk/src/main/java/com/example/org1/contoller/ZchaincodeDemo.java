package com.example.org1.contoller;


import org.hyperledger.fabric.gateway.Contract;
import org.hyperledger.fabric.gateway.Gateway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.hyperledger.fabric.gateway.Network;
import org.hyperledger.fabric.gateway.Wallet;
import org.hyperledger.fabric.gateway.Wallets;


import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/demochaincode")
public class ZchaincodeDemo {

    static {
        System.setProperty("org.hyperledger.fabric.sdk.service_discovery.as_localhost", "true");
    }

    private static Logger logger = LoggerFactory.getLogger(ChaincodeController.class);


    @PostMapping("/queryRecord/{userName}")
    public String queryRecord(@PathVariable("userName") String userName) throws Exception {

        Path walletPath = Paths.get("wallet");
        Wallet wallet = Wallets.newFileSystemWallet(walletPath);


        Path networkConfigPath = Paths.get("..", "test-network", "organizations", "peerOrganizations", "verizon.example.com", "connection-verizon.yaml");


        Gateway.Builder builder = Gateway.createBuilder();
        builder.identity(wallet, userName).networkConfig(networkConfigPath).discovery(true);

        try (Gateway gateway = builder.connect()) {

            // get the network and contract
            Network network = gateway.getNetwork("mychannel");
            Contract contract = network.getContract("vz");



//            byte[] result1 = contract.evaluateTransaction("QueryByKeyForResult","demouser");
//            System.out.println("chaincode invoke is success " + new String(result1));
//
//
//
//            contract.submitTransaction("CreateUser","mit","mit1","mit2");
//            byte[] result2 = contract.evaluateTransaction("QueryByKeyForResult","mit");
//            System.out.println("CreateUser is success" + new String(result2));

//            byte[] result3 = contract.evaluateTransaction("QueryBusinessCaseName");
//            System.out.println("business cases for perticular user " + new String(result3));

//            byte[] result4 = contract.evaluateTransaction("QueryBusinessCaseDetails", "BC-3");
//            System.out.println("business case detail of BC-23456 :" + new String(result4));


//            byte[] result6 = contract.evaluateTransaction("QueryUserBusinessCase","1","1");
//            System.out.println("business cases for perticular user " + new String(result6));

//            String queryString = "{\"selector\":{\"docType\":\"BusinessCase\",\"users\":{\"$elemMatch\":{\"$eq\":\"cindy\"}}},\"skip\":2,\"limit\":1,\"sort\":[{\"bcId\":\"asc\"},{\"bcCommonName\":\"asc\"}],\"use_index\":[\"_design/businessCaseDoc\",\"businessCase\"]}";
//            logger.info("queryString"+queryString);
//            byte[] result7 = contract.evaluateTransaction("getQueryResultForQueryString",  queryString);

            String queryString = "{\"selector\":{\"docType\":\"BusinessCase\",\"users\":{\"$elemMatch\":{\"$eq\":\"cindy\"}}},\"skip\":2,\"sort\":[{\"bcId\":\"asc\"},{\"bcCommonName\":\"asc\"}],\"use_index\":[\"_design/businessCaseDoc\",\"businessCase\"]}";
            logger.info("queryString"+queryString);
            byte[] result8 = contract.evaluateTransaction("GetQueryResultPaginationForQueryString",  queryString, "1");


            return new String(result8);
        }

    }
}
