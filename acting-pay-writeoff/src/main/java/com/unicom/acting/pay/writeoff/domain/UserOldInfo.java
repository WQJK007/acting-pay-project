package com.unicom.acting.pay.writeoff.domain;

/**
 * 用户省份信息
 *
 * @author Wangkh
 */
public class UserOldInfo {
    private String userId;
    private String oldUserId;
    private String oldAcctId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getOldUserId() {
        return oldUserId;
    }

    public void setOldUserId(String oldUserId) {
        this.oldUserId = oldUserId;
    }

    public String getOldAcctId() {
        return oldAcctId;
    }

    public void setOldAcctId(String oldAcctId) {
        this.oldAcctId = oldAcctId;
    }

    @Override
    public String toString() {
        return "UserOldInfo{" +
                "userId='" + userId + '\'' +
                ", oldUserId='" + oldUserId + '\'' +
                ", oldAcctId='" + oldAcctId + '\'' +
                '}';
    }
}
