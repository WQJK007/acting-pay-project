package com.unicom.acting.pay.writeoff.service;

import com.unicom.acting.fee.domain.DepositPriorRule;
import com.unicom.acting.pay.domain.*;
import com.unicom.acting.pay.writeoff.domain.CancelRecvFeeInfo;
import com.unicom.acting.pay.writeoff.domain.CancelRecvFeeInfoIn;
import com.unicom.acting.pay.writeoff.domain.CancelRecvFeeInfoOut;
import com.unicom.acting.pay.writeoff.domain.CancelRecvFeeMqMessage;
import com.unicom.skyark.component.service.IBaseService;

import java.util.List;
import java.util.Map;


/**
 * 返销核心业务逻辑处理接口类
 * @author ducj
 */
public interface CancelTradePayService extends IBaseService {

    /**
     * 返销前业务校验 -- acting.pay.writeoff
     * @param cancelRecvFeeInfoIn 返销输入对象
     * @return 返销交易数据
     */
    CancelRecvFeeInfo buildCancelTradeInfoCommon(CancelRecvFeeInfoIn cancelRecvFeeInfoIn) ;


    /**
     * 返销主流程 -- acting.pay.writeoff
     * @param cancelRecvFeeInfo 返销交易数据
     * @return 返销输出对象
     */
    CancelRecvFeeInfoOut cancelFee(CancelRecvFeeInfo cancelRecvFeeInfo);

    /**
     *
     * 返销核心流程 -- acting.pay.writeoff
     * @param cancelRecvFeeInfo 返销交易数据
     * @param cancelRecvFeeInfoOut 返销输出对象
     * @return 返销输出对象
     */
    CancelRecvFeeInfoOut cancelFeeCore(CancelRecvFeeInfo cancelRecvFeeInfo, CancelRecvFeeInfoOut cancelRecvFeeInfoOut);

    /**
     * 账务中心- 根据chargeId和acctId获取关联的交费记录 -- acting.pay.writeoff
     * @param chargeId 交费流水
     * @param acctId 账户标识
     * @return 交费日志列表
     */
    List<PayLog> getRelationPaylogsByChargeIdAndAcctId(String chargeId, String acctId);

    /**
     * 返销日志入库 -- acting.pay.writeoff
     * @param cancelRecvFeeInfoOut 返销输出对象
     * @return 是否入库成功
     */
    boolean cancelRecvFeeLogIndbCore(CancelRecvFeeInfoOut cancelRecvFeeInfoOut);


    /**
     * 构建mq消息体
     * @param cancelRecvFeeInfoOut 返销输出对象
     * @return mq消息体
     */
    CancelRecvFeeMqMessage genCancelFeeMqMessageCommon(CancelRecvFeeInfoOut cancelRecvFeeInfoOut);


    /**
     * dmn表入库 -- acting.pay.writeoff
     * @param payLogDmnList dmn入库列表
     * @return 是否入库成功
     */
    boolean insertPaylogDmnList(List<PayLogDmn> payLogDmnList);


    /**
     * 交费关系入表 -- acting.pay.writeoff
     * @param chargeRelations 交费关系入库对象
     */
    void insertChargeRelations(List<ChargeRelation> chargeRelations);

    /**
     * 返销paylog记录 -- acting.pay.writeoff
     * @param tradeTradeStaff 返销交易员工
     * @param chargeId 交费流水
     * @param cancelTime 返销时间
     * @param acctid 账户标识
     */
    void cancelOrigPaylogByChargeIdAndAcctId(TradeStaff tradeTradeStaff, String chargeId, String cancelTime, String acctid);

    /**
     * 返销paylog_d记录 -- acting.pay.writeoff
     * @param tradeTradeStaff 返销交易员工
     * @param chargeId 交费流水
     * @param cancelTime 返销时间
     * @param acctid 账户标识
     */
    void cancelOrigPaylogDByChargeIdAndAcctId(TradeStaff tradeTradeStaff, String chargeId, String cancelTime, String acctid);

    /**
     * 返销代收费日志表 -- acting.pay.writeoff
     * @param tradeTradeStaff 交易员工
     * @param chargeId 交费流水
     * @param cancelOuterTradeId 返销外围流水
     * @param cancelChargeId 返销交费流水
     * @param acctId 账户标识
     */
    void cancelClPaylogByChargeIdAndAcctId(TradeStaff tradeTradeStaff, String chargeId, String cancelOuterTradeId, String cancelChargeId, String acctId);


    /**
     * 返销日志入表 -- acting.pay.writeoff
     * @param payLog 交费日志对象
     */
    void insertPaylog(PayLog payLog);

    /**
     * 抵扣返销日志入表 -- acting.pay.writeoff
     * @param payLog 交费日志对象
     */
    void insertPaylogD(PayLog payLog);

    /**
     * 返销更新快照日志 -- acting.pay.writeoff
     * @param chargeId 交费流水
     * @param acctId 账户标识
     */
    void cancelWriteoffSnaplogByChargeIdAndAcctId(String chargeId, String acctId);

    /**
     * 返销更新抵扣快照日志 -- acting.pay.writeoff
     * @param chargeId 交费流水
     * @param acctId 账户标识
     */
    void cancelWriteoffSnaplogDByChargeIdAndAcctId(String chargeId, String acctId);

    /**
     * 返销销账日志 -- acting.pay.writeoff
     * @param chargeId 交费流水
     * @param acctId 账户标识
     */
    void cancelWriteofflogByChargeIdAndAcctId(String chargeId, String acctId);

    /**
     * 返销抵扣销账日志 -- acting.pay.writeoff
     * @param chargeId 交费流水
     * @param acctId 账户标识
     */
    void cancelWriteofflogDByChargeIdAndAcctId(String chargeId, String acctId);

    /**
     * 返销存取款日志 -- acting.pay.writeoff
     * @param chargeId 交费流水
     * @param acctId 账户标识
     */
    void cancelAccesslogByChargeIdAndAcctId(String chargeId, String acctId);

    /**
     * 返销抵扣存取款日志 -- acting.pay.writeoff
     * @param chargeId 交费流水
     * @param acctId 账户标识
     */
    void cancelAccesslogDByChargeIdAndAcctId(String chargeId, String acctId);

    /**
     * 账务中心记录存取款日志表 -- acting.pay.writeoff
     * @param accessLogs 存取款日志入库对象
     * @param cancelPayLog 返销交费日志对象
     */
    void insertAccesslog(List<AccessLog> accessLogs, PayLog cancelPayLog);

    /**
     * 账务中心记录抵扣存取款日志表 -- acting.pay.writeoff
     * @param accessLogs 抵扣存取款日志入库对象
     * @param cancelPayLog 返销交费日志对象
     */
    void insertAccesslogD(List<AccessLog> accessLogs, PayLog cancelPayLog);


    /**
     * 更新收费其他信息表TF_B_payother_log -- acting.pay.writeoff
     * @param chargeId 交费流水
     * @param acctId 账户标识
     */
    void cancelPayOtherlogByChargeIdAndAcctId(String chargeId, String acctId);


    /**
     * 账单入库 -- acting.pay.writeoff
     * @param writeOffLogs 销账日志
     */
    void cancelBillsByWriteOffLogs(List<WriteOffLog> writeOffLogs);


    /**
     * 回查事务状态 -- acting.pay.writeoff
     * @param cancelRecvFeeMqMessage mq消息
     * @return 回查结果
     */
    boolean checkbusinessService(CancelRecvFeeMqMessage cancelRecvFeeMqMessage);


    /**
     * 获取账本的deposit_type_code -- acting.pay.writeoff
     * @param depositCode 账本标识
     * @param depositPriorRuleMap 账本优先级存取Map
     * @return depositTypeCode  depositTypeCode
     */
    char getDepositTypeCode(int depositCode, Map<Integer, DepositPriorRule> depositPriorRuleMap);


    /**
     * 更新账本 -- acting.pay.writeoff
     * @param accessLogs
     */
/*    void cancelUpdateAccountDeposit(List<AccessLog> accessLogs);*/


    /**
     * 返销更新活动表 -- acting.pay.writeoff
     * @param actionEventId
     * @param recvFee
     * @param acctId 账户标识
     * @param provinceId 省份编码
     */
/*
    void updateDiscntDepositByEventIdIdAndAcctId(String actionEventId, long recvFee, String acctId, String provinceId);
*/
}
