package utility.query;

public class SupplierQuery {
    public static final String LOAD_ALL_SUPPLIER_DATA = "SELECT * FROM supplier";
    public static final String LOAD_SPECIFIC_SUPPLIER_DATA = "SELECT * FROM supplier WHERE sID = ?";
    public static final String INSERT_SUPPLIER_DATA = "INSERT INTO supplier (sName, sNIC, sEmail, sAddress, sPhoneNumber, sType) VALUES (?,?,?,?,?,?)";
    public static final String UPDATE_SUPPLIER_DATA = "UPDATE supplier SET sName = ?, sNIC = ?, sEmail = ?, sAddress = ?, sPhoneNumber = ?, sType = ? WHERE sID = ?";
    public static final String DELETE_SUPPLIER_DATA = "DELETE FROM supplier WHERE sID = ?";
}
