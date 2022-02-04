/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package smart_fridge.controller;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import smart_fridge.model.DataManagement;
import smart_fridge.model.Employee;
import smart_fridge.model.Item;
import smart_fridge.view.History;

/**
 *
 * @author T0266882
 */
public class RecordHistory implements DataManagement{
    
    private String action;
    private String date, time;
    
    // Model
    private Employee employee;
    private Item item;
    
    // View
    private History history;

    public RecordHistory() {}

    public RecordHistory(String action, String date, String time, String[] userDetails, Item item) {
        this.action = action;
        this.date = date;
        this.time = time;
        this.employee = new Employee(userDetails[0], userDetails[1], userDetails[2], userDetails[3], userDetails[4]);;
        this.item = item;
    }

    @Override
    public void storeData(){
        try (CSVWriter writer = new CSVWriter(new FileWriter("history.csv", true))) {
            String fullName = employee.getFirstName() + " " + employee.getLastName();
            String itemName = item.getName();
            String data[] = {fullName, employee.getRole(), itemName, action, date, time};
            writer.writeNext(data);
        } catch (IOException ex) {
            Logger.getLogger(RecordHistory.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public List<String[]> retrieveData() {
        List<String[]> employees = new ArrayList<>();
        try (CSVReader reader = new CSVReader(new FileReader("history.csv"))) {
            employees = reader.readAll();
        } catch (FileNotFoundException ex) {} 
        catch (IOException ex) {
            Logger.getLogger(RecordHistory.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return employees;
    }

    
    public void displayHistoryTable(JTable inventoryTable) {
        DefaultTableModel tblModel = (DefaultTableModel)inventoryTable.getModel();
        List<String[]> data = retrieveData();
        for(String[] row : data){
            tblModel.addRow(row);
        }
    }

    @Override
    public void updateData(int row, int col) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void deleteData(int index) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
