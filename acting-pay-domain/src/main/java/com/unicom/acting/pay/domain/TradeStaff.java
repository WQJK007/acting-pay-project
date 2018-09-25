package com.unicom.acting.pay.domain;

/**
 * 交易员工信息对象
 *
 * @author ducj
 */
public class TradeStaff {
    private String staffId;
    private String departId;
    private String cityCode;
    private String eparchyCode;
    private String provinceCode;

    public String getStaffId() {
        return staffId;
    }

    public void setStaffId(String staffId) {
        this.staffId = staffId;
    }

    public String getDepartId() {
        return departId;
    }

    public void setDepartId(String departId) {
        this.departId = departId;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public String getEparchyCode() {
        return eparchyCode;
    }

    public void setEparchyCode(String eparchyCode) {
        this.eparchyCode = eparchyCode;
    }

    public String getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(String provinceCode) {
        this.provinceCode = provinceCode;
    }

    @Override
    public String toString() {
        return "TradeStaff{" +
                "staffId='" + staffId + '\'' +
                ", departId='" + departId + '\'' +
                ", cityCode='" + cityCode + '\'' +
                ", eparchyCode='" + eparchyCode + '\'' +
                ", provinceCode='" + provinceCode + '\'' +
                '}';
    }
}
