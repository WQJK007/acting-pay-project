package com.unicom.acting.pay.domain;

/**
 * 账务交易公共参数
 *
 * @author Wangkh
 */
public class ActingPayCommparaDef {

    private ActingPayCommparaDef() {
    }

    /**
     * 违约金减免按金额能持续使用
     */
    public static final String ASM_LATEUSE_PERSIST = "ASM_LATEUSE_PERSIST";

    /**
     * 异地缴费判断
     */
    public static final String ASM_NONLOCAL_RECVFEE = "ASM_NONLOCAL_RECVFEE";

    /**
     * 销账参数时间戳
     */
    public static final String ASM_PARAM_TIMESTAMP = "ASM_PARAM_TIMESTAMP";

    /**
     * 大合帐用户特殊缴费异步处理参数
     */
    public static final String ASM_DHZLIMIT_CHOICE = "ASM_DHZLIMIT_CHOICE";

    /**
     * 销账计算剔除实时账单
     */
    public static final String ASM_CAN_PREREALBILL_CALC = "ASM_CAN_PREREALBILL_CALC";
    /**
     * 是否启用增量出账
     */
    public static final String IF_DRECV_ADDACCT = "IF_DRECV_ADDACCT";
    /**
     * 账本排序规则
     */
    public static final String ASM_DEPOSIT_PRIVATE_PRIORITY = "ASM_DEPOSIT_PRIVATE_PRIORITY";
    /**
     * 不计算滞纳金的支付方式
     */
    public static final String ASM_CALCLATEFEE_PAYMODE_LIMIT = "ASM_CALCLATEFEE_PAYMODE_LIMIT";

    /**
     * 账期状态从缓存中获取
     */
    public static final String ASM_AUXACCTSTATUS_FROMCACHE = "ASM_AUXACCTSTATUS_FROMCACHE";


    /**
     * 只有销户用户做滞纳金计算
     */
    public static final String ASM_CALCLATEFEE_DESTROY_USER = "ASM_CALCLATEFEE_DESTROY_USER";

    /**
     * 坏账是否做滞纳金计算
     */
    public static final String ASM_BADBILL_CALC_LATEFEE = "ASM_BADBILL_CALC_LATEFEE";
    /**
     * 坏账缴费处理方式
     */
    public static final String ASM_BADBILL_PAYFEE = "ASM_BADBILL_PAYFEE";
    /**
     * 托收在途账单能否通过其他渠道缴费
     */
    public static final String ASM_CONSIGN_CAN_RECV = "ASM_CONSIGN_CAN_RECV";
    /**
     * 托收账户支付方式
     */
    public static final String ASM_CONSIGN_PAY_MODE = "ASM_CONSIGN_PAY_MODE";
    /**
     * 联建局用户开户缴费
     */
    public static final String ASM_SITECONSIGN_DEAL = "ASM_SITECONSIGN_DEAL";
    /**
     * 预打印发票不能前台缴费
     */
    public static final String ASM_PRE_PRINTINVOICE_CAN_RECV = "ASM_PRE_PRINTINVOICE_CAN_RECV";
    /**
     * 负账单销账账本
     */
    public static final String ASM_NEGATIVEBILL_DEPOSIT = "ASM_NEGATIVEBILL_DEPOSIT";
    /**
     * 电子赠款停机状态不能赠送
     */
    public static final String ASM_PRESENT_LIMITPAYMENT = "ASM_PRESENT_LIMITPAYMENT";

    /**
     * 分省控制哪些账本不包含在账户当前可用余额中
     */
    public static final String ASM_BALAN_BORD = "ASM_BALAN_BORD";

    /**
     * 广东托收用户参与实时信控,往月欠费不计入用户结余
     */
    public static final String ASM_CONSIGN_USERBALANCE = "ASM_CONSIGN_USERBALANCE";

    /**
     * 缴费大账户处理参数
     */
    public static final String JF_BIGACCT = "JF_BIGACCT";

    /**
     * 触发信控方式
     */
    public static final String FIRE_CREDIT_MODE = "FIRE_CREDIT_MODE";

    /**
     * 短信发送模式
     */
    public static final String ASM_MSGSEND_TYPE = "ASM_MSGSEND_TYPE";

    /**
     * 不发送短信的渠道类型
     */
    public static final String ASM_SENDSMS_CHANNELID = "ASM_SENDSMS_CHANNELID";


    /**
     * 10000短信模板实际发送内容
     */
    public static final String ASM_PAYFEESMS_OPTIMIZED = "ASM_PAYFEESMS_OPTIMIZED";

    /**
     * 融合用户发送多个号码
     */
    public static final String ASM_PAYFEESMS_WXWJ = "ASM_PAYFEESMS_WXWJ";

    /**
     * 充值短信给主用户发送的融合类型
     */
    public static final String ASM_RHMSGSEND_TYPE = "ASM_RHMSGSEND_TYPE";

    /**
     * 不同步在线线控的储值方式
     */
    public static final String GET_CREDITONLINE_PAYMENT = "GET_CREDITONLINE_PAYMENT";

    /**
     * 是否通过KAFKA同步在线信控
     */
    public static final String ASM_CREDITONLINE_SENDMSG = "ASM_CREDITONLINE_SENDMSG";

    /**
     * KFAKA方式发送消息是否记录日志
     */
    public static final String ASM_CREDITONLINE_BALANCELOG = "ASM_CREDITONLINE_BALANCELOG";

    /**
     * 在线信控充值记录同步KAFKA配置信息
     */
    public static final String ASM_KAFKA_PARAM = "ASM_KAFKA_PARAM";

    /**
     * 统一余额播报参数
     */
    public static final String ASM_SHOW_TYPE = "ASM_SHOW_TYPE";

    /**
     * 异地缴费信息同步标识参数
     */
    public static final String ASM_ALLOPATRY_PAYINFO_SYNC = "ASM_ALLOPATRY_PAYINFO_SYNC";


    /**
     * 合约惠机ATTR_CODE_的种类
     */
    public static final String ASM_AM_HY_ATTR_CODE_TYPE = "ASM_AM_HY_ATTR_CODE_TYPE";

    /**
     * 应缴是否包含实话话费
     */
    public static final String ASM_SPAY_MODE = "ASM_SPAY_MODE";

    /**
     * 专项可用预存款,专项可用赠款,专项可用款特殊展示
     */
    public static final String ASM_SHOW_SPECIALFEE = "ASM_SHOW_SPECIALFEE";
    /**
     * 预付费用户清退是否扣除实时话费
     */
    public static final String ASM_IFBACKFEE_NOREALFEE = "ASM_IFBACKFEE_NOREALFEE";
    /**
     * 是否账户当前可用余额赋值实时结余
     */
    public static final String ASM_SHOW_CURRENT = "ASM_SHOW_CURRENT";


    /**
     * 交费返销可打金额是否限制
     */
    public static final String ASM_NOPRINTFEE_CANCELLIMIT = "ASM_NOPRINTFEE_CANCELLIMIT";


    /**
     * 隔天返销限制参数
     */
    public static final String ASM_NOALLOW_WRITEOFF_CANCEL = "ASM_NOALLOW_WRITEOFF_CANCEL";


    /**
     * 是否可以隔壁返销
     */
    public static final String ASM_NOALLOW_INTERVAL_CANCELFEE = "ASM_NOALLOW_INTERVAL_CANCELFEE";



}
