package com.example.org2.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserWallet {

    private TempUser user;
    private String walletID;

}
