package bean;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import utility.dataHandler.UtilityMethod;

public class Order {
    private String oID = null;
    private String oCustomerName = null;
    private String oProductID = null;
    private String oProductName = null;
    private String oNotes = null;
    private Integer oQuantity = null;
    private String oStatus = null;

    public Order() {
    }

    public Order(String oID, String oCustomerName, String oProductID, String oProductName, String oNotes, Integer oQuantity, String oStatus) {
        this.oID = UtilityMethod.addPrefix("OR", oID);
        this.oCustomerName = oCustomerName;
        this.oProductID = UtilityMethod.addPrefix("P", oProductID);
        this.oProductName = oProductName;
        this.oNotes = oNotes;
        this.oQuantity = oQuantity;
        this.oStatus = oStatus;
    }

    public String getoID() {
        return oID;
    }

    public StringProperty oIDProperty(){
        return new SimpleStringProperty(oID);
    }

    public void setoID(String oID) {
        this.oID = oID;
    }

    public String getoCustomerName() {
        return oCustomerName;
    }

    public StringProperty oCustomerNameProperty(){
        return new SimpleStringProperty(oCustomerName);
    }

    public void setoCustomerName(String oCustomerName) {
        this.oCustomerName = oCustomerName;
    }

    public String getoProductID() {
        return oProductID;
    }

    public void setoProductID(String oProductID) {
        this.oProductID = oProductID;
    }

    public String getoProductName() {
        return oProductName;
    }

    public StringProperty oProductNameProperty(){
        return new SimpleStringProperty(oProductName);
    }

    public void setoProductName(String oProductName) {
        this.oProductName = oProductName;
    }

    public String getoNotes() {
        return oNotes;
    }

    public StringProperty oNotesProperty(){
        return new SimpleStringProperty(oNotes);
    }

    public void setoNotes(String oNotes) {
        this.oNotes = oNotes;
    }

    public Integer getoQuantity() {
        return oQuantity;
    }

    public IntegerProperty oQuantityProperty(){
        return new SimpleIntegerProperty(oQuantity);
    }

    public void setoQuantity(Integer oQuantity) {
        this.oQuantity = oQuantity;
    }

    public String getoStatus() {
        return oStatus;
    }

    public StringProperty oStatusProperty(){
        return new SimpleStringProperty(oStatus);
    }

    public void setoStatus(String oStatus) {
        this.oStatus = oStatus;
    }
}
