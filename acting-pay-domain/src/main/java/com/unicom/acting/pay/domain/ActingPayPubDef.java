package com.unicom.acting.pay.domain;

/**
 * 账务交易常量
 */
public class ActingPayPubDef {
    private ActingPayPubDef() {
    }

    //默认省份、地市、网别值，获取销账规则使用
    public static final String DEFAULT_PROVINCE_CODE = "ZZZZ";
    public static final String DEFAULT_EPARCHY_CODE = "ZZZZ";
    public static final String DEFAULT_NET_TYPE_CODE = "ZZ";

    //####序列类型####
    public static final String SEQ_OUTERTRADEID_TANNAME = "TF_B_PAYLOG";
    public static final String SEQ_OUTERTRADEID_COLUMNNAME = "OUTER_TRADE_ID";
    /**
     * @see #SEQ_CHARGEID_TANNAME 交易流水
     */
    public static final String SEQ_CHARGEID_TANNAME = "TF_B_PAYLOG";
    /**
     * @see #SEQ_CHARGEID_COLUMNNAME 交易流水列名
     */
    public static final String SEQ_CHARGEID_COLUMNNAME = "CHARGE_ID";
    public static final String SEQ_CLCHARGEID_TANNAME = "TF_B_CLPAYLOG";
    public static final String SEQ_CLCHARGEID_COLUMNNAME = "CL_PAYLOG_ID";
    public static final String SEQ_ACCESSID_TABNAME = "TF_B_ACCESSLOG";
    public static final String SEQ_ACCESSID_COLUMNNAME = "ACCESS_ID";
    public static final String SEQ_WRITEOFFID_TABNAME = "TF_B_WRITEOFFLOG";
    public static final String SEQ_WRITEOFFID_COLUMNNAME = "WRITEOFF_ID";
    public static final String SEQ_SMSSENDID_TABNAME = "TF_B_SMSLOG";
    public static final String SEQ_SMSSENDID_COLUMNNAME = "SMS_ID";

    //交费账期
    public static final int MIN_CYCLE_ID = 198001;    //最小销账账期
    public static final int MAX_CYCLE_ID = 203001;  //最大销账账期
    public static final int MAX_MONTH_NUM = 240;    //最大销账账期数

    //限额
    public static final long MAX_LIMIT_FEE = 99999999999L;    //最大销账限额


    //大合帐默认付费用户数
    public static final int MUTIACCT_PAYRELATION_DEFAULTNUM = 100;

    //信控模式
    //发送信控开机工单
    public static final String JIAOFEI_TO_CREDIT = "JIAOFEI_TO_CREDIT";
    //不发送信控开机工单
    public static final String RECV_TO_CREDIT = "RECV_TO_CREDIT";
    //触发信控工单类型
    public static final String RECVCREDIT_TRADE_TYPE_DEFAULT = "8000";
    public static final String RECVCREDIT_TRADE_TYPE_BIGACCT = "8001";


    //按总额清退
    public static final String BACK_BY_ALLMONEY = "0";
    //按特定账本科目类型清退
    public static final String BACK_BY_DEPOSITCODE = "1";
    //按账本实例清退
    public static final String BACK_BY_ACCTBALANCEID = "2";


    //调用的微服务
    //三户资料查询微服务
    public static final String QRY_USER_DATUM = "userdatumjdbc";
    //查询实时账单微服务
    public static final String QRY_REAL_BILL = "realbilljdbc";
    //获取序列微服务
    public static final String GET_SEQUENCE = "getorcsqe";

    //用户属性查询
    public static final String QRY_USER_PARAM = "userinfo/getuserparam";

    //用户属性查询
    public static final String QRY_USER_RELATION = "userinfo/getuserrelation";

    //非抵扣期
    public static final String ORDER_CYCLE_STATUS = "0";

    //抵扣期
    public static final String DRECV_CYCLE_STATUS = "1";

    //回检TF_B_PAYLOG
    public static final String CHECK_TYPE_PAYLOG = "0";

    //回检TF_B_PAYLOG_DMN
    public static final String CHECK_TYPE_PAYLOGDMN = "1";

    //回检TF_B_ASYN_WORK
    public static final String CHECK_TYPE_ASYNWORK = "2";

    //Topic类型
    public static final String TRADE_MQTYPE_ORDER = "DEVACT_SYNCDEPO_TEST1";

    /**
     * 智慧沃家主用户属性
     */
    public static final String WJ_MAINUSER_PARAMID = "20000006";
    /**
     * 沃享主用户属性21000007
     */
    public static final String WX_MAINUSER_PARAMID = "21000007";
    /**
     * 省份账户属性
     */
    public static final String OLD_ACCTID_PARAMID = "20000060";
    /**
     * 省份用户属性
     */
    public static final String OLD_USERID_PARAMID = "20000050";
}
