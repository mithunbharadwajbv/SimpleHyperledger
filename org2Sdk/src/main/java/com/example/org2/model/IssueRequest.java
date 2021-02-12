package com.example.org2.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class IssueRequest {

    private String paperId;
    private String issuer;
    private String issueDate;
    private Double faceValue;
    private String maturityDate;
    private String owner;
    private String state;
    private String price;

}
