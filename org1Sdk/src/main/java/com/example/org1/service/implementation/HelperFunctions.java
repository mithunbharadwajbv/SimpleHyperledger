package com.example.org1.service.implementation;

import com.google.gson.Gson;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.UUID;

@Service
public class HelperFunctions {

    @Autowired
    ChaincodeService chaincodeService;

    private static Logger logger = LoggerFactory.getLogger(HelperFunctions.class);

//    public ArrayList<TokenDlt> tokenListToTokenDltList(ArrayList<Token> tokenList) throws Exception {
//
//        ArrayList<TokenDlt> tokenDltList = new ArrayList<>();
//
//        for (Token token : tokenList) {
//
//            String jsonStringOfToken = new Gson().toJson(token);
//
//            ObjectMapper objectMapper = new ObjectMapper();
//            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
//            TokenDlt tokenDlt = objectMapper.readValue(jsonStringOfToken, TokenDlt.class);
//
//            Map<String,Double> map = new HashMap<>();
//            map.put("capex",token.getCapex());
//            map.put("opex",token.getOpex());
//            tokenDlt.setFunds(map);
//
//            tokenDltList.add(tokenDlt);
//        }
//
//        return tokenDltList;
//    }

//    public TransferRequest transferRequestEnrich(TransferRequest transferRequest) throws Exception {
//
//        logger.info("print 1 :" + new Gson().toJson(transferRequest));
//
//        ArrayList<String> uuidList = new ArrayList<>();
//        for( Integer i=0;i<=transferRequest.getTokens().size();i++){
//
//            uuidList.add(UUID.randomUUID().toString());
//        }
//
//        transferRequest.setNewIds(uuidList);
//
//        logger.info("print 2 :" + new Gson().toJson(transferRequest));
//
//        return transferRequest;
//
//    }
//
    public byte[] getMyPaper () throws Exception{

        String queryString = "{\"selector\":{\"owner\":\"" + "Org1" + "\",\"State\":{\"$in\":[\"Trading\",\"Issued\"]}}}";
        System.out.println("queryString : " + queryString);

        return chaincodeService.getQueryResultForQueryString(queryString);

    }
//
//    public byte[] GetAllTokens_Infy (String user) throws Exception{
//
//        String queryString = "{ \"selector\": { \"docType\": \"Wallet\", \"owner\": \"" + user + "\" }, \"fields\": [ \"walletID\" ] }";
//        System.out.println("queryString : " + queryString);
//
//        byte[] walletListInBytes = chaincodeService.getQueryResultForQueryString(queryString);
//
//        JSONArray walletListJsonArray = new JSONArray(new String(walletListInBytes, "UTF-8"));
//
//        JSONArray resultArray = new JSONArray();
//
//        for (Integer i = 0; i < walletListJsonArray.length(); i++) {
//            JSONObject walletJsonObject = walletListJsonArray.getJSONObject(i);
//
//            String walletId = walletJsonObject.getString("walletID");
//
//            String queryString2 = "{ \"selector\": { \"docType\": \"VzCoinInfy\", \"walletID\": \"" + walletId + "\" } }";
//            System.out.println("queryString2 : " + queryString2);
//
//            byte[] tokenListInBytes = chaincodeService.getQueryResultForQueryString(queryString2);
//
//            JSONArray tokenListInJsonArray = new JSONArray(new String(tokenListInBytes, "UTF-8"));
//
//            for (Integer j = 0; i < tokenListInJsonArray.length(); i++) {
//                JSONObject tokenJsonObject = tokenListInJsonArray.getJSONObject(i);
//                resultArray.put(tokenJsonObject);
//            }
//
//        }
//        return resultArray.toString().getBytes();
//    }

}
