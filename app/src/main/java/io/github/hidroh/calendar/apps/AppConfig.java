package io.github.hidroh.calendar.apps;

/**
 * Created by singgih on 9/6/2017.
 */


public class AppConfig {

    private static String baseUrl = "http://128.159.1.65:8000";

    private static  String key = "?api_token=4b18260ba82c85ebe624b5caac41cc54ac40ee6f";

    // Server user login url
    public static String URL_LOGIN = "http://192.168.0.8/impro/android_login_api/login.php";

    // Server user register url
    public static String URL_REGISTER = "http://192.168.0.8/impro/android_login_api/register.php";


    // Customer CRUD
    public static final String URL_GET_ALL_CUST = baseUrl + "/customer" + key;
    public static final String URL_GET_CUST = baseUrl + "/customer/";
    public static final String URL_SAVE_CUST = baseUrl + "/customer/create" + key;
    public static final String URL_UPDATE_CUST = baseUrl + "/customer/update/";
    public static final String URL_DELETE_CUST = baseUrl + "/customer/delete/";


    //Define Key
    public static final String KEY_EMP_ID_TASKS = "id";
    public static final String KEY_EMP_SUBJECT_TASKS = "subject_tasks";
    public static final String KEY_EMP_STATUS_TASKS = "status_tasks";
    public static final String KEY_EMP_TANGGAL_TASKS = "tanggal_tasks";

    public static final String KEY_EMP_WAKTU_TASKS = "waktu_tasks";
    public static final String KEY_EMP_OUTCOME_TASKS = "outcome_tasks";
    public static final String KEY_EMP_CUSTOMERS_TASKS = "customers_tasks";
    public static final String KEY_EMP_TYPE_TASKS = "type_tasks";
    public static final String KEY_EMP_DESCRIPTION_TASKS = "description_tasks";


    //JSON Tags
    public static final String SUCCESS = "success";
    public static final String RESULT="result";
    public static final String CUST_ID = "cust_id";
    public static final String CUST_NAME = "cust_name";
    public static final String CUST_ST = "cust_st";
    public static final String CUST_PHONE = "cust_phone";
    public static final String CUST_ADDRESS = "cust_address";
    public static final String CUST_DESC= "cust_desc";


    //ID karyawan
    //emp itu singkatan dari Employee
    public static final String EMP_ID = "emp_id";
}