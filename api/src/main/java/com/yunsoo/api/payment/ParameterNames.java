package com.yunsoo.api.payment;

/**
 * Created by:   Haitao
 * Created on:   2016/2/26
 * Descriptions:
 */
public final class ParameterNames {
    private ParameterNames() {
    }

    //basic parameters
    public static final String SERVICE = "service";
    public static final String PARTNER = "partner";
    public static final String INPUT_CHARSET = "_input_charset";
    public static final String SIGN_TYPE = "sign_type";
    public static final String SIGN = "sign";
    public static final String NOTIFY_TYPE = "notify_type";
    public static final String NOTIFY_URL = "notify_url";
    public static final String RETURN_URL = "return_url";
    public static final String ERROR_NOTIFY_URL = "error_notify_url";


    //business parameters
    public static final String OUT_TRADE_NO = "out_trade_no";
    public static final String SUBJECT = "subject";
    public static final String PAYMENT_TYPE = "payment_type";
    public static final String TOTAL_FEE = "total_fee";
    public static final String SELLER_ID = "seller_id";

    //batch trans business parameters
    public static final String ACCOUNT_NAME = "account_name";
    public static final String DETAIL_DATA = "detail_data";
    public static final String BATCH_NO = "batch_no";
    public static final String BATCH_NUM = "batch_num";
    public static final String BATCH_FEE = "batch_fee";
    public static final String EMAIL = "email";
    public static final String PAY_DATE = "pay_date";

    //refund business parameters
    public static final String SELLER_EMAIL = "seller_email";
    public static final String SELLER_USER_ID = "seller_user_id";
    public static final String REFUND_DATE = "refund_date";

}