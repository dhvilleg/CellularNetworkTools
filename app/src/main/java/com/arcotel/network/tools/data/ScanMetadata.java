package com.arcotel.network.tools.data;

import android.content.ContentValues;

import com.arcotel.network.tools.contracts.ScanContract;

public class ScanMetadata {

    private String timestamp;
    private String deviceUUID;
    private String ratingCounter;
    private int simNumberID;
    private String countryISO;
    private String phoneOperatorId;
    private String simOperatorId;
    private String operatorMcc;
    private String operatorMnc;
    private String devManufacturer;
    private String devModel;
    private String isConected;
    private String phoneNetStandard;
    private String phoneNetTechnology;
    private String internetConNetwork;
    private String latitude;
    private String longitude;
    private String pingTimeMilis;
    private String downloadSpeed;
    private String uploadSpeed;
    private String ipPublicAddr;
    private String internetISP;
    private String phoneSignalStrength;
    private String phoneAsuStrength;
    private String phoneSignalLevel;
    private String signalQuality;
    private int fieldIsRegistered;
    private String phoneRsrpStrength;
    private String phoneRssnrStrength;
    private String phoneTimingAdvance;
    private String phoneCqiStrength;
    private String phoneRsrqStrength;
    private String cellLtePci;
    private String cellLteCid;
    private String cellLteTac;
    private String cellLteeNodeB;
    private String cellLteEarfcn;
    private String cellBslat;
    private String cellBslon;
    private String cellSid;
    private String cellNid;
    private String cellBid;
    private String cellWcdmaLac;
    private String cellWcdmaUcid;
    private String cellWcdmaUarfcn;
    private String cellWcdmaPsc;
    private String cellWcdmaCid;
    private String cellWcdmaRnc;
    private String cellGsmArcfn;
    private String cellGsmLac;
    private String cellGsmCid;

    public ScanMetadata(String timestamp,String deviceUUID,String ratingCounter,int simNumberID,String countryISO,String phoneOperatorId,String simOperatorId,String operatorMcc,String operatorMnc,String devManufacturer,String devModel,String isConected,String phoneNetStandard,String phoneNetTechnology,String internetConNetwork,String latitude,String longitude,String pingTimeMilis,String downloadSpeed,String uploadSpeed,String ipPublicAddr,String internetISP,String phoneSignalStrength,String phoneAsuStrength,String phoneSignalLevel,String signalQuality,int fieldIsRegistered,String phoneRsrpStrength,String phoneRssnrStrength,String phoneTimingAdvance,String phoneCqiStrength,String phoneRsrqStrength,String cellLtePci,String cellLteCid,String cellLteTac,String cellLteeNodeB,String cellLteEarfcn,String cellBslat,String cellBslon,String cellSid,String cellNid,String cellBid,String cellWcdmaLac,String cellWcdmaUcid,String cellWcdmaUarfcn,String cellWcdmaPsc,String cellWcdmaCid,String cellWcdmaRnc,String cellGsmArcfn,String cellGsmLac,String cellGsmCid){
        this.timestamp = timestamp;
        this.deviceUUID = deviceUUID;
        this.ratingCounter = ratingCounter;
        this.simNumberID = simNumberID;
        this.countryISO = countryISO;
        this.phoneOperatorId = phoneOperatorId;
        this.simOperatorId = simOperatorId;
        this.operatorMcc = operatorMcc;
        this.operatorMnc = operatorMnc;
        this.devManufacturer = devManufacturer;
        this.devModel = devModel;
        this.isConected = isConected;
        this.phoneNetStandard = phoneNetStandard;
        this.phoneNetTechnology = phoneNetTechnology;
        this.internetConNetwork = internetConNetwork;
        this.latitude = latitude;
        this.longitude = longitude;
        this.pingTimeMilis = pingTimeMilis;
        this.downloadSpeed = downloadSpeed;
        this.uploadSpeed = uploadSpeed;
        this.ipPublicAddr = ipPublicAddr;
        this.internetISP = internetISP;
        this.phoneSignalStrength = phoneSignalStrength;
        this.phoneAsuStrength = phoneAsuStrength;
        this.phoneSignalLevel = phoneSignalLevel;
        this.signalQuality = signalQuality;
        this.fieldIsRegistered = fieldIsRegistered;
        this.phoneRsrpStrength = phoneRsrpStrength;
        this.phoneRssnrStrength = phoneRssnrStrength;
        this.phoneTimingAdvance = phoneTimingAdvance;
        this.phoneCqiStrength = phoneCqiStrength;
        this.phoneRsrqStrength = phoneRsrqStrength;
        this.cellLtePci = cellLtePci;
        this.cellLteCid = cellLteCid;
        this.cellLteTac = cellLteTac;
        this.cellLteeNodeB = cellLteeNodeB;
        this.cellLteEarfcn = cellLteEarfcn;
        this.cellBslat = cellBslat;
        this.cellBslon = cellBslon;
        this.cellSid = cellSid;
        this.cellNid = cellNid;
        this.cellBid = cellBid;
        this.cellWcdmaLac = cellWcdmaLac;
        this.cellWcdmaUcid = cellWcdmaUcid;
        this.cellWcdmaUarfcn = cellWcdmaUarfcn;
        this.cellWcdmaPsc = cellWcdmaPsc;
        this.cellWcdmaCid = cellWcdmaCid;
        this.cellWcdmaRnc = cellWcdmaRnc;
        this.cellGsmArcfn = cellGsmArcfn;
        this.cellGsmLac = cellGsmLac;
        this.cellGsmCid = cellGsmCid;
    }

    public String getTimestamp(){return timestamp;}
    public String getDeviceUUID(){return deviceUUID;}
    public String getRatingCounter(){return ratingCounter;}
    public int getSimNumberID(){return simNumberID;}
    public String getCountryISO(){return countryISO;}
    public String getPhoneOperatorId(){return phoneOperatorId;}
    public String getSimOperatorId(){return simOperatorId;}
    public String getOperatorMcc(){return operatorMcc;}
    public String getOperatorMnc(){return operatorMnc;}
    public String getDevManufacturer(){return devManufacturer;}
    public String getDevModel(){return devModel;}
    public String getIsConected(){return isConected;}
    public String getPhoneNetStandard(){return phoneNetStandard;}
    public String getPhoneNetTechnology(){return phoneNetTechnology;}
    public String getInternetConNetwork(){return internetConNetwork;}
    public String getLatitude(){return latitude;}
    public String getLongitude(){return longitude;}
    public String getPingTimeMilis(){return pingTimeMilis;}
    public String getDownloadSpeed(){return downloadSpeed;}
    public String getUploadSpeed(){return uploadSpeed;}
    public String getIpPublicAddr(){return ipPublicAddr;}
    public String getInternetISP(){return internetISP;}
    public String getPhoneSignalStrength(){return phoneSignalStrength;}
    public String getPhoneAsuStrength(){return phoneAsuStrength;}
    public String getPhoneSignalLevel(){return phoneSignalLevel;}
    public String getSignalQuality(){return signalQuality;}
    public int getFieldIsRegistered(){return fieldIsRegistered;}
    public String getPhoneRsrpStrength(){return phoneRsrpStrength;}
    public String getPhoneRssnrStrength(){return phoneRssnrStrength;}
    public String getPhoneTimingAdvance(){return phoneTimingAdvance;}
    public String getPhoneCqiStrength(){return phoneCqiStrength;}
    public String getPhoneRsrqStrength(){return phoneRsrqStrength;}
    public String getCellLtePci(){return cellLtePci;}
    public String getCellLteCid(){return cellLteCid;}
    public String getCellLteTac(){return cellLteTac;}
    public String getCellLteeNodeB(){return cellLteeNodeB;}
    public String getCellLteEarfcn(){return cellLteEarfcn;}
    public String getCellBslat(){return cellBslat;}
    public String getCellBslon(){return cellBslon;}
    public String getCellSid(){return cellSid;}
    public String getCellNid(){return cellNid;}
    public String getCellBid(){return cellBid;}
    public String getCellWcdmaLac(){return cellWcdmaLac;}
    public String getCellWcdmaUcid(){return cellWcdmaUcid;}
    public String getCellWcdmaUarfcn(){return cellWcdmaUarfcn;}
    public String getCellWcdmaPsc(){return cellWcdmaPsc;}
    public String getCellWcdmaCid(){return cellWcdmaCid;}
    public String getCellWcdmaRnc(){return cellWcdmaRnc;}
    public String getCellGsmArcfn(){return cellGsmArcfn;}
    public String getCellGsmLac(){return cellGsmLac;}
    public String getCellGsmCid(){return cellGsmCid;}

    public ContentValues toContentValues(){
        ContentValues values = new ContentValues();
        values.put(ScanContract.ScanEntry.TIMESTAMP, timestamp);
        values.put(ScanContract.ScanEntry.DEVICEUUID, deviceUUID);
        values.put(ScanContract.ScanEntry.RATINGCOUNTER, ratingCounter);
        values.put(ScanContract.ScanEntry.SIMNUMBERID, simNumberID);
        values.put(ScanContract.ScanEntry.COUNTRYISO, countryISO);
        values.put(ScanContract.ScanEntry.PHONEOPERATORID, phoneOperatorId);
        values.put(ScanContract.ScanEntry.SIMOPERATORID, simOperatorId);
        values.put(ScanContract.ScanEntry.OPERATORMCC, operatorMcc);
        values.put(ScanContract.ScanEntry.OPERATORMNC, operatorMnc);
        values.put(ScanContract.ScanEntry.DEVMANUFACTURER, devManufacturer);
        values.put(ScanContract.ScanEntry.DEVMODEL, devModel);
        values.put(ScanContract.ScanEntry.ISCONECTED, isConected);
        values.put(ScanContract.ScanEntry.PHONENETSTANDARD, phoneNetStandard);
        values.put(ScanContract.ScanEntry.PHONENETTECHNOLOGY, phoneNetTechnology);
        values.put(ScanContract.ScanEntry.INTERNETCONNETWORK, internetConNetwork);
        values.put(ScanContract.ScanEntry.LATITUDE, latitude);
        values.put(ScanContract.ScanEntry.LONGITUDE, longitude);
        values.put(ScanContract.ScanEntry.PINGTIMEMILIS, pingTimeMilis);
        values.put(ScanContract.ScanEntry.DOWNLOADSPEED, downloadSpeed);
        values.put(ScanContract.ScanEntry.UPLOADSPEED, uploadSpeed);
        values.put(ScanContract.ScanEntry.IPPUBLICADDR, ipPublicAddr);
        values.put(ScanContract.ScanEntry.INTERNETISP, internetISP);
        values.put(ScanContract.ScanEntry.PHONESIGNALSTRENGTH, phoneSignalStrength);
        values.put(ScanContract.ScanEntry.PHONEASUSTRENGTH, phoneAsuStrength);
        values.put(ScanContract.ScanEntry.PHONESIGNALLEVEL, phoneSignalLevel);
        values.put(ScanContract.ScanEntry.SIGNALQUALITY, signalQuality);
        values.put(ScanContract.ScanEntry.FIELDISREGISTERED, fieldIsRegistered);
        values.put(ScanContract.ScanEntry.PHONERSRPSTRENGTH, phoneRsrpStrength);
        values.put(ScanContract.ScanEntry.PHONERSSNRSTRENGTH, phoneRssnrStrength);
        values.put(ScanContract.ScanEntry.PHONETIMINGADVANCE, phoneTimingAdvance);
        values.put(ScanContract.ScanEntry.PHONECQISTRENGTH, phoneCqiStrength);
        values.put(ScanContract.ScanEntry.PHONERSRQSTRENGTH, phoneRsrqStrength);
        values.put(ScanContract.ScanEntry.CELLLTEPCI, cellLtePci);
        values.put(ScanContract.ScanEntry.CELLLTECID, cellLteCid);
        values.put(ScanContract.ScanEntry.CELLLTETAC, cellLteTac);
        values.put(ScanContract.ScanEntry.CELLLTEENODEB, cellLteeNodeB);
        values.put(ScanContract.ScanEntry.CELLLTEEARFCN, cellLteEarfcn);
        values.put(ScanContract.ScanEntry.CELLBSLAT, cellBslat);
        values.put(ScanContract.ScanEntry.CELLBSLON, cellBslon);
        values.put(ScanContract.ScanEntry.CELLSID, cellSid);
        values.put(ScanContract.ScanEntry.CELLNID, cellNid);
        values.put(ScanContract.ScanEntry.CELLBID, cellBid);
        values.put(ScanContract.ScanEntry.CELLWCDMALAC, cellWcdmaLac);
        values.put(ScanContract.ScanEntry.CELLWCDMAUCID, cellWcdmaUcid);
        values.put(ScanContract.ScanEntry.CELLWCDMAUARFCN, cellWcdmaUarfcn);
        values.put(ScanContract.ScanEntry.CELLWCDMAPSC, cellWcdmaPsc);
        values.put(ScanContract.ScanEntry.CELLWCDMACID, cellWcdmaCid);
        values.put(ScanContract.ScanEntry.CELLWCDMARNC, cellWcdmaRnc);
        values.put(ScanContract.ScanEntry.CELLGSMARCFN, cellGsmArcfn);
        values.put(ScanContract.ScanEntry.CELLGSMLAC, cellGsmLac);
        values.put(ScanContract.ScanEntry.CELLGSMCID, cellGsmCid);
        return values;
    }
}
