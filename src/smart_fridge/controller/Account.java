/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package smart_fridge.controller;

import java.io.IOException;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import smart_fridge.view.Home;
import smart_fridge.view.Login;
import smart_fridge.model.Employee;
import smart_fridge.view.Order;
import smart_fridge.view.Register;

/**
 *
 * @author T0266882
 */
public class Account {
    
    public static String[] userDetails;
    
    // Model
    private Employee employee;
    
    // View
    private Login login;
    private Register register;

    public Account() {}
    
    public Account(Employee employee, Login login) 
    {
        this.employee = employee;
        this.login = login;
    }
    
    public Account(Employee employee, Register register) 
    {
        this.employee = employee;
        this.register = register;
    }
    
    public void validateAccount() throws IOException{
        List<String[]> employees = employee.retrieveData();
        
        boolean found = false;
        for (String[] row : employees){
            String user = row[0], pwd = row[1];
            
            if ((user == null ? employee.getUsername() == null : user.equals(employee.getUsername())) && (pwd == null ? employee.getPassword() == null : pwd.equals(employee.getPassword()))){
                userDetails = row;
                
                if("Delivery Person".equals(userDetails[4])){
                    new Order().setVisible(true);
                }else{
                    new Home().setVisible(true);
                }
                
                login.dispose();
                found = true;
                break;
            }
        }
        
        if (!found){
            JOptionPane.showMessageDialog(login, "Incorrect credentials!", "Warning", JOptionPane.WARNING_MESSAGE);
        }
    }
    
    public void userExists(){
        List<String[]> employees = employee.retrieveData();
        
        boolean exists = false;
        for (String[] row : employees){
            String username = row[0];
            if(employee.getUsername() == null ? username == null : employee.getUsername().equals(username)){
                exists = true;
                JOptionPane.showMessageDialog(register, "Username already exists!", "Warning", JOptionPane.WARNING_MESSAGE);
                break;
            }
        }
        
        if(!exists){
            employee.storeData();
            JOptionPane.showMessageDialog(register, "Registration Successful!");
            new Login().setVisible(true);
            register.dispose();
        }
    }
    
    public void displayAccessTable(JTable inventoryTable){
        DefaultTableModel tblModel = (DefaultTableModel)inventoryTable.getModel();
        
        employee = new Employee();
        List<String[]> empAccess = employee.retrieveData();
        for(String[] row : empAccess){
            String fullName = row[2] + " " + row[3];
            String access = row[5];
            String role = row[4];
            
            if(!"Head Chef".equals(role)){
                String[] cell = {fullName, access};
                tblModel.addRow(cell);
            }
        } 
    }
}
