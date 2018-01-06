package io.github.hidroh.calendar.apps;

/**
 * Created by singgih on 9/6/2017.
 */


public class AppConfig {

    private static String baseUrl = "http://api.itechmandiri.com/";

    private static  String key = "?api_token=4b18260ba82c85ebe624b5caac41cc54ac40ee6f";

    public static final String API_KEY = key;
    public static final String SUCCESS = "success";
    public static final String RESULT="result";

    //1 means data is synced and 0 means data is not synced
    public static final int DATA_SYNCED_WITH_SERVER = 1;
    public static final int DATA_NOT_SYNCED_WITH_SERVER = 0;
    //a broadcast to know weather the data is synced or not
    public static final String DATA_SAVED_BROADCAST = "Data Saved";

    // Server user login url
    public static String URL_LOGIN = "http://128.159.1.65/impro/android_login_api/login.php";

    // Server user register url
    public static String URL_REGISTER = "http://128.159.1.65/impro/android_login_api/register.php";

    // Customer CRUD
    public static final String URL_GET_ALL_CUST = baseUrl + "/customer" + key;
    public static final String URL_GET_CUST = baseUrl + "/customer/";
    public static final String URL_SAVE_CUST = baseUrl + "/customer/create" + key;
    public static final String URL_UPDATE_CUST = baseUrl + "/customer/update/";
    public static final String URL_DELETE_CUST = baseUrl + "/customer/delete/";
    public static final String URL_GET_CUST_NAME = baseUrl + "/list/customer" + key;

    // Opportunity CRUD
    public static final String URL_GET_ALL_OPP = baseUrl + "/opportunity" + key;
    public static final String URL_GET_OPP = baseUrl + "/opportunity/";
    public static final String URL_SAVE_OPP = baseUrl + "/opportunity/create" + key;
    public static final String URL_UPDATE_OPP = baseUrl + "/opportunity/update/";
    public static final String URL_DELETE_OPP = baseUrl + "/opportunity/delete/";


    //Task CRUD
    public static final String URL_GET_ALL_TASK = baseUrl + "/task" + key;
    public static final String URL_GET_TASK = baseUrl + "/task/";
    public static final String URL_SAVE_TASK = baseUrl + "/task/create" + key;
    public static final String URL_UPDATE_TASK = baseUrl + "/task/update/";
    public static final String URL_DELETE_TASK = baseUrl + "/task/delete/";
    public static final String URL_GET_TASK_NAME = baseUrl + "/list/task" + key;


    //JSON Tags for Customer
    public static final String OPP_ID = "opp_id";
    public static final String OPP_NAME = "opp_name";
    public static final String OPP_CUST = "opp_cust";
    public static final String OPP_ST = "opp_st";
    public static final String OPP_STAGE = "opp_stage";
    public static final String OPP_PROB = "opp_prob";
    public static final String OPP_DATE = "opp_date";

    //JSON Tags for Opportunity
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