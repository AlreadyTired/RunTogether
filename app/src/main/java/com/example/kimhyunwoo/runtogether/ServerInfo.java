package com.example.kimhyunwoo.runtogether;

public class ServerInfo {
    public static final String serverURL = "http://teamc-iot.calit2.net/";
    public static final String registerURL = "api/auth/signup";
    public static final String validateURL = "api/auth/verify/email";
    public static final String loginURL = "api/auth/signin";
    public static final String logoutURL = "api/auth/signout";
    public static final String changepasswordURL = "api/auth/password/change";
    public static final String idcancellationURL = "api/auth/cancel/cancellation";
    public static final String idrealcancellationURL = "api/auth/cancel/complete";
    public static final String findpasswordURL = "api/auth/password/forgotten";
    public static final String historicaldataviewURL = "api/sensor/historical/view";
    public static final String SensorDeregistrationURL = "api/sensor/deregistration";
    public static final String SensorListViewURL = "api/sensor/listview";
    public static final String SensorRegistrationURL = "api/sensor/registration";
    public static final String DataTransferURL = "api/sensor/real/transfer";
    public static final String AqiDataTransferURL = "api/sensor/aqi/transfer";
    public static final String UserListRequestURL = "/api/auth/user/list";
    public static final String SpecificUserRealTimeDataURL = "/api/sensor/all/view";
    public static final String ExerciseTransferURL = "api/maps/exercise/reg";
}
