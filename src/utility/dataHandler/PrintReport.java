package utility.dataHandler;

import bean.Employee;
import bean.Menu;
import bean.Order;
import bean.Supplier;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JRViewer;
import service.MenuService;
import utility.dbConnection.DBConnection;
import utility.popUp.AlertPopUp;

import javax.swing.*;
import java.io.File;
import java.sql.Connection;
import java.time.LocalDate;
import java.util.HashMap;

public class PrintReport extends JFrame {

  public void printCustomers() {
//    CustomerService customerService = new CustomerService();
//    ObservableList<Customer> customerObservableList = customerService.loadAllCustomerData();

//    JRBeanCollectionDataSource jrBeanCollectionDataSource = new JRBeanCollectionDataSource(customerObservableList);

//    try {
//      HashMap parameter = new HashMap();
//      parameter.put("date", String.valueOf(LocalDate.now()));
//      parameter.put("customerObservableList", jrBeanCollectionDataSource);
//
//      JasperDesign jd =
//          JRXmlLoader.load(
//              new File("").getAbsolutePath() + "/src/view/jasperReport/CustomerReport.jrxml");
//      JasperReport jasperReport = JasperCompileManager.compileReport(jd);
//      JasperPrint JasperPrint = JasperFillManager.fillReport(jasperReport, parameter,  new JREmptyDataSource());
//
//      JRViewer viewer = new JRViewer(JasperPrint);
//
//
//      // viewer.setOpaque(true);
//      viewer.setVisible(true);
//
//      add(viewer);
//      this.setSize(850, 800); // JFrame size
//      this.setVisible(true);
//
//    } catch (JRException e) {
//      e.printStackTrace();
//    }
  }
  public void printOrderQuotation(Order order){
    MenuService menuService = new MenuService();
    Menu menu = menuService.loadSpecificMenu(order.getoMenuID());

    try {
      HashMap parameter = new HashMap();
      parameter.put("orderID", order.getoID());
      parameter.put("itemID", menu.getmID());
      parameter.put("itemName", menu.getmName());
      parameter.put("itemInfo", menu.getmInfo());
      parameter.put("unitPrice", menu.getmPrice());
      parameter.put("orderQuantity", order.getoQuantity());
      parameter.put("orderNotes", order.getoNotes());
      parameter.put("deliveryStatus", order.getoStatus());
      parameter.put("customerName", order.getoCustomerName());

      JasperDesign jd =
          JRXmlLoader.load(
              new File("").getAbsolutePath() + "/src/view/jasperReport/OrderQuotationReport.jrxml");
      JasperReport jasperReport = JasperCompileManager.compileReport(jd);
      JasperPrint JasperPrint = JasperFillManager.fillReport(jasperReport, parameter,  new JREmptyDataSource());

      JRViewer viewer = new JRViewer(JasperPrint);


      // viewer.setOpaque(true);
      viewer.setVisible(true);

      add(viewer);
      this.setSize(850, 800); // JFrame size
      this.setVisible(true);

    } catch (JRException e) {
      e.printStackTrace();
    }
  }

  public void printSupplierReport(Supplier supplier){


    try {
      HashMap parameter = new HashMap();
      parameter.put("sID", supplier.getId());
      parameter.put("sName", supplier.getName());
      parameter.put("sNIC", supplier.getNic());
      parameter.put("contactNo", supplier.getPhoneNumber());
      parameter.put("email", supplier.getEmail());
      parameter.put("address", supplier.getAddress());
      parameter.put("sType", supplier.getType());

      JasperDesign jd =
              JRXmlLoader.load(
                      new File("").getAbsolutePath() + "/src/view/jasperReport/SupplierReport.jrxml");
      JasperReport jasperReport = JasperCompileManager.compileReport(jd);
      JasperPrint JasperPrint = JasperFillManager.fillReport(jasperReport, parameter,  new JREmptyDataSource());

      JRViewer viewer = new JRViewer(JasperPrint);


      // viewer.setOpaque(true);
      viewer.setVisible(true);

      add(viewer);
      this.setSize(850, 800); // JFrame size
      this.setVisible(true);

    } catch (JRException e) {
      e.printStackTrace();
    }
  }
  public void printEmployeeReport(Employee employee){


    try {
      HashMap parameter = new HashMap();
      parameter.put("eID", employee.geteID());
      parameter.put("eName", employee.geteName());
      parameter.put("eNIC", employee.geteNIC());
      parameter.put("contactNo", employee.geteContactNo().toString());
      parameter.put("designation", employee.geteDesignation());

      JasperDesign jd =
              JRXmlLoader.load(
                      new File("").getAbsolutePath() + "/src/view/jasperReport/EmployeeReport.jrxml");
      JasperReport jasperReport = JasperCompileManager.compileReport(jd);
      JasperPrint JasperPrint = JasperFillManager.fillReport(jasperReport, parameter,  new JREmptyDataSource());

      JRViewer viewer = new JRViewer(JasperPrint);


      // viewer.setOpaque(true);
      viewer.setVisible(true);

      add(viewer);
      this.setSize(850, 800); // JFrame size
      this.setVisible(true);

    } catch (JRException e) {
      e.printStackTrace();
    }
  }

}
