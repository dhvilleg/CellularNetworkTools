package com.arcotel.network.tools;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.arcotel.network.tools.contracts.ScanContract;
import com.arcotel.network.tools.data.DeviceMetadata;
import com.arcotel.network.tools.data.ErrorCodesMetadata;
import com.arcotel.network.tools.data.ScanMetadata;

import java.util.ArrayList;

public class AntDbHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "ScanArcotelDemo.db";

    public AntDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + ScanContract.ScanEntry.TABLE_NAME + " ("
                + ScanContract.ScanEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + ScanContract.ScanEntry.TIMESTAMP + "TEXT NOT NULL,"
                + ScanContract.ScanEntry.DEVICEUUID + "TEXT NOT NULL,"
                + ScanContract.ScanEntry.RATINGCOUNTER + "TEXT NOT NULL,"
                + ScanContract.ScanEntry.SIMNUMBERID + "INTEGER NOT NULL,"
                + ScanContract.ScanEntry.COUNTRYISO + "TEXT NOT NULL,"
                + ScanContract.ScanEntry.PHONEOPERATORID + "TEXT NOT NULL,"
                + ScanContract.ScanEntry.SIMOPERATORID + "TEXT NOT NULL,"
                + ScanContract.ScanEntry.OPERATORMCC + "TEXT NOT NULL,"
                + ScanContract.ScanEntry.OPERATORMNC + "TEXT NOT NULL,"
                + ScanContract.ScanEntry.DEVMANUFACTURER + "TEXT NOT NULL,"
                + ScanContract.ScanEntry.DEVMODEL + "TEXT NOT NULL,"
                + ScanContract.ScanEntry.ISCONECTED + "TEXT NOT NULL,"
                + ScanContract.ScanEntry.PHONENETSTANDARD + "TEXT NOT NULL,"
                + ScanContract.ScanEntry.PHONENETTECHNOLOGY + "TEXT NOT NULL,"
                + ScanContract.ScanEntry.INTERNETCONNETWORK + "TEXT NOT NULL,"
                + ScanContract.ScanEntry.LATITUDE + "TEXT NOT NULL,"
                + ScanContract.ScanEntry.LONGITUDE + "TEXT NOT NULL,"
                + ScanContract.ScanEntry.PINGTIMEMILIS + "TEXT NOT NULL,"
                + ScanContract.ScanEntry.DOWNLOADSPEED + "TEXT NOT NULL,"
                + ScanContract.ScanEntry.UPLOADSPEED + "TEXT NOT NULL,"
                + ScanContract.ScanEntry.IPPUBLICADDR + "TEXT NOT NULL,"
                + ScanContract.ScanEntry.INTERNETISP + "TEXT NOT NULL,"
                + ScanContract.ScanEntry.PHONESIGNALSTRENGTH + "TEXT NOT NULL,"
                + ScanContract.ScanEntry.PHONEASUSTRENGTH + "TEXT NOT NULL,"
                + ScanContract.ScanEntry.PHONESIGNALLEVEL + "TEXT NOT NULL,"
                + ScanContract.ScanEntry.SIGNALQUALITY + "TEXT NOT NULL,"
                + ScanContract.ScanEntry.FIELDISREGISTERED + "INTEGER NOT NULL,"
                + ScanContract.ScanEntry.PHONERSRPSTRENGTH + "TEXT NOT NULL,"
                + ScanContract.ScanEntry.PHONERSSNRSTRENGTH + "TEXT NOT NULL,"
                + ScanContract.ScanEntry.PHONETIMINGADVANCE + "TEXT NOT NULL,"
                + ScanContract.ScanEntry.PHONECQISTRENGTH + "TEXT NOT NULL,"
                + ScanContract.ScanEntry.PHONERSRQSTRENGTH + "TEXT NOT NULL,"
                + ScanContract.ScanEntry.CELLLTEPCI + "TEXT NOT NULL,"
                + ScanContract.ScanEntry.CELLLTECID + "TEXT NOT NULL,"
                + ScanContract.ScanEntry.CELLLTETAC + "TEXT NOT NULL,"
                + ScanContract.ScanEntry.CELLLTEENODEB + "TEXT NOT NULL,"
                + ScanContract.ScanEntry.CELLLTEEARFCN + "TEXT NOT NULL,"
                + ScanContract.ScanEntry.CELLBSLAT + "TEXT NOT NULL,"
                + ScanContract.ScanEntry.CELLBSLON + "TEXT NOT NULL,"
                + ScanContract.ScanEntry.CELLSID + "TEXT NOT NULL,"
                + ScanContract.ScanEntry.CELLNID + "TEXT NOT NULL,"
                + ScanContract.ScanEntry.CELLBID + "TEXT NOT NULL,"
                + ScanContract.ScanEntry.CELLWCDMALAC + "TEXT NOT NULL,"
                + ScanContract.ScanEntry.CELLWCDMAUCID + "TEXT NOT NULL,"
                + ScanContract.ScanEntry.CELLWCDMAUARFCN + "TEXT NOT NULL,"
                + ScanContract.ScanEntry.CELLWCDMAPSC + "TEXT NOT NULL,"
                + ScanContract.ScanEntry.CELLWCDMACID + "TEXT NOT NULL,"
                + ScanContract.ScanEntry.CELLWCDMARNC + "TEXT NOT NULL,"
                + ScanContract.ScanEntry.CELLGSMARCFN + "TEXT NOT NULL,"
                + ScanContract.ScanEntry.CELLGSMLAC + "TEXT NOT NULL,"
                + ScanContract.ScanEntry.CELLGSMCID + "TEXT NOT NULL,"
                + "UNIQUE (" + ScanContract.ScanEntry._ID + "))");

        db.execSQL("CREATE TABLE " + ScanContract.DeviceContract.TABLE_NAME + " ("
                + ScanContract.DeviceContract.DEVICEUUID + "TEXT NOT NULL,"
                + ScanContract.DeviceContract.DEVICEMODEL + "TEXT NOT NULL,"
                + ScanContract.DeviceContract.DEVICEOS + "TEXT NOT NULL,"
                + ScanContract.DeviceContract.OSVERSION + "TEXT NOT NULL,"
                + ScanContract.DeviceContract.ISDUALSIM + "INTEGER NOT NULL,"
                + ScanContract.DeviceContract.FIELDISREGISTERED + "INTEGER NOT NULL,"
                + "UNIQUE (" + ScanContract.DeviceContract.DEVICEUUID + "))");

        db.execSQL("CREATE TABLE " + ScanContract.ErrorCodesContract.TABLE_NAME + " ("
                + ScanContract.ErrorCodesContract._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + ScanContract.ErrorCodesContract.DEVICEUUID + "TEXT NOT NULL,"
                + ScanContract.ErrorCodesContract.ERRORCODEIDENTIFIER + "TEXT NOT NULL,"
                + ScanContract.ErrorCodesContract.ERRORCODEDETAIL + "TEXT NOT NULL,"
                + ScanContract.ErrorCodesContract.FIELDISREGISTERED + "INTEGER NOT NULL,"
                + "UNIQUE (" + ScanContract.ErrorCodesContract._ID + "))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        /** TODO Implement onUpgrade funtion in order to update new functionalities
        db.execSQL("DROP TABLE IF EXISTS "+ScanContract.ScanEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS "+ScanContract.DeviceContract.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS "+ScanContract.ErrorCodesContract.TABLE_NAME);
        onCreate(db);
         **/
    }

    /**Sección para insertar datos en las tablas**/
    public void saveCellularSqlScan(ScanMetadata scanMetadata) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        Log.d("SqlLite","entra a insert SqlLite");
        sqLiteDatabase.insert(ScanContract.ScanEntry.TABLE_NAME,null,scanMetadata.toContentValues());
    }
    public void saveDeviceSqlScan(DeviceMetadata deviceMetadata) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        Log.d("SqlLite","entra a insert SqlLite");
        sqLiteDatabase.insert(ScanContract.DeviceContract.TABLE_NAME,null,deviceMetadata.toContentValues());
    }
    public void saveErrorCodesSqlScan(ErrorCodesMetadata errorCodesMetadata) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        Log.d("SqlLite","entra a insert SqlLite");
        sqLiteDatabase.insert(ScanContract.ErrorCodesContract.TABLE_NAME,null,errorCodesMetadata.toContentValues());
    }

    /**Sección para Consultas**/
    public Cursor getCellularInfoByIsRegistered(int isRegistered) {
        Log.d("LteInfoByIsRegistered","Entra al metodo");
        Cursor c = getReadableDatabase().query(
                ScanContract.ScanEntry.TABLE_NAME,
                null,
                ScanContract.ScanEntry.FIELDISREGISTERED + " LIKE ?",new String[]{String.valueOf(isRegistered)},
                null,
                null,
                null);
        return c;
    }
    public Cursor getDeviceInfoByIsRegistered(int isRegistered) {
        Log.d("LteInfoByIsRegistered","Entra al metodo");
        Cursor c = getReadableDatabase().query(
                ScanContract.DeviceContract.TABLE_NAME,
                null,
                ScanContract.DeviceContract.FIELDISREGISTERED + " LIKE ?",new String[]{String.valueOf(isRegistered)},
                null,
                null,
                null);
        return c;
    }
    public Cursor getErrorCodeInfoByIsRegistered(int isRegistered) {
        Log.d("LteInfoByIsRegistered","Entra al metodo");
        Cursor c = getReadableDatabase().query(
                ScanContract.ErrorCodesContract.TABLE_NAME,
                null,
                ScanContract.ErrorCodesContract.FIELDISREGISTERED + " LIKE ?",new String[]{String.valueOf(isRegistered)},
                null,
                null,
                null);
        return c;
    }

    public Cursor getAllCellularInfo() {
        return getReadableDatabase()
                .query(
                        ScanContract.ScanEntry.TABLE_NAME,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null);
    }
    public Cursor getAllDeviceInfo() {
        return getReadableDatabase()
                .query(
                        ScanContract.DeviceContract.TABLE_NAME,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null);
    }
    public Cursor getAllErrorCodesInfo() {
        return getReadableDatabase()
                .query(
                        ScanContract.ErrorCodesContract.TABLE_NAME,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null);
    }

    /**Sección para crear formato JSON**/
    public ArrayList<String> getCellularInfoInJson(Cursor getCellularInfoByIsRegistered){
        ArrayList<String> jsonQueryFormat = new ArrayList<String>();
        int counter = 0;
        while(getCellularInfoByIsRegistered.moveToNext()){
            String scan_id = getCellularInfoByIsRegistered.getString(getCellularInfoByIsRegistered.getColumnIndex(ScanContract.ScanEntry._ID));
            String timestamp = getCellularInfoByIsRegistered.getString(getCellularInfoByIsRegistered.getColumnIndex(ScanContract.ScanEntry.TIMESTAMP));
            String deviceUUID = getCellularInfoByIsRegistered.getString(getCellularInfoByIsRegistered.getColumnIndex(ScanContract.ScanEntry.DEVICEUUID));
            String simNumberID = getCellularInfoByIsRegistered.getString(getCellularInfoByIsRegistered.getColumnIndex(ScanContract.ScanEntry.SIMNUMBERID));
            String ratingCounter = getCellularInfoByIsRegistered.getString(getCellularInfoByIsRegistered.getColumnIndex(ScanContract.ScanEntry.RATINGCOUNTER));
            String countryISO = getCellularInfoByIsRegistered.getString(getCellularInfoByIsRegistered.getColumnIndex(ScanContract.ScanEntry.COUNTRYISO));
            String phoneOperatorId = getCellularInfoByIsRegistered.getString(getCellularInfoByIsRegistered.getColumnIndex(ScanContract.ScanEntry.PHONEOPERATORID));
            String simOperatorId = getCellularInfoByIsRegistered.getString(getCellularInfoByIsRegistered.getColumnIndex(ScanContract.ScanEntry.SIMOPERATORID));
            String operatorMcc = getCellularInfoByIsRegistered.getString(getCellularInfoByIsRegistered.getColumnIndex(ScanContract.ScanEntry.OPERATORMCC));
            String operatorMnc = getCellularInfoByIsRegistered.getString(getCellularInfoByIsRegistered.getColumnIndex(ScanContract.ScanEntry.OPERATORMNC));
            String devManufacturer = getCellularInfoByIsRegistered.getString(getCellularInfoByIsRegistered.getColumnIndex(ScanContract.ScanEntry.DEVMANUFACTURER));
            String devModel = getCellularInfoByIsRegistered.getString(getCellularInfoByIsRegistered.getColumnIndex(ScanContract.ScanEntry.DEVMODEL));
            String isConected = getCellularInfoByIsRegistered.getString(getCellularInfoByIsRegistered.getColumnIndex(ScanContract.ScanEntry.ISCONECTED));
            String phoneNetStandard = getCellularInfoByIsRegistered.getString(getCellularInfoByIsRegistered.getColumnIndex(ScanContract.ScanEntry.PHONENETSTANDARD));
            String phoneNetTechnology = getCellularInfoByIsRegistered.getString(getCellularInfoByIsRegistered.getColumnIndex(ScanContract.ScanEntry.PHONENETTECHNOLOGY));
            String internetConNetwork = getCellularInfoByIsRegistered.getString(getCellularInfoByIsRegistered.getColumnIndex(ScanContract.ScanEntry.INTERNETCONNETWORK));
            String latitude = getCellularInfoByIsRegistered.getString(getCellularInfoByIsRegistered.getColumnIndex(ScanContract.ScanEntry.LATITUDE));
            String longitude = getCellularInfoByIsRegistered.getString(getCellularInfoByIsRegistered.getColumnIndex(ScanContract.ScanEntry.LONGITUDE));
            String pingTimeMilis = getCellularInfoByIsRegistered.getString(getCellularInfoByIsRegistered.getColumnIndex(ScanContract.ScanEntry.PINGTIMEMILIS));
            String downloadSpeed = getCellularInfoByIsRegistered.getString(getCellularInfoByIsRegistered.getColumnIndex(ScanContract.ScanEntry.DOWNLOADSPEED));
            String uploadSpeed = getCellularInfoByIsRegistered.getString(getCellularInfoByIsRegistered.getColumnIndex(ScanContract.ScanEntry.UPLOADSPEED));
            String ipPublicAddr = getCellularInfoByIsRegistered.getString(getCellularInfoByIsRegistered.getColumnIndex(ScanContract.ScanEntry.IPPUBLICADDR));
            String internetISP = getCellularInfoByIsRegistered.getString(getCellularInfoByIsRegistered.getColumnIndex(ScanContract.ScanEntry.INTERNETISP));
            String phoneSignalStrength = getCellularInfoByIsRegistered.getString(getCellularInfoByIsRegistered.getColumnIndex(ScanContract.ScanEntry.PHONESIGNALSTRENGTH));
            String phoneAsuStrength = getCellularInfoByIsRegistered.getString(getCellularInfoByIsRegistered.getColumnIndex(ScanContract.ScanEntry.PHONEASUSTRENGTH));
            String phoneSignalLevel = getCellularInfoByIsRegistered.getString(getCellularInfoByIsRegistered.getColumnIndex(ScanContract.ScanEntry.PHONESIGNALLEVEL));
            String signalQuality = getCellularInfoByIsRegistered.getString(getCellularInfoByIsRegistered.getColumnIndex(ScanContract.ScanEntry.SIGNALQUALITY));
            String fieldIsRegistered = getCellularInfoByIsRegistered.getString(getCellularInfoByIsRegistered.getColumnIndex(ScanContract.ScanEntry.FIELDISREGISTERED));
            String phoneRsrpStrength = getCellularInfoByIsRegistered.getString(getCellularInfoByIsRegistered.getColumnIndex(ScanContract.ScanEntry.PHONERSRPSTRENGTH));
            String phoneRssnrStrength = getCellularInfoByIsRegistered.getString(getCellularInfoByIsRegistered.getColumnIndex(ScanContract.ScanEntry.PHONERSSNRSTRENGTH));
            String phoneTimingAdvance = getCellularInfoByIsRegistered.getString(getCellularInfoByIsRegistered.getColumnIndex(ScanContract.ScanEntry.PHONETIMINGADVANCE));
            String phoneCqiStrength = getCellularInfoByIsRegistered.getString(getCellularInfoByIsRegistered.getColumnIndex(ScanContract.ScanEntry.PHONECQISTRENGTH));
            String phoneRsrqStrength = getCellularInfoByIsRegistered.getString(getCellularInfoByIsRegistered.getColumnIndex(ScanContract.ScanEntry.PHONERSRQSTRENGTH));
            String cellLtePci = getCellularInfoByIsRegistered.getString(getCellularInfoByIsRegistered.getColumnIndex(ScanContract.ScanEntry.CELLLTEPCI));
            String cellLteCid = getCellularInfoByIsRegistered.getString(getCellularInfoByIsRegistered.getColumnIndex(ScanContract.ScanEntry.CELLLTECID));
            String cellLteTac = getCellularInfoByIsRegistered.getString(getCellularInfoByIsRegistered.getColumnIndex(ScanContract.ScanEntry.CELLLTETAC));
            String cellLteeNodeB = getCellularInfoByIsRegistered.getString(getCellularInfoByIsRegistered.getColumnIndex(ScanContract.ScanEntry.CELLLTEENODEB));
            String cellLteEarfcn = getCellularInfoByIsRegistered.getString(getCellularInfoByIsRegistered.getColumnIndex(ScanContract.ScanEntry.CELLLTEEARFCN));
            String cellBslat = getCellularInfoByIsRegistered.getString(getCellularInfoByIsRegistered.getColumnIndex(ScanContract.ScanEntry.CELLBSLAT));
            String cellBslon = getCellularInfoByIsRegistered.getString(getCellularInfoByIsRegistered.getColumnIndex(ScanContract.ScanEntry.CELLBSLON));
            String cellSid = getCellularInfoByIsRegistered.getString(getCellularInfoByIsRegistered.getColumnIndex(ScanContract.ScanEntry.CELLSID));
            String cellNid = getCellularInfoByIsRegistered.getString(getCellularInfoByIsRegistered.getColumnIndex(ScanContract.ScanEntry.CELLNID));
            String cellBid = getCellularInfoByIsRegistered.getString(getCellularInfoByIsRegistered.getColumnIndex(ScanContract.ScanEntry.CELLBID));
            String cellWcdmaLac = getCellularInfoByIsRegistered.getString(getCellularInfoByIsRegistered.getColumnIndex(ScanContract.ScanEntry.CELLWCDMALAC));
            String cellWcdmaUcid = getCellularInfoByIsRegistered.getString(getCellularInfoByIsRegistered.getColumnIndex(ScanContract.ScanEntry.CELLWCDMAUCID));
            String cellWcdmaUarfcn = getCellularInfoByIsRegistered.getString(getCellularInfoByIsRegistered.getColumnIndex(ScanContract.ScanEntry.CELLWCDMAUARFCN));
            String cellWcdmaPsc = getCellularInfoByIsRegistered.getString(getCellularInfoByIsRegistered.getColumnIndex(ScanContract.ScanEntry.CELLWCDMAPSC));
            String cellWcdmaCid = getCellularInfoByIsRegistered.getString(getCellularInfoByIsRegistered.getColumnIndex(ScanContract.ScanEntry.CELLWCDMACID));
            String cellWcdmaRnc = getCellularInfoByIsRegistered.getString(getCellularInfoByIsRegistered.getColumnIndex(ScanContract.ScanEntry.CELLWCDMARNC));
            String cellGsmArcfn = getCellularInfoByIsRegistered.getString(getCellularInfoByIsRegistered.getColumnIndex(ScanContract.ScanEntry.CELLGSMARCFN));
            String cellGsmLac = getCellularInfoByIsRegistered.getString(getCellularInfoByIsRegistered.getColumnIndex(ScanContract.ScanEntry.CELLGSMLAC));
            String cellGsmCid = getCellularInfoByIsRegistered.getString(getCellularInfoByIsRegistered.getColumnIndex(ScanContract.ScanEntry.CELLGSMCID));
            if (Integer.parseInt(fieldIsRegistered) == 0 ){
                jsonQueryFormat.add("\"timestamp\":\""+timestamp+"\"," +
                        "\"deviceUUID\":\""+deviceUUID+"\"," +
                        "\"simNumberID\":\""+simNumberID+"\"," +
                        "\"ratingCounter\":\""+ratingCounter+"\"," +
                        "\"countryISO\":\""+countryISO+"\"," +
                        "\"phoneOperatorId\":\""+phoneOperatorId+"\"," +
                        "\"simOperatorId\":\""+simOperatorId+"\"," +
                        "\"operatorMcc\":\""+operatorMcc+"\"," +
                        "\"operatorMnc\":\""+operatorMnc+"\"," +
                        "\"devManufacturer\":\""+devManufacturer+"\"," +
                        "\"devModel\":\""+devModel+"\"," +
                        "\"isConected\":\""+isConected+"\"," +
                        "\"phoneNetStandard\":\""+phoneNetStandard+"\"," +
                        "\"phoneNetTechnology\":\""+phoneNetTechnology+"\"," +
                        "\"internetConNetwork\":\""+internetConNetwork+"\"," +
                        "\"latitude\":\""+latitude+"\"," +
                        "\"longitude\":\""+longitude+"\"," +
                        "\"pingTimeMilis\":\""+pingTimeMilis+"\"," +
                        "\"downloadSpeed\":\""+downloadSpeed+"\"," +
                        "\"uploadSpeed\":\""+uploadSpeed+"\"," +
                        "\"ipPublicAddr\":\""+ipPublicAddr+"\"," +
                        "\"internetISP\":\""+internetISP+"\"," +
                        "\"phoneSignalStrength\":\""+phoneSignalStrength+"\"," +
                        "\"phoneAsuStrength\":\""+phoneAsuStrength+"\"," +
                        "\"phoneSignalLevel\":\""+phoneSignalLevel+"\"," +
                        "\"signalQuality\":\""+signalQuality+"\"," +
                        "\"fieldIsRegistered\":\""+fieldIsRegistered+"\"," +
                        "\"phoneRsrpStrength\":\""+phoneRsrpStrength+"\"," +
                        "\"phoneRssnrStrength\":\""+phoneRssnrStrength+"\"," +
                        "\"phoneTimingAdvance\":\""+phoneTimingAdvance+"\"," +
                        "\"phoneCqiStrength\":\""+phoneCqiStrength+"\"," +
                        "\"phoneRsrqStrength\":\""+phoneRsrqStrength+"\"," +
                        "\"cellLtePci\":\""+cellLtePci+"\"," +
                        "\"cellLteCid\":\""+cellLteCid+"\"," +
                        "\"cellLteTac\":\""+cellLteTac+"\"," +
                        "\"cellLteeNodeB\":\""+cellLteeNodeB+"\"," +
                        "\"cellLteEarfcn\":\""+cellLteEarfcn+"\"," +
                        "\"cellBslat\":\""+cellBslat+"\"," +
                        "\"cellBslon\":\""+cellBslon+"\"," +
                        "\"cellSid\":\""+cellSid+"\"," +
                        "\"cellNid\":\""+cellNid+"\"," +
                        "\"cellBid\":\""+cellBid+"\"," +
                        "\"cellWcdmaLac\":\""+cellWcdmaLac+"\"," +
                        "\"cellWcdmaUcid\":\""+cellWcdmaUcid+"\"," +
                        "\"cellWcdmaUarfcn\":\""+cellWcdmaUarfcn+"\"," +
                        "\"cellWcdmaPsc\":\""+cellWcdmaPsc+"\"," +
                        "\"cellWcdmaCid\":\""+cellWcdmaCid+"\"," +
                        "\"cellWcdmaRnc\":\""+cellWcdmaRnc+"\"," +
                        "\"cellGsmArcfn\":\""+cellGsmArcfn+"\"," +
                        "\"cellGsmLac\":\""+cellGsmLac+"\"," +
                        "\"cellGsmCid\":\""+cellGsmCid+"\"};"+scan_id);
                        counter = counter+1;
            }
        }
        return jsonQueryFormat;
    }
    public ArrayList<String> getDeviceInfoInJson(Cursor getDeviceInfoByIsRegistered){
        ArrayList<String> jsonQueryFormat = new ArrayList<String>();
        int counter = 0;
        while(getDeviceInfoByIsRegistered.moveToNext()){
            String deviceUUID = getDeviceInfoByIsRegistered.getString(getDeviceInfoByIsRegistered.getColumnIndex(ScanContract.DeviceContract.DEVICEUUID));
            String deviceModel = getDeviceInfoByIsRegistered.getString(getDeviceInfoByIsRegistered.getColumnIndex(ScanContract.DeviceContract.DEVICEMODEL));
            String deviceOS = getDeviceInfoByIsRegistered.getString(getDeviceInfoByIsRegistered.getColumnIndex(ScanContract.DeviceContract.DEVICEOS));
            String osVersion = getDeviceInfoByIsRegistered.getString(getDeviceInfoByIsRegistered.getColumnIndex(ScanContract.DeviceContract.OSVERSION));
            String isDualSim = getDeviceInfoByIsRegistered.getString(getDeviceInfoByIsRegistered.getColumnIndex(ScanContract.DeviceContract.ISDUALSIM));
            String fieldIsRegistered = getDeviceInfoByIsRegistered.getString(getDeviceInfoByIsRegistered.getColumnIndex(ScanContract.DeviceContract.FIELDISREGISTERED));
            if (Integer.parseInt(fieldIsRegistered) == 0 ){
                jsonQueryFormat.add("\"deviceUUID\":\""+deviceUUID+"\"," +
                        "\"deviceModel\":\""+deviceModel+"\"," +
                        "\"deviceOS\":\""+deviceOS+"\"," +
                        "\"osVersion\":\""+osVersion+"\"," +
                        "\"isDualSim\":\""+isDualSim+"\"," +
                        "\"fieldIsRegistered\":\""+fieldIsRegistered+"\"};"+deviceUUID);
                counter = counter+1;
            }
        }
        return jsonQueryFormat;
    }
    public ArrayList<String> getErrorCodesInfoInJson(Cursor getErrorCodesInfoByIsRegistered){
        ArrayList<String> jsonQueryFormat = new ArrayList<String>();
        int counter = 0;
        while(getErrorCodesInfoByIsRegistered.moveToNext()){
            String scan_id = getErrorCodesInfoByIsRegistered.getString(getErrorCodesInfoByIsRegistered.getColumnIndex(ScanContract.ScanEntry._ID));
            String deviceUUID = getErrorCodesInfoByIsRegistered.getString(getErrorCodesInfoByIsRegistered.getColumnIndex(ScanContract.DeviceContract.DEVICEUUID));
            String deviceModel = getErrorCodesInfoByIsRegistered.getString(getErrorCodesInfoByIsRegistered.getColumnIndex(ScanContract.DeviceContract.DEVICEMODEL));
            String deviceOS = getErrorCodesInfoByIsRegistered.getString(getErrorCodesInfoByIsRegistered.getColumnIndex(ScanContract.DeviceContract.DEVICEOS));
            String osVersion = getErrorCodesInfoByIsRegistered.getString(getErrorCodesInfoByIsRegistered.getColumnIndex(ScanContract.DeviceContract.OSVERSION));
            String isDualSim = getErrorCodesInfoByIsRegistered.getString(getErrorCodesInfoByIsRegistered.getColumnIndex(ScanContract.DeviceContract.ISDUALSIM));
            String fieldIsRegistered = getErrorCodesInfoByIsRegistered.getString(getErrorCodesInfoByIsRegistered.getColumnIndex(ScanContract.DeviceContract.FIELDISREGISTERED));
            if (Integer.parseInt(fieldIsRegistered) == 0 ){
                jsonQueryFormat.add("\"deviceUUID\":\""+deviceUUID+"\"," +
                        "\"deviceModel\":\""+deviceModel+"\"," +
                        "\"deviceOS\":\""+deviceOS+"\"," +
                        "\"osVersion\":\""+osVersion+"\"," +
                        "\"isDualSim\":\""+isDualSim+"\"," +
                        "\"fieldIsRegistered\":\""+fieldIsRegistered+"\"};"+deviceUUID);
                counter = counter+1;
            }
        }
        return jsonQueryFormat;
    }

    /**Sección para crear actualizar campos que no se hayan registrado en el backend**/
    public void updateCellularIsRegisteredById(String scanId) {
        ContentValues cv = new ContentValues();
        cv.put(ScanContract.ScanEntry.FIELDISREGISTERED,"1");
        int c = getWritableDatabase().update(ScanContract.ScanEntry.TABLE_NAME,
                cv,ScanContract.ScanEntry._ID + " LIKE ?",new String[]{scanId});
        Log.d("updateIsRegisteredByID","el valor de la consulta es "+c);
    }
    public void updateDeviceIsRegisteredById(String scanId) {
        ContentValues cv = new ContentValues();
        cv.put(ScanContract.DeviceContract.FIELDISREGISTERED,"1");
        int c = getWritableDatabase().update(ScanContract.DeviceContract.TABLE_NAME,
                cv,ScanContract.DeviceContract.DEVICEUUID + " LIKE ?",new String[]{scanId});
        Log.d("updateIsRegisteredByID","el valor de la consulta es "+c);
    }
    public void updateErrorCodesIsRegisteredById(String scanId) {
        ContentValues cv = new ContentValues();
        cv.put(ScanContract.ErrorCodesContract.FIELDISREGISTERED,"1");
        int c = getWritableDatabase().update(ScanContract.ErrorCodesContract.TABLE_NAME,
                cv,ScanContract.ErrorCodesContract._ID + " LIKE ?",new String[]{scanId});
        Log.d("updateIsRegisteredByID","el valor de la consulta es "+c);
    }

    /**Crea consultas para dibujar mapa **/
    public ArrayList<String> getMapLteQuery(Cursor queryAllCellularInfo){
        Log.d("SqlLite","Entra a getMapQuery");
        ArrayList<String> queryMapFormat = new ArrayList<String>();
        int contador = 0;

        while(queryAllCellularInfo.moveToNext()){
            String operatorName = queryAllCellularInfo.getString(queryAllCellularInfo.getColumnIndex(ScanContract.ScanEntry.PHONEOPERATORID));
            String phoneNetworType = queryAllCellularInfo.getString(queryAllCellularInfo.getColumnIndex(ScanContract.ScanEntry.PHONENETSTANDARD));
            String phoneTimestamp = queryAllCellularInfo.getString(queryAllCellularInfo.getColumnIndex(ScanContract.ScanEntry.TIMESTAMP));
            String phoneSignalStrength = queryAllCellularInfo.getString(queryAllCellularInfo.getColumnIndex(ScanContract.ScanEntry.PHONESIGNALSTRENGTH));
            String signalQuality = queryAllCellularInfo.getString(queryAllCellularInfo.getColumnIndex(ScanContract.ScanEntry.SIGNALQUALITY));
            String latitude = queryAllCellularInfo.getString(queryAllCellularInfo.getColumnIndex(ScanContract.ScanEntry.LATITUDE));
            String longitude = queryAllCellularInfo.getString(queryAllCellularInfo.getColumnIndex(ScanContract.ScanEntry.LONGITUDE));
            queryMapFormat.add(operatorName+";"
                    +phoneNetworType+";"
                    +phoneSignalStrength+";"
                    +phoneTimestamp+";"
                    +latitude+";"
                    +longitude+";"
                    +signalQuality);

            Log.d("getMapQuery","queryMapFormat es "+queryMapFormat.get(contador)+" contador es "+contador);
            contador = contador+1;
        }
        return queryMapFormat;
    }



}


