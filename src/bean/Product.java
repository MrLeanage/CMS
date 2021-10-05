package bean;


import javafx.beans.property.*;
import utility.dataHandler.UtilityMethod;

public class Product {
    private String pID = null;
    private String pName = null;
    private String pInfo = null;
    private Integer pQuantity = null;
    private Double pPrice = null;
    private String pAvailability = null;

    public Product() {
    }

    public Product(String pID, String pName, String pInfo, Integer pQuantity, Double pPrice, String pAvailability) {
        this.pID = UtilityMethod.addPrefix("P", pID);
        this.pName = pName;
        this.pInfo = pInfo;
        this.pQuantity = pQuantity;
        this.pPrice = pPrice;
        this.pAvailability = pAvailability;
    }

    public String getpID() {
        return pID;
    }
    public StringProperty pIDProperty(){
        return new SimpleStringProperty(pID);
    }

    public void setpID(String pID) {
        this.pID = pID;
    }

    public String getpName() {
        return pName;
    }

    public StringProperty pNameProperty(){
        return new SimpleStringProperty(pName);
    }

    public void setpName(String pName) {
        this.pName = pName;
    }

    public String getpInfo() {
        return pInfo;
    }

    public StringProperty pInfoProperty(){
        return new SimpleStringProperty(pInfo);
    }

    public void setpInfo(String pInfo) {
        this.pInfo = pInfo;
    }

    public Integer getpQuantity() {
        return pQuantity;
    }

    public IntegerProperty pQuantityProperty(){
        return new SimpleIntegerProperty(pQuantity);
    }

    public void setpQuantity(Integer pQuantity) {
        this.pQuantity = pQuantity;
    }

    public Double getpPrice() {
        return pPrice;
    }

    public DoubleProperty pPriceProperty(){
        return new SimpleDoubleProperty(pPrice);
    }

    public void setpPrice(Double pPrice) {
        this.pPrice = pPrice;
    }

    public String getpAvailability() {
        return pAvailability;
    }

    public StringProperty pAvailabilityProperty(){
        return new SimpleStringProperty(pAvailability);
    }

    public void setpAvailability(String pAvailability) {
        this.pAvailability = pAvailability;
    }
}
