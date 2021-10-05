package bean;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import utility.dataHandler.UtilityMethod;

public class Designation {
    private String dID = null;
    private String dName = null;
    private String dDescription = null;
    private String dStatus = null;

    public Designation() {
    }

    public Designation(String dID, String dName, String dDescription, String dStatus) {
        this.dID = UtilityMethod.addPrefix("D", dID);
        this.dName = dName;
        this.dDescription = dDescription;
        this.dStatus = dStatus;
    }

    public String getdID() {
        return dID;
    }

    public StringProperty dIDProperty(){
        return new SimpleStringProperty(dID);
    }

    public void setdID(String dID) {
        this.dID = dID;
    }

    public String getdName() {
        return dName;
    }

    public StringProperty dNameProperty(){
        return new SimpleStringProperty(dName);
    }

    public void setdName(String dName) {
        this.dName = dName;
    }

    public String getdDescription() {
        return dDescription;
    }

    public StringProperty dDescriptionProperty(){
        return new SimpleStringProperty(dDescription);
    }

    public void setdDescription(String dDescription) {
        this.dDescription = dDescription;
    }

    public String getdStatus() {
        return dStatus;
    }

    public StringProperty dStatusProperty(){
        return new SimpleStringProperty(dStatus);
    }

    public void setdStatus(String dStatus) {
        this.dStatus = dStatus;
    }
}
