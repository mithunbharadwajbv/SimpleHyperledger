package com.example.org2.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TempUser {

    private String docType;
    private String userID;
    private String role;
    private String name;
    private Integer roleLevel;

}
