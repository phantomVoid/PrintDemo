package net.printer.printdemo16.pojo;

public class XmlPojo {
    public String carNo;
    public String areaNo;
    public String timeStr;

    public XmlPojo() {
    }

    public XmlPojo(String carNo, String areaNo, String timeStr) {
        this.carNo = carNo;
        this.areaNo = areaNo;
        this.timeStr = timeStr;
    }

    public String getCarNo() {
        return carNo;
    }

    public void setCarNo(String carNo) {
        this.carNo = carNo;
    }

    public String getAreaNo() {
        return areaNo;
    }

    public void setAreaNo(String areaNo) {
        this.areaNo = areaNo;
    }

    public String getTimeStr() {
        return timeStr;
    }

    public void setTimeStr(String timeStr) {
        this.timeStr = timeStr;
    }

    @Override
    public String toString() {
        return "XmlPojo{" +
                "carNo='" + carNo + '\'' +
                ", areaNo='" + areaNo + '\'' +
                ", timeStr='" + timeStr + '\'' +
                '}';
    }
}
