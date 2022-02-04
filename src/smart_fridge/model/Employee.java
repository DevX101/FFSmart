/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package smart_fridge.model;

import java.io.FileWriter;
import java.io.FileReader;
import com.opencsv.CSVWriter;
import com.opencsv.CSVReader; 
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author T0266882
 */
public class Employee implements DataManagement{
    private String username;
    private String password;
    private String firstName, lastName;
    private String role;
    private boolean access;  
    private String supplier = "N/A";
    
    public Employee() {}

    public Employee(String username, String password, String firstName, String lastName, String role) {
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.role = role;
    }

    public Employee(String username, String password) {
        this.username = username;
        this.password = password; 
    }

    public String getUsername() {
        return username;
    }
    
    public String getPassword() {
        return password;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getRole() {
        return role;
    }

    public boolean getAccess() {
        return access;
    }

    public void setAccess(boolean access) {
        this.access = access;
    }

    public String getSupplier() {
        return supplier;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }

    @Override
    public void storeData() {
        try (CSVWriter writer = new CSVWriter(new FileWriter("employees.csv", true))) {
            String credentials[] = {username, password, firstName, lastName, role, String.valueOf(access), supplier};
            writer.writeNext(credentials);
        } catch (IOException ex) {
            Logger.getLogger(Employee.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public List<String[]> retrieveData() {
        List<String[]> employees = new ArrayList<>();
        try (CSVReader reader = new CSVReader(new FileReader("employees.csv"))) {
            employees = reader.readAll();
        }
        catch(FileNotFoundException e){} 
        catch (IOException ex) {
            Logger.getLogger(Employee.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return employees;
    }

    @Override
    public void updateData(int row, int col) {
        
        try {
            List<String[]> data;
            data = retrieveData();
            data.get(row)[col] = String.valueOf(access);
            
            try (CSVWriter writer = new CSVWriter(new FileWriter("employees.csv"))) {
            writer.writeAll(data);
        }
        } catch (IOException ex) {
            Logger.getLogger(Employee.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }

    @Override
    public void deleteData(int index) {
        List<String[]> employees = retrieveData();
        List<String[]> newEmployees = new ArrayList<>();
        
        int i = 0;
        for(String[] row : employees){
            if(index != i){
                newEmployees.add(row);
            }
            ++i;
        }
        
        try (CSVWriter writer = new CSVWriter(new FileWriter("orders.csv"))) {
            writer.writeAll(newEmployees);
        } catch (IOException ex) {
            Logger.getLogger(Delivery.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
