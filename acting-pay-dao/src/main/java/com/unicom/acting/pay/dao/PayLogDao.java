package com.unicom.acting.pay.dao;


import com.unicom.acting.pay.domain.*;
import com.unicom.skyark.component.dao.IBaseDao;

import java.util.List;

/**
 * 对TF_B_PAYLOG，TF_B_PAYOTHER_LOG,表的增查改操作
 *
 * @author Wangkh
 */
public interface PayLogDao extends IBaseDao {
    /**
     * 新增缴费日志记录
     *
     * @param payLog
     * @return
     */
    int insertPayLog(PayLog payLog);

    /**
     * 新增代收费日志
     *
     * @param clPayLogs
     */
    void insertCLPayLog(List<CLPayLog> clPayLogs);

    /**
     * 新增收费其他信息日志
     *
     * @param payOtherLog
     */
    int insertPayOtherLog(PayOtherLog payOtherLog);

    /**
     * 交易关联日志
     *
     * @param chargeRelation
     * @return
     */
    int insertChargerelation(ChargeRelation chargeRelation);



    /**
     * 账务中心是否存在交易记录
     *
     * @param tradeId
     * @return
     */
    boolean ifExistOuterTradeId(String tradeId);


    /**
     * 账务中心根据外围流水获取交费记录
     *
     * @param outerTradeId
     * @return
     */
    List<PayLog> getPaylogByOuterTradeId(String outerTradeId);


    /**
     *账务中心根据chargeId和acctId获取交费记录
     * @param chargeId
     * @param acctId
     * @return
     */
    List<PayLog> getPaylogByChargeIdAndAcctId(String chargeId, String acctId);



    /**
     * 账务中心根据outerTradeId和acctId获取cancel_tag为0的交费记录
     *
     * @param outerTradeId
     * @param acctId
     * @return
     */
    List<PayLog> getPaylogByOuterTradeIdAndAcctId(String outerTradeId, String acctId);

    /**
     * 根据chargeId和acctId获取paylog_dmn表记录
     * @param chargeId
     * @param acctId
     * @return
     */
    List<PayLogDmn> getPaylogDmnByChargeIdAndAcctId(String chargeId, String acctId);

    /**
     * 生成账务后台工单表记录
     *
     * @param payLogDmn
     * @return
     */
    int insertPaylogDmn(PayLogDmn payLogDmn);


    /**
     * 返销更新收费其他信息表
     * @param chargeId
     * @param acctid
     * @return
     */
    int updatePayOtherlogByChargeIdAndAcctId(String chargeId, String acctid);

    /**
     * 返销更新原交费记录
     * @param tradeTradeStaff
     * @param chargeId
     * @param cancelTime
     * @param acctid
     * @return
     */
    int updatePayLogByChargeIdAndAcctId(TradeStaff tradeTradeStaff, String chargeId, String cancelTime, String acctid);

    /**
     * 返销更新交费记录
     * @param tradeTradeStaff
     * @param chargeId
     * @param cancelTime
     * @param acctid
     * @return
     */
    int updatePayLogDByChargeIdAndAcctId(TradeStaff tradeTradeStaff, String chargeId, String cancelTime, String acctid);

    /**
     * 查入新的clpaylog记录
     * @param clPaylog
     * @return
     */
    int insertClPaylog(CLPayLog clPaylog);

    /**
     * 获取clpaylog记录
     * @param chargeId
     * @param acctid
     * @return
     */
    List<CLPayLog> getClPaylogByChargeIdAndAcctId(String chargeId, String acctid);

    /**
     * 更新原clpaylog记录
     * @param clPayLog
     * @param dealTag
     * @return
     */
    int updateOrigClPaylog(CLPayLog clPayLog, String dealTag);

    /**
     * 更新原clpaylog记录
     * @param chargeId
     * @param acctid
     * @return
     */
    int updateOrigClPaylogByChargeIdAndAcctId(String chargeId, String acctid);

    

    /**
     * paylog_d入库
     * @param paylog
     * @return
     */
    int insertPaylogD(PayLog paylog);

    /**
     * 账务中心核查交费记录是否存在
     * @param outerTradeId
     * @param acctId
     * @return
     */
    boolean ifExistPaylogByChargeId(String outerTradeId, String acctId);


}
