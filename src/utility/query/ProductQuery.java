package utility.query;

public class ProductQuery {
    public static final String LOAD_ALL_PRODUCT_DATA = "SELECT * FROM product";
    public static final String LOAD_SPECIFIC_PRODUCT_DATA = "SELECT * FROM product WHERE pID = ?";
    public static final String INSERT_PRODUCT_DATA = "INSERT INTO product (pName, pInfo, pQuantity, pPrice, pAvailability) VALUES (?, ?, ?, ?, ?)";
    public static final String UPDATE_PRODUCT_DATA = "UPDATE product SET pName = ?, pInfo = ?, pQuantity = ?, pPrice = ?, pAvailability = ? WHERE pID = ?";
    public static final String DELETE_PRODUCT_DATA = "DELETE FROM product WHERE pID = ?";

}
