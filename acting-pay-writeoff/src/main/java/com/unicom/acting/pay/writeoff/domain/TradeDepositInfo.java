package com.unicom.acting.pay.writeoff.domain;

/**
 * 可清退转账账本明细信息
 *
 * @author Wangkh
 */
public class TradeDepositInfo {
    /**
     * @see #acctBalanceId 账本实例
     */
    private String acctBalanceId;
    /**
     * @see #userId 用户标识
     */
    private String userId;
    /**
     * @see #depositCode 用户标识
     */
    private String depositCode;
    /**
     * @see #remark 备注
     */
    private String remark;
    /**
     * @see #money 账本可清退/可转金额
     */
    private long money;

    /**
     * @see #forceBakeTag 强制处理标识
     */
    private char forceBakeTag;

    public String getAcctBalanceId() {
        return acctBalanceId;
    }

    public void setAcctBalanceId(String acctBalanceId) {
        this.acctBalanceId = acctBalanceId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDepositCode() {
        return depositCode;
    }

    public void setDepositCode(String depositCode) {
        this.depositCode = depositCode;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public long getMoney() {
        return money;
    }

    public void setMoney(long money) {
        this.money = money;
    }

    public char getForceBakeTag() {
        return forceBakeTag;
    }

    public void setForceBakeTag(char forceBakeTag) {
        this.forceBakeTag = forceBakeTag;
    }

    @Override
    public String toString() {
        return "TradeDepositInfo{" +
                "acctBalanceId='" + acctBalanceId + '\'' +
                ", userId='" + userId + '\'' +
                ", depositCode='" + depositCode + '\'' +
                ", remark='" + remark + '\'' +
                ", money=" + money +
                ", forceBakeTag=" + forceBakeTag +
                '}';
    }
}
