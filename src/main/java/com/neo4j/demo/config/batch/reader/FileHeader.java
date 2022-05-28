package com.neo4j.demo.config.batch.reader;

public class FileHeader {

    public static String[] customerHeader = {"CIF","Age","EmailAddress","FirstName","LastName","PhoneNumber","Gender",
            "Address","Country","JobTitle","CardNumber","AccountNumber"};

    public static String[] transferHeader = {"TransactionID","SenderAccountNumber","ReceiverAccountNumber","Amount",
            "TransferDatetime"};

    public static String[] purchaseHeader = {"TransactionID","CardNumber","Merchant","Amount","PurchaseDatetime","CardIssuer"};
}
