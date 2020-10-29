package com.arcotel.network.tools.data;

public class Constants {
    public static final int SUCCESS_RESULT = 0;

    public static final int FAILURE_RESULT = 1;

    private static final String PACKAGE_NAME = "com.coderminion.googlemaps";

    public static final String RECEIVER = PACKAGE_NAME + ".RECEIVER";

    public static final String RESULT_DATA_KEY = PACKAGE_NAME + ".RESULT_DATA_KEY";

    public static final String LOCATION_DATA_EXTRA = PACKAGE_NAME + ".LOCATION_DATA_EXTRA";

    public static final int NUMBER_FOR_DELETE_FIELDS_IN_MAP = 15;
    public static final int REGISTERED_OFF = 0;
    public static final int REGISTERED_ON = 1;

    public static final double LATITUDE_DEFAULT = -1.646367;
    public static final double LONGITUDE_DEFAULT = -78.683545;

    public static final String URL_FOR_CELLULAR_DATA = "http://ec2-3-23-89-83.us-east-2.compute.amazonaws.com:80/add";
    public static final String URL_FOR_DEVICE_DATA = "http://ec2-3-23-89-83.us-east-2.compute.amazonaws.com:80/adddev";
    public static final String URL_FOR_ERROR_DATA = "http://ec2-3-23-89-83.us-east-2.compute.amazonaws.com:80/adderrdata";

    public static final String INSTANCE_OF_CELLULAR = "cellular";
    public static final String INSTANCE_OF_DEVICE = "device";
    public static final String INSTANCE_OF_ERROR = "error";
}
