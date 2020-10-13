package com.arcotel.network.tools.data;

import android.content.ContentValues;

import com.arcotel.network.tools.contracts.ScanContract;

public class ErrorCodesMetadata {
    private String deviceUUID;
    private String errorCodeIdentifier;
    private String errorCodeDetail;
    private int fieldIsRegistered;


    public ErrorCodesMetadata(String deviceUUID,String errorCodeIdentifier,String errorCodeDetail,int fieldIsRegistered){
        this.deviceUUID = deviceUUID;
        this.errorCodeIdentifier = errorCodeIdentifier;
        this.errorCodeDetail = errorCodeDetail;
        this.fieldIsRegistered = fieldIsRegistered;

    }
    public String getDeviceUUID(){return deviceUUID;}
    public String getErrorCodeIdentifier(){return errorCodeIdentifier;}
    public String getErrorCodeDetail(){return errorCodeDetail;}
    public int getFieldIsRegistered(){return fieldIsRegistered;}


    public ContentValues toContentValues() {
        ContentValues values = new ContentValues();
        values.put(ScanContract.ErrorCodesContract.DEVICEUUID, deviceUUID);
        values.put(ScanContract.ErrorCodesContract.ERRORCODEIDENTIFIER, errorCodeIdentifier);
        values.put(ScanContract.ErrorCodesContract.ERRORCODEDETAIL, errorCodeDetail);
        values.put(ScanContract.ErrorCodesContract.FIELDISREGISTERED, fieldIsRegistered);

        return values;
    }



}
