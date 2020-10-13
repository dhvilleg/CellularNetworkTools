package com.arcotel.network.tools.data;

import android.content.ContentValues;

import com.arcotel.network.tools.contracts.ScanContract;

public class DeviceMetadata {
    private String deviceUUID;
    private String deviceModel;
    private String deviceOS;
    private String osVersion;
    private int isDualSim;
    private int fieldIsRegistered;


    public  DeviceMetadata(String deviceUUID,String deviceModel,String deviceOS,String osVersion,int isDualSim,int fieldIsRegistered){
        this.deviceUUID = deviceUUID;
        this.deviceModel = deviceModel;
        this.deviceOS = deviceOS;
        this.osVersion = osVersion;
        this.isDualSim = isDualSim;
        this.fieldIsRegistered = fieldIsRegistered;

    }
    public String getDeviceUUID(){return deviceUUID;}
    public String getDeviceModel(){return deviceModel;}
    public String getDeviceOS(){return deviceOS;}
    public String getOsVersion(){return osVersion;}
    public int getIsDualSim(){return isDualSim;}
    public int getFieldIsRegistered(){return fieldIsRegistered;}


    public ContentValues toContentValues() {
        ContentValues values = new ContentValues();
        values.put(ScanContract.DeviceContract.DEVICEUUID, deviceUUID);
        values.put(ScanContract.DeviceContract.DEVICEMODEL, deviceModel);
        values.put(ScanContract.DeviceContract.DEVICEOS, deviceOS);
        values.put(ScanContract.DeviceContract.OSVERSION, osVersion);
        values.put(ScanContract.DeviceContract.ISDUALSIM, isDualSim);
        values.put(ScanContract.DeviceContract.FIELDISREGISTERED, fieldIsRegistered);
        return values;
    }




}
