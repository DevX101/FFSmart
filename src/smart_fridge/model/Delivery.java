/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package smart_fridge.model;

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

/**
 *
 * @author T0266882
 */
public class Delivery extends Item implements DataManagement{
    private String supplier;

    public Delivery() {}

    public Delivery(String supplier, String name, int quantity) {
        super(name, quantity);
        this.supplier = supplier;
    }
    
    @Override
    // Chef places order to delivery person.
    public void storeData() {
        try (CSVWriter writer = new CSVWriter(new FileWriter("orders.csv", true))) {
            String order[] = {getName(), String.valueOf(getQuantity()), supplier};
            writer.writeNext(order);
        }catch (IOException ex) {
            Logger.getLogger(Item.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Override
    public List<String[]> retrieveData() {
        List<String[]> orders = new ArrayList<>();
        try (CSVReader reader = new CSVReader(new FileReader("orders.csv"))) {
            orders = reader.readAll();
        }catch(FileNotFoundException e){} 
        catch (IOException ex) {
            Logger.getLogger(Item.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return orders;
    }
    
    @Override
    public void deleteData(int index) {
        List<String[]> orders = retrieveData();
        List<String[]> newOrders = new ArrayList<>();
        
        int i = 0;
        for(String[] row : orders){
            if(index != i){
                newOrders.add(row);
            }
            ++i;
        }
        
        try (CSVWriter writer = new CSVWriter(new FileWriter("orders.csv"))) {
            writer.writeAll(newOrders);
        } catch (IOException ex) {
            Logger.getLogger(Delivery.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
