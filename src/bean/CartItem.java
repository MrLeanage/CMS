package bean;

import javafx.beans.property.*;
import utility.dataHandler.UtilityMethod;

public class CartItem {
    private String itemID = null;
    private String itemOrderID = null;
    private String itemProductID = null;
    private String itemProductName = null;
    private Integer itemQuantity = null;
    private Double itemUnitPrice = 0.0;
    private Double itemTotalPrice = 0.0;

    public CartItem() {
    }
    public CartItem(String itemID, String itemOrderID, String itemProductID, String itemProductName, Integer itemQuantity, Double itemUnitPrice) {
        this.itemID = UtilityMethod.addPrefix("L", itemID);
        this.itemOrderID = UtilityMethod.addPrefix("OR", itemOrderID);
        this.itemProductID = UtilityMethod.addPrefix("P", itemProductID);
        this.itemProductName = itemProductName;
        this.itemQuantity = itemQuantity;
        this.itemUnitPrice = itemUnitPrice;
        this.itemTotalPrice = itemQuantity * itemUnitPrice;
    }

    public CartItem(String itemProductID, String itemProductName, Integer itemQuantity, Double itemUnitPrice) {
        this.itemProductID = itemProductID;
        this.itemProductName = itemProductName;
        this.itemQuantity = itemQuantity;
        this.itemUnitPrice = itemUnitPrice;
        this.itemTotalPrice = itemQuantity * itemUnitPrice;
    }

    public String getItemID() {
        return itemID;
    }

    public StringProperty itemIDProperty(){
        return new SimpleStringProperty(itemID);
    }

    public void setItemID(String itemID) {
        this.itemID = itemID;
    }

    public String getItemOrderID() {
        return itemOrderID;
    }

    public void setItemOrderID(String itemOrderID) {
        this.itemOrderID = itemOrderID;
    }

    public String getItemProductID() {
        return itemProductID;
    }

    public void setItemProductID(String itemProductID) {
        this.itemProductID = itemProductID;
    }

    public String getItemProductName() {
        return itemProductName;
    }

    public StringProperty itemProductNameProperty(){
        return new SimpleStringProperty(itemProductName);
    }

    public void setItemProductName(String itemProductName) {
        this.itemProductName = itemProductName;
    }

    public Integer getItemQuantity() {
        return itemQuantity;
    }

    public IntegerProperty itemQuantityProperty(){
        return new SimpleIntegerProperty(itemQuantity);
    }

    public void setItemQuantity(Integer itemQuantity) {
        this.itemQuantity = itemQuantity;
    }

    public Double getItemUnitPrice() {
        return itemUnitPrice;
    }

    public DoubleProperty itemUnitPriceProperty(){
        return new SimpleDoubleProperty(itemUnitPrice);
    }

    public void setItemUnitPrice(Double itemUnitPrice) {
        this.itemUnitPrice = itemUnitPrice;
    }

    public Double getItemTotalPrice() {
        return itemTotalPrice;
    }

    public DoubleProperty itemTotalPriceProperty(){
        return new SimpleDoubleProperty(itemTotalPrice);
    }

    public void setItemTotalPrice(Double itemTotalPrice) {
        this.itemTotalPrice = itemTotalPrice;
    }
}
