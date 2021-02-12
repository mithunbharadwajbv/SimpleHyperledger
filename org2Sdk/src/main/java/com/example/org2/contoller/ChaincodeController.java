package com.example.org2.contoller;


import com.example.org2.model.IssueRequest;
import com.example.org2.service.implementation.*;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.websocket.server.PathParam;
import java.util.ArrayList;


@RestController
@RequestMapping("/chaincode")
public class ChaincodeController {

    private static Logger logger = LoggerFactory.getLogger(ChaincodeController.class);


    @Autowired
    private ChaincodeService chaincodeService;


    @Autowired
    private HelperFunctions help;


//    @GetMapping("/QueryBusinessCaseDetails/{userName}/{businessCaseId}")
//    public ResponseEntity<Object> QueryUserBusinessCase(@PathVariable("userName") String username, @PathVariable("businessCaseId") String businessCaseId) throws Exception {
//        logger.info("Class: " + this.getClass().getName() + " Function: QueryBusinessCaseDetails");
//
//        try {
//
//            return ResponseEntity.status(HttpStatus.OK)
//                    .body(chaincodeService.QueryBusinessCaseDetails(username, businessCaseId));
//
//        } catch (Exception e) {
//            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//
//    }

//    @PostMapping("/MintTokens")
//    public ResponseEntity<Object> MintTokens(@RequestBody ArrayList<VzCoin> vzCoinList) {
//        logger.info("received upload excel request!!!");
//
//        try {
//
//
//            chaincodeService.MintTokens(vzCoinList);
//
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(vzCoinList);
//        }
//
//        return new ResponseEntity<>("Transaction added", HttpStatus.OK);
//    }

    @PostMapping("/issue")
    public ResponseEntity<Object> issue(@RequestBody IssueRequest issueRequest) {
        logger.info("received issue request!!!");

        try {


            chaincodeService.issue(issueRequest);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }

        return new ResponseEntity<>("Issue Successful", HttpStatus.OK);
    }

    @PostMapping("/sell")
    public ResponseEntity<Object> buy(@RequestParam String paperId , @RequestParam String newOwner , @RequestParam String newPrice ) {
        logger.info("received buy request!!!");

        try {
            chaincodeService.sell(paperId , newOwner ,  newPrice);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }

        return new ResponseEntity<>("Sell Successful", HttpStatus.OK);
    }

    @PostMapping("/redeem")
    public ResponseEntity<Object> Redeem(@RequestParam String paperId , @RequestParam String todayDate) {
        logger.info("received redeem request!!!");

        try {
            chaincodeService.redeem(paperId, todayDate);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }

        return new ResponseEntity<>("Redeem Successful", HttpStatus.OK);
    }

    @GetMapping("/getMyPaper")
    public ResponseEntity<Object> getMyPaper() throws Exception {
        logger.info("Class: " + this.getClass().getName() + " Function: getMyPaper");

        try {

            return ResponseEntity.status(HttpStatus.OK)
                    .body(help.getMyPaper());

        } catch (Exception e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
















