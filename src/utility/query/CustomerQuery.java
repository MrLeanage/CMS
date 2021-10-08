package utility.query;

public class CustomerQuery {
    public static final String LOAD_ALL_CUSTOMER_DATA = "SELECT * FROM customer";
    public static final String LOAD_SPECIFIC_CUSTOMER_DATA = "SELECT * FROM customer WHERE cID = ?";
    public static final String INSERT_CUSTOMER_DATA = "INSERT INTO customer (cName, cNIC, cAddress, cPhone) VALUES (?, ?, ?, ?)";
    public static final String UPDATE_CUSTOMER_DATA = "UPDATE customer SET cName = ?, cNIC = ?, cAddress = ?, cPhone = ? WHERE cID = ?";
    public static final String DELETE_CUSTOMER_DATA = "DELETE FROM customer WHERE cID = ?";
}
