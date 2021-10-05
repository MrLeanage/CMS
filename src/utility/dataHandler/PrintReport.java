package utility.dataHandler;

import bean.Employee;
import bean.Order;
import bean.Product;
import bean.Supplier;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JRViewer;
import service.EmployeeService;
import service.ProductService;
import service.OrderService;
import service.SupplierService;
import utility.popUp.AlertPopUp;

import javax.swing.*;
import java.io.File;
import java.util.HashMap;

public class PrintReport extends JFrame {

  public void printOrderQuotation(Order order){
    ProductService productService = new ProductService();
    Product product = productService.loadSpecificProduct(order.getoProductID());

    try {
      HashMap parameter = new HashMap();
      parameter.put("orderID", order.getoID());
      parameter.put("itemID", product.getpID());
      parameter.put("itemName", product.getpName());
      parameter.put("itemInfo", product.getpInfo());
      parameter.put("unitPrice", product.getpPrice());
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
  public void printOrderDeliveryReport(String deliveryStatus){
    OrderService orderService = new OrderService();
    ObservableList<Order> orderObservableList = orderService.loadAllOrderData();

    ObservableList<Order> sortedOrderObservableList =  FXCollections.observableArrayList();

    for(Order order : orderObservableList){
      if(order.getoStatus().equals(deliveryStatus)){
        sortedOrderObservableList.add(order);
      }
    }
    if(sortedOrderObservableList.size()!=0){
      try {
        JRBeanCollectionDataSource jrBeanCollectionDataSource = new JRBeanCollectionDataSource(sortedOrderObservableList);
        HashMap parameter = new HashMap();
        parameter.put("orderList", jrBeanCollectionDataSource);
        parameter.put("deliveryStatus", deliveryStatus.toUpperCase());

        JasperDesign jd =
                JRXmlLoader.load(
                        new File("").getAbsolutePath() + "/src/view/jasperReport/OrderDeliveryInfo.jrxml");
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
    }else{
      AlertPopUp.noRecordFound(deliveryStatus);
    }
  }

  public void printSpecificSupplierReport(Supplier supplier){


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

  public void printSupplierReport(String supplierType){
    SupplierService supplierService = new SupplierService();
    ObservableList<Supplier> supplierObservableList = supplierService.loadAllSupplierData();

    ObservableList<Supplier> sortedSupplierObservableList =  FXCollections.observableArrayList();

    for(Supplier supplier : supplierObservableList){
      if(supplier.getType().equals(supplierType)){
        sortedSupplierObservableList.add(supplier);
      }
    }
    if(sortedSupplierObservableList.size()!=0){
      try {
        JRBeanCollectionDataSource jrBeanCollectionDataSource = new JRBeanCollectionDataSource(sortedSupplierObservableList);
        HashMap parameter = new HashMap();
        parameter.put("supplierList", jrBeanCollectionDataSource);
        parameter.put("sType", supplierType.toUpperCase());

        JasperDesign jd =
                JRXmlLoader.load(
                        new File("").getAbsolutePath() + "/src/view/jasperReport/MySuppliers.jrxml");
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
    }else{
      AlertPopUp.noRecordFound(supplierType);
    }
  }

  public void printSpecificEmployeeReport(Employee employee){


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
  public void printEmployeeReport(String employeeDesignation){
    EmployeeService employeeService = new EmployeeService();
    ObservableList<Employee> employeeObservableList = employeeService.loadAllEmployeeData();

    ObservableList<Employee> sortedEmployeeObservableList =  FXCollections.observableArrayList();

    if(employeeDesignation.equals("All")){
      sortedEmployeeObservableList.addAll(employeeObservableList);
    }else{
      for(Employee employee : employeeObservableList){
        if(employee.geteDesignation().equals(employeeDesignation)){
          sortedEmployeeObservableList.add(employee);
        }
      }
    }

    if(sortedEmployeeObservableList.size()!=0){
      try {
        JRBeanCollectionDataSource jrBeanCollectionDataSource = new JRBeanCollectionDataSource(sortedEmployeeObservableList);
        HashMap parameter = new HashMap();
        parameter.put("employeeList", jrBeanCollectionDataSource);
        parameter.put("employeeDesignation", employeeDesignation.toUpperCase());

        JasperDesign jd =
                JRXmlLoader.load(
                        new File("").getAbsolutePath() + "/src/view/jasperReport/EmployeeTeam.jrxml");
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
    }else{
      AlertPopUp.noRecordFound(employeeDesignation);
    }
  }

}
