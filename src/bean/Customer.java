package bean;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import utility.dataHandler.UtilityMethod;

public class Customer {
    private String cID = null;
    private String cName = null;
    private String cNIC = null;
    private String cAddress = null;
    private String cPhone = null;

    public Customer() {
    }

    public Customer(String cID, String cName, String cNIC, String cAddress, String cPhone) {
        this.cID = UtilityMethod.addPrefix("C", cID);
        this.cName = cName;
        this.cNIC = cNIC;
        this.cAddress = cAddress;
        this.cPhone = cPhone;
    }

    public String getcID() {
        return cID;
    }

    public Integer getIntegercID() {
        return UtilityMethod.seperateID(cID);
    }

    public StringProperty cIDProperty(){
        return new SimpleStringProperty(cID);
    }

    public void setcID(String cID) {
        this.cID = cID;
    }

    public String getcName() {
        return cName;
    }

    public StringProperty cNameProperty(){
        return new SimpleStringProperty(cName);
    }

    public void setcName(String cName) {
        this.cName = cName;
    }

    public String getcNIC() {
        return cNIC;
    }

    public StringProperty cNICProperty(){
        return new SimpleStringProperty(cNIC);
    }

    public void setcNIC(String cNIC) {
        this.cNIC = cNIC;
    }

    public String getcAddress() {
        return cAddress;
    }

    public StringProperty cAddressProperty(){
        return new SimpleStringProperty(cAddress);
    }

    public void setcAddress(String cAddress) {
        this.cAddress = cAddress;
    }

    public String getcPhone() {
        return cPhone;
    }

    public StringProperty cPhoneProperty(){
        return new SimpleStringProperty(cPhone);
    }

    public void setcPhone(String cPhone) {
        this.cPhone = cPhone;
    }
}
