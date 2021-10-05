package utility.query;

public class DesignationQuery {
    public static final String LOAD_ALL_DESIGNATION_DATA = "SELECT * FROM designation";
    public static final String LOAD_SPECIFIC_DESIGNATION_DATA = "SELECT * FROM designation WHERE dID = ?";
    public static final String INSERT_DESIGNATION_DATA = "INSERT INTO designation (dName, dDescription, dStatus) VALUES (?, ?, ?)";
    public static final String UPDATE_DESIGNATION_DATA = "UPDATE designation SET dName = ?, dDescription = ?, dStatus = ? WHERE dID = ?";
    public static final String DELETE_DESIGNATION_DATA = "DELETE FROM designation WHERE dID = ?";
}
