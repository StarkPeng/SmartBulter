package com.stark.smartbutler.entity;

/**
 * 项目名:  SmartButler
 *  *包名:    com.stark.smartbutler.entity
 * 文件名:  CourierData
 * 创建者:  Stark
 * 创建时间：2017/3/1 10:28
 * 描述：   TODO
 */
public class CourierData {

    //时间
    private String datetime;
    //状态
    private String remark;
    //城市
    private String zone;

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public void setZone(String zone) {
        this.zone = zone;
    }

    public String getZone() {

        return zone;
    }

    public String getRemark() {
        return remark;
    }

    public String getDatetime() {
        return datetime;
    }

    @Override
    public String toString() {
        return "CourierData{" +
                "datatime='" + datetime + '\'' +
                ", remark='" + remark + '\'' +
                ", zone='" + zone + '\'' +
                '}';
    }
}
