package bean;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import utility.dataHandler.UtilityMethod;

public class Supplier {
    private String id = null;
    private String name = null;
    private String nic = null;
    private String email = null;
    private String address = null;
    private String phoneNumber = null;
    private String type = null;

    public Supplier(String id, String name, String nic, String email, String address, String phoneNumber, String type) {
        this.id = UtilityMethod.addPrefix("S", id);
        this.name = name;
        this.nic = nic;
        this.email = email;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.type = type;
    }

    public Supplier() {

    }

    public String getId() {
        return id;
    }

    public StringProperty idProperty(){
        return new SimpleStringProperty(id);
    }

    public Integer getIntegerId() {
        return UtilityMethod.seperateID(id);
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public StringProperty nameProperty(){
        return new SimpleStringProperty(name);
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNic() {
        return nic;
    }

    public StringProperty nicProperty(){
        return new SimpleStringProperty(nic);
    }

    public void setNic(String nic) {
        this.nic = nic;
    }

    public String getEmail() {
        return email;
    }

    public StringProperty emailProperty(){
        return new SimpleStringProperty(email);
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public StringProperty addressProperty(){
        return new SimpleStringProperty(address);
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public StringProperty phoneNumberProperty(){
        return new SimpleStringProperty(phoneNumber);
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getType() {
        return type;
    }

    public StringProperty typeProperty(){
        return new SimpleStringProperty(type);
    }

    public void setType(String type) {
        this.type = type;
    }
}
