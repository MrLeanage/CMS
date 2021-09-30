package view.supplierManagement;

import bean.*;
import com.jfoenix.controls.JFXButton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import service.SupplierService;
import utility.dataHandler.PrintReport;
import utility.popUp.AlertPopUp;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class SupplerDetailController implements Initializable {
    @FXML
    private AnchorPane detailAnchorPane;

    @FXML
    private TableView<Supplier> supplierTable;

    @FXML
    private TableColumn<Supplier, String> iDColumn;

    @FXML
    private TableColumn<Supplier, String> nameColumn;

    @FXML
    private TableColumn<Supplier, String> nICColumn;

    @FXML
    private TableColumn<Supplier, String> emailColumn;

    @FXML
    private TableColumn<Supplier, String> addressColumn;

    @FXML
    private TableColumn<Supplier, String> contactColumn;

    @FXML
    private TableColumn<Supplier, String> typeColumn;

    @FXML
    private TextField searchTextField;

    @FXML
    private JFXButton updateButton;

    @FXML
    private JFXButton addButton;

    @FXML
    private Label supplierIDLabel;

    @FXML
    private TextField supplierIDTextBox;

    @FXML
    private Label supplierNameLabel;

    @FXML
    private TextField supplierNameTextBox;

    @FXML
    private JFXButton printSpecificSupplierButton;

    @FXML
    private Label supplierReportLabel;

    @FXML
    private ChoiceBox<String> supplierTypeChoiceBox;

    public  static Supplier selectedSupplier;

    private static boolean refreshTable = false;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setInitData();
        ObservableList<String> typeList = FXCollections.observableArrayList("Raw Material", "Ready Made", "Raw & Ready Made");
        supplierTypeChoiceBox.setValue(typeList.get(0));
        supplierTypeChoiceBox.setItems(typeList);
    }

    public void setInitData(){
        loadData();
        searchTable();
        refreshTable = false;
        clearFields();

    }

    public void loadData(){
        SupplierService supplierService = new SupplierService();
        ObservableList<Supplier> supplierObservableList = supplierService.loadAllSupplierData();

        iDColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        nICColumn.setCellValueFactory(new PropertyValueFactory<>("nic"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        addressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
        contactColumn.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));


        supplierTable.setItems(null);
        supplierTable.setItems(supplierObservableList);
        searchTable();

    }

    public void searchTable() {

        SupplierService supplierService = new SupplierService();

        SortedList<Supplier> sortedData = supplierService.searchTable(searchTextField);

        //binding the SortedList to TableView
        sortedData.comparatorProperty().bind(supplierTable.comparatorProperty());
        //adding sorted and filtered data to the table
        supplierTable.setItems(sortedData);
    }

    @FXML
    void addSupplierWindow(ActionEvent event) throws IOException{
        Stage primaryStage= new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("/view/supplierManagement/addSupplier.fxml"));
        Scene scene = new  Scene(root, 720, 350);
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.sizeToScene();
        primaryStage.showAndWait();

        if(refreshTable)
            setInitData();
    }

    @FXML
    void updateSupplierWindow(ActionEvent event) throws  IOException{
        if(selectedSupplier != null){
            UpdateSupplierController.setData(selectedSupplier);
            Stage updateStage= new Stage();
            Parent root = FXMLLoader.load(getClass().getResource("/view/supplierManagement/updateSupplier.fxml"));
            Scene scene = new  Scene(root, 720, 350);
            updateStage.setScene(scene);
            updateStage.setResizable(false);
            updateStage.sizeToScene();
            updateStage.showAndWait();
            if(refreshTable)
                setInitData();
        }else{
            AlertPopUp.selectRowToUpdate("Supplier");
        }
    }

    @FXML
    private void deleteSupplier(ActionEvent event) {
        if(selectedSupplier != null){
            SupplierService supplierService = new SupplierService();
            Optional<ButtonType> action = AlertPopUp.deleteConfirmation("Supplier");
            if (action.get() == ButtonType.OK) {
                if (supplierService.deleteSupplierData((selectedSupplier.getId()))) {
                    AlertPopUp.deleteSuccesfull("Supplier");
                    setInitData();
                } else
                    AlertPopUp.deleteFailed("Supplier");
            } else
                loadData();
        } else {
            AlertPopUp.selectRowToDelete("Supplier");
        }
    }

    @FXML
    private void setSelectedData(MouseEvent event) {
        try {
            supplierIDLabel.setVisible(true);
            supplierNameLabel.setVisible(true);
            supplierIDTextBox.setVisible(true);
            supplierNameTextBox.setVisible(true);
            printSpecificSupplierButton.setVisible(true);
            supplierReportLabel.setVisible(true);
            addButton.setVisible(false);
            updateButton.setVisible(true);
            selectedSupplier = supplierTable.getSelectionModel().getSelectedItem();

            supplierIDTextBox.setText(selectedSupplier.getId().toString());
            supplierNameTextBox.setText(selectedSupplier.getName());
            updateButton.setVisible(true);
            selectedSupplier = supplierTable.getSelectionModel().getSelectedItem();
        } catch (NullPointerException exception) {

        }
    }

    @FXML
    private void clearFields(ActionEvent event) {
        clearFields();
    }
    private void clearFields() {
        supplierIDTextBox.setText("");
        supplierNameTextBox.setText("");
        resetComponents();
        selectedSupplier = null;
    }
    private void resetComponents() {
        addButton.setVisible(true);
        updateButton.setVisible(false);
        supplierIDLabel.setVisible(false);
        supplierNameLabel.setVisible(false);
        supplierIDTextBox.setVisible(false);
        supplierNameTextBox.setVisible(false);
        printSpecificSupplierButton.setVisible(false);
        supplierReportLabel.setVisible(false);
    }
    public void setState(boolean refresh){
        refreshTable = refresh;
    }
    @FXML
    private void getSpecificSupplierReport(ActionEvent event){
        if(selectedSupplier != null){
            PrintReport printReport = new PrintReport();
            printReport.printSpecificSupplierReport(selectedSupplier);
        }
    }
    @FXML
    private void getSupplierReport(ActionEvent event){
        PrintReport printReport = new PrintReport();
        printReport.printSupplierReport(supplierTypeChoiceBox.getValue());
    }
}
