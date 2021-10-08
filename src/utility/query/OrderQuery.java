package utility.query;

public class OrderQuery {
    public static final String LOAD_ALL_ORDER_DATA = "SELECT * FROM orderlist";
    public static final String LOAD_SPECIFIC_ORDER_DATA = "SELECT * FROM orderlist WHERE oID = ?";
    public static final String INSERT_ORDER_DATA = "INSERT INTO orderlist (ocID, oNotes, oDate, oStatus) VALUES (?, ?, ?, ?)";
    public static final String UPDATE_ORDER_DATA = "UPDATE orderlist SET ocID = ?, oNotes = ?, oDate = ?, oStatus = ?WHERE oID = ?";
    public static final String DELETE_ORDER_DATA = "DELETE FROM orderlist WHERE oID = ?";
    public static final String GET_LAST_RECORD_ID = "SELECT * FROM orderlist ORDER BY oID DESC LIMIT 1;";

    public static final String LOAD_ALL_ORDER_PRODUCT_DATA = "SELECT * FROM orderitem WHERE oIOID = ?";
    public static final String INSERT_ORDER_ITEM_DATA = "INSERT INTO orderitem (oIOID, oIPID, oIPQty, oIUPrice) VALUES (?, ?, ?, ?)";
    public static final String DELETE_ORDER_PRODUCT_DATA = "DELETE FROM orderitem WHERE oIOID = ?";

}
