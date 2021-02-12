package com.example.org1.service.implementation;

import com.example.org1.model.AppUser;
import com.example.org1.utils.UserGatewayConnection;
import org.hyperledger.fabric.gateway.Gateway;
import org.hyperledger.fabric.gateway.Wallet;
import org.hyperledger.fabric.gateway.Wallets;
import org.hyperledger.fabric.gateway.Identity;
import org.hyperledger.fabric.sdk.Enrollment;
import org.hyperledger.fabric.sdk.User;
import org.hyperledger.fabric.sdk.security.CryptoSuite;
import org.hyperledger.fabric.sdk.security.CryptoSuiteFactory;
import org.hyperledger.fabric_ca.sdk.EnrollmentRequest;
import org.hyperledger.fabric_ca.sdk.Attribute;
import org.hyperledger.fabric_ca.sdk.HFCAClient;
import org.hyperledger.fabric.gateway.X509Identity;
import org.hyperledger.fabric.gateway.Identities;
import org.hyperledger.fabric_ca.sdk.RegistrationRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.security.cert.X509Certificate;


import javax.annotation.PostConstruct;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.PrivateKey;
import java.util.Properties;
import java.util.Set;

@Service
public class UserServiceImpl {

    private static Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Value("${org.name}")
    private String orgName;

    @Value("${org.host}")
    private String orgHost;

    @Value("${org.ca.port}")
    private int caPort;

    @Autowired
    private UserGatewayConnection userGatewayConnection;

    public String getOrgName() {
        return orgName.substring(0, 1).toUpperCase() + orgName.substring(1);
    }

    @PostConstruct
    void initialize(){
        try {
            initializeConnectionForExistingUsers();
        }catch (Exception e){
            logger.error("Error initializing connections: "+e.getMessage());
        }
    }


    public void addConnection(String userName) throws Exception{
        // Create a wallet for managing identities
        Wallet wallet = Wallets.newFileSystemWallet(Paths.get("wallet"));
        Path networkConfigPath = Paths.get( "./../test-network/organizations/peerOrganizations/org1.example.com/connection-org1.yaml");
        Gateway.Builder builder = Gateway.createBuilder();
        builder.identity(wallet, userName).networkConfig(networkConfigPath).discovery(true);
        Gateway gateway = builder.connect();
        userGatewayConnection.addUser(new AppUser(userName, gateway));
        logger.info("Connection for user: "+userName+" saved successfully");
    }


    public void initializeConnectionForExistingUsers() throws Exception{
        // Create a wallet for managing identities
        Wallet wallet = Wallets.newFileSystemWallet(Paths.get("wallet"));
        for(String user: wallet.list()){
            addConnection(user);
        }
    }


    public void reConnection(String userName) throws Exception {
        addConnection(userName);
    }


    public HFCAClient getCAClient() throws Exception {
        // Create a CA client for interacting with the CA.
        // TODO: Add these configuration info in resource file and access it
        Properties props = new Properties();
        String currentDirectory = System.getProperty("user.dir");
        System.out.println("The current working directory is mithun" + currentDirectory);
        props.put("pemFile",
                "./../test-network/organizations/peerOrganizations/org1.example.com/ca/ca.org1.example.com-cert.pem");
        props.put("allowAllHostNames", "true");

        HFCAClient caClient = HFCAClient.createNewInstance("https://"+orgHost+":"+caPort, props);

        CryptoSuite cryptoSuite = CryptoSuiteFactory.getDefault().getCryptoSuite();

        caClient.setCryptoSuite(cryptoSuite);

        return caClient;
    }


    public void enrollAdmin() throws Exception {

        logger.info("Class: " + this.getClass().getName() + " Function: enrollAdmin");

        HFCAClient caClient = getCAClient();

        // Create a wallet for managing identities
        Wallet wallet = Wallets.newFileSystemWallet(Paths.get("wallet"));

        // Check to see if we've already enrolled the admin user.
        Identity adminExists = wallet.get("admin");
        if (adminExists != null) {
            logger.info("An identity for the admin user \"admin\" already exists in the wallet");
            throw new Exception("An identity for the admin user \"admin\" already exists in the wallet");
        }

        // Enroll the admin user, and import the new identity into the wallet.
        final EnrollmentRequest enrollmentRequestTLS = new EnrollmentRequest();
        enrollmentRequestTLS.addHost(orgHost);
        enrollmentRequestTLS.setProfile("tls");
        System.out.println("12");
        Enrollment enrollment = caClient.enroll("admin", "adminpw", enrollmentRequestTLS);
        X509Identity user = Identities.newX509Identity(getOrgName() + "MSP", enrollment);
        wallet.put("admin", user);
        logger.info("Successfully enrolled user \"admin\" and imported it into the wallet");
        addConnection("admin");

    }


    public void registerUser(String userName , String role ,String department) throws Exception {

        logger.info("Class: " + this.getClass().getName() + " Function: registerUser");
        String orgNameCapitalized = orgName.substring(0, 1).toUpperCase() + orgName.substring(1);

        HFCAClient caClient = getCAClient();

        // Create a wallet for managing identities
        Wallet wallet = Wallets.newFileSystemWallet(Paths.get("wallet"));

        // Check to see if we've already enrolled the user.
        Identity userExists = wallet.get(userName);
        if (userExists != null) {
            logger.error("An identity for the user \"" + userName + "\" already exists in the wallet");
            throw new Exception("An identity for the user \"" + userName + "\" already exists in the wallet");
        }

        userExists = wallet.get("admin");
        if (userExists == null ) {
            logger.error("\"admin\" needs to be enrolled and added to the wallet first");
            throw new Exception("\"admin\" needs to be enrolled and added to the wallet first");
        }

        X509Identity adminIdentity = (X509Identity) wallet.get("admin");
        User admin = new User() {

            @Override
            public String getName() {
                return "admin";
            }

            @Override
            public Set<String> getRoles() {
                return null;
            }

            @Override
            public String getAccount() {
                return null;
            }

            @Override
            public String getAffiliation() {
                return orgName+"."+department.toLowerCase();
            }

            @Override
            public Enrollment getEnrollment() {
                return new Enrollment() {

                    @Override
                    public PrivateKey getKey() {
                        return adminIdentity.getPrivateKey();
                    }

                    @Override
                    public String getCert() {
                        return Identities.toPemString(adminIdentity.getCertificate());
                    }
                };
            }

            @Override
            public String getMspId() {
                return orgNameCapitalized + "MSP";
            }

        };

        // Register the user, enroll the user, and import the new identity into the wallet.
        RegistrationRequest registrationRequest = new RegistrationRequest(userName);
        registrationRequest.setAffiliation(admin.getAffiliation());
        registrationRequest.setEnrollmentID(userName);
        registrationRequest.addAttribute(new Attribute("Role",role));
        String enrollmentSecret = caClient.register(registrationRequest, admin);
        Enrollment enrollment = caClient.enroll(userName, enrollmentSecret);

        X509Identity user = Identities.newX509Identity(admin.getMspId(), enrollment);
        wallet.put(userName, user);
        logger.info("Successfully enrolled user \"" + userName + "\" and imported it into the wallet");
        addConnection(userName);
    }

    public void loginUser(String userName) throws Exception {

        logger.info("Class: " + this.getClass().getName() + " Function: loginUser");

        HFCAClient caClient = getCAClient();

        // Create a wallet for managing identities
        Wallet wallet = Wallets.newFileSystemWallet(Paths.get("wallet"));
        // Check to see if we've already enrolled the user.
        Identity userExists = wallet.get(userName);
        if (userExists == null) {
            logger.error("An identity for the user \"" + userName + "\" not exists in the wallet");
            throw new Exception("An identity for the user \"" + userName + "\" not exists in the wallet");
        }
    }


}
