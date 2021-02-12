package main

import (
	"encoding/json"
	"fmt"
	"log"
	"bytes"
	"strconv"
	"time"

	"github.com/hyperledger/fabric-chaincode-go/shim"
	"github.com/hyperledger/fabric-contract-api-go/contractapi"
)

type CommercialPaper struct {
	PaperId      string  `json:"paperId"`
	Issuer       string  `json:"issuer"`
	IssueDate    string  `json:"issueDate"`
	FaceValue    float64 `json:"faceValue"`
	MaturityDate string  `json:"maturityDate"`
	Owner        string  `json:"owner"`
	State        string  `metadata:"state"`
	Price        float64  `metadata:"price"`
}

func (s *SmartContract) Issue(ctx contractapi.TransactionContextInterface, arg string) error {

	log.Printf("..... inside issue with args >>> " + arg)

	mspId, _ := ctx.GetClientIdentity().GetMSPID()

	var paper CommercialPaper

	err := json.Unmarshal([]byte(arg), &paper)
	if err != nil {
		return fmt.Errorf("Error unmarshaling commersial paper")
	}

	// check if the paper already exists
	paperAsBytes, _ := ctx.GetStub().GetState(paper.PaperId)
	if paperAsBytes != nil {
		return fmt.Errorf("Commercialpaper with id " + paper.PaperId + " already present please try with new Id")
	}

	// set issuer from msp
	paper.Issuer = mspId[:len(mspId)-3]
	// set owner should be same as issuer
	paper.Owner = paper.Issuer
	// set state to Issued
	paper.State = "Issued"

	//save the paper on the blockchain~
	paperAsBytes, _ = json.Marshal(paper)
	ctx.GetStub().PutState(paper.PaperId, paperAsBytes)

	log.Println("CommercialPaper issue successful")
	return nil

}

func (s *SmartContract) Sell(ctx contractapi.TransactionContextInterface, paperId string, newOwner string , newPrice string) error {

	log.Printf("..... inside Sell with paperId %s  >>> ", paperId)

	mspId, _ := ctx.GetClientIdentity().GetMSPID()

	//convert newprice from string to float64
	newPricefloat,_ := strconv.ParseFloat(newPrice, 64)

	var paper CommercialPaper

	//check validity of paperId request
	paperAsBytes, _ := ctx.GetStub().GetState(paperId)
	if paperAsBytes == nil {
		return fmt.Errorf("no Commercialpaper with id %s present" , paper.PaperId)
	}

	json.Unmarshal(paperAsBytes, &paper)

	//only owner can issue sell operation
	if paper.Owner != mspId[:len(mspId)-3] {
		return fmt.Errorf("only %s can initiate this sell operation but its is initiated by %s", paper.Owner ,mspId[:len(mspId)-3] )
	}

	//cant sell to issuer
	if paper.Issuer == newOwner {
		return fmt.Errorf("Commercialpaper cant be sold to the issuer ")
	}

	//cant be sold to themself
	if paper.Owner == newOwner {
		return fmt.Errorf("you already own this Commercialpaper ")
	}

	//Commercialpaper has to be in state Trading or Issued (redeemed paper cannot be sold)
	if ( paper.State == "Redeemed") {
		return fmt.Errorf("State of the Commercialpaper has to be Issued or Trading but it is %s", paper.State)
	}

	//set parameters of buy (owner,state,price)
	paper.Owner = newOwner
	paper.State = "Trading"
	paper.Price = newPricefloat

	//marshal and save to blockchain
	paperAsBytes, _ = json.Marshal(paper)
	ctx.GetStub().PutState(paper.PaperId, paperAsBytes)

	log.Println("CommercialPaper Sell successful")
	return nil
}

func (s *SmartContract) Redeem(ctx contractapi.TransactionContextInterface, paperId string, todaysdate string) error {

	log.Printf("..... inside Redeem with paperId %s and todaysdate %s  >>> ", paperId, todaysdate)

	mspId, _ := ctx.GetClientIdentity().GetMSPID()

	var paper CommercialPaper

	//check validity of paperId request
	paperAsBytes, _ := ctx.GetStub().GetState(paperId)
	if paperAsBytes == nil {
		return fmt.Errorf("No Commercialpaper with id " + paperId + " present")
	}

	json.Unmarshal(paperAsBytes, &paper)

	//validate maturitydate should be less than todays date
	log.Println(todaysdate)

	//Commercialpaper has to be in state Trading or Issued (redeemed paper cannot be sold)
	if ( paper.State == "Redeemed") {
		return fmt.Errorf("This Commercialpaper with id %s has already been redeemed", paper.PaperId)
	}

	//only owner of paper can initiate this transaction
	if paper.Owner != mspId[:len(mspId)-3] {
		return fmt.Errorf("only %s can initite this redeem", paper.Owner)
	}

	//set parameters of redeem (owner,state)
	paper.Owner = paper.Issuer
	paper.State = "Redeemed"

	//marshal and save to blockchain
	paperAsBytes, _ = json.Marshal(paper)
	ctx.GetStub().PutState(paper.PaperId, paperAsBytes)

	//save transaction to blockchain

	log.Println("CommercialPaper Redeem successful")
	return nil
}

func (s *SmartContract) GetQueryResultForQueryString(ctx contractapi.TransactionContextInterface, queryString string) (string, error) {

	fmt.Printf("- getQueryResultForQueryString queryString:\n%s\n", queryString)
	fmt.Println(queryString)

	resultsIterator, err := ctx.GetStub().GetQueryResult(queryString)
	if err != nil {
		return "", err
	}
	defer resultsIterator.Close()

	buffer, err := constructQueryResponseFromIterator(resultsIterator)
	if err != nil {
		return "", err
	}

	fmt.Printf("- getQueryResultForQueryString queryResult:\n%s\n", buffer.String())

	return string(buffer.Bytes()), nil
}

func constructQueryResponseFromIterator(resultsIterator shim.StateQueryIteratorInterface) (*bytes.Buffer, error) {
    // buffer is a JSON array containing QueryResults
    var buffer bytes.Buffer
    buffer.WriteString("[")

    bArrayMemberAlreadyWritten := false
    for resultsIterator.HasNext() {
        queryResponse, err := resultsIterator.Next()
        if err != nil {
            return nil, err
        }
        // Add a comma before array members, suppress it for the first array member
        if bArrayMemberAlreadyWritten == true {
            buffer.WriteString(",")
        }       
        // Record is a JSON object, so we write as-is
        buffer.WriteString(string(queryResponse.Value))
        bArrayMemberAlreadyWritten = true
    }
    buffer.WriteString("]")

    return &buffer, nil
}

func (s *SmartContract) GetPaperHistory(ctx contractapi.TransactionContextInterface, paperId string) (string, error) {
	log.Printf("..... inside GetPaperHistory with paperId %s  >>> ", paperId)

	resultsIterator, err := ctx.GetStub().GetHistoryForKey(paperId)
	if err != nil {
		return "", fmt.Errorf(err.Error())
	}

	defer resultsIterator.Close()

	var buffer bytes.Buffer
	buffer.WriteString("[")

	bArrayMemberAlreadyWritten := false
	for resultsIterator.HasNext() {
		response, err := resultsIterator.Next()
		if err != nil {
			return "", fmt.Errorf("does not exist")
		}
		// Add a comma before array members, suppress it for the first array member
		if bArrayMemberAlreadyWritten == true {
			buffer.WriteString(",")
		}
		buffer.WriteString("{\"TxId\":")
		buffer.WriteString("\"")
		buffer.WriteString(response.TxId)
		buffer.WriteString("\"")

		buffer.WriteString(", \"Value\":")
		// if it was a delete operation on given key, then we need to set the
		//corresponding value null. Else, we will write the response.Value
		//as-is (as the Value itself a JSON marble)
		if response.IsDelete {
			buffer.WriteString("null")
		} else {
			buffer.WriteString(string(response.Value))
		}

		buffer.WriteString(", \"Timestamp\":")
		buffer.WriteString("\"")
		buffer.WriteString(time.Unix(response.Timestamp.Seconds, int64(response.Timestamp.Nanos)).String())
		buffer.WriteString("\"")

		buffer.WriteString(", \"IsDelete\":")
		buffer.WriteString("\"")
		buffer.WriteString(strconv.FormatBool(response.IsDelete))
		buffer.WriteString("\"")

		buffer.WriteString("}")
		bArrayMemberAlreadyWritten = true
	}
	buffer.WriteString("]")

	return string(buffer.Bytes()), nil	
}