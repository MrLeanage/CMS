package bean;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import utility.dataHandler.UtilityMethod;

import java.util.LinkedList;

public class Order {
    private String oID = null;
    private String oCID = null;
    private String oCName = null;
    private String oNotes = null;
    private LinkedList<CartItem> oItemList = new LinkedList<>();
    private String totalPrice = null;
    private String oDate = null;
    private String oStatus = null;

    public Order() {
    }

    public Order(String oID, String oCID, String oCName, String oNotes, LinkedList<CartItem> oItemList, String oDate, String oStatus) {
        this.oID = UtilityMethod.addPrefix("OR", oID);
        this.oCID = UtilityMethod.addPrefix("C", oCID);
        this.oCName = oCName;
        this.oNotes = oNotes;
        this.oItemList = oItemList;
        double total = 0;
        for(CartItem cartItem :oItemList){
            total += cartItem.getItemTotalPrice();
        }
        totalPrice = "Rs "+total + "0";
        this.oDate = oDate;
        this.oStatus = oStatus;
    }

    public String getoID() {
        return oID;
    }

    public Integer getIntegeroID() {
        return UtilityMethod.seperateID(oID);
    }

    public StringProperty oIDProperty(){
        return new SimpleStringProperty(oID);
    }

    public void setoID(String oID) {
        this.oID = oID;
    }

    public String getoCID() {
        return oCID;
    }

    public Integer getIntegeroCID() {
        return UtilityMethod.seperateID(oCID);
    }

    public StringProperty oCIDProperty(){
        return new SimpleStringProperty(oCID);
    }

    public void setoCID(String oCID) {
        this.oCID = oCID;
    }

    public String getoCName() {
        return oCName;
    }

    public StringProperty oCNameProperty(){
        return new SimpleStringProperty(oCName);
    }

    public void setoCName(String oCName) {
        this.oCName = oCName;
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

    public String getoStatus() {
        return oStatus;
    }

    public StringProperty oStatusProperty(){
        return new SimpleStringProperty(oStatus);
    }

    public void setoStatus(String oStatus) {
        this.oStatus = oStatus;
    }

    public LinkedList<CartItem> getoItemList() {
        return oItemList;
    }

    public void setoItemList(LinkedList<CartItem> oItemList) {
        this.oItemList = oItemList;
    }

    public String getoDate() {
        return oDate;
    }

    public StringProperty oDateProperty(){
        return new SimpleStringProperty(oDate);
    }

    public void setoDate(String oDate) {
        this.oDate = oDate;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public StringProperty totalPriceProperty(){
        return new SimpleStringProperty(totalPrice);
    }
}
