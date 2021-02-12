package com.example.org3.contoller;

import com.example.org3.model.UserWallet;
import com.example.org3.model.UserModel;
import com.example.org3.service.implementation.UserServiceImpl;
import com.example.org3.service.implementation.ChaincodeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

/**
 * This class helps in performing user related operations
 */
@RestController
@RequestMapping("/user")
public class UserController {
    private static Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private ChaincodeService chaincodeService;

    /**
     * Enrolling an admin user
     *
     * @return
     */
    @PostMapping("/enrollAdmin")
    public ResponseEntity<String> enrollAdmin() {
        logger.info("Class: " + this.getClass().getName() + " Function: enrollAdmin");
        try {
            userService.enrollAdmin();

            return new ResponseEntity<>("Enrolling Admin is successful", HttpStatus.CREATED);

        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Registering a user into an organization
     *
     * @param userModel
     * @return
     */

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody UserModel userModel) {
        logger.info("Class: " + this.getClass().getName() + " Function: registerUser");
        try {
            //calling the blockchain service
            userService.registerUser(userModel.getUsername(), "admin", userModel.getDepartment());

            return new ResponseEntity<>("User: '" + userModel.getUsername() + "' registration is successful", HttpStatus.CREATED);

        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
