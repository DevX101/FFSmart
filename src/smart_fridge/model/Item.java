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
public class Item implements DataManagement{
    private String name;
    private int quantity;
    private String expiry; 

    public Item() {}

    // Deliver item
    public Item(String name, int quantity, String expiry) {
        this.name = name;
        this.quantity = quantity;
        this.expiry = expiry;
    }

    // Order item
    public Item(String name, int quantity) {
        this.name = name;
        this.quantity = quantity;
    }

    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getExpiry() {
        return expiry;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public void storeData() {
        try (CSVWriter writer = new CSVWriter(new FileWriter("inventory.csv", true))) {
            String items[] = {name, String.valueOf(quantity), expiry};
            writer.writeNext(items);
        } catch (IOException ex) {
            Logger.getLogger(Item.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public List<String[]> retrieveData() {
        List<String[]> items = new ArrayList<>();
        try (CSVReader reader = new CSVReader(new FileReader("inventory.csv"))) {
            items = reader.readAll();
        }catch(FileNotFoundException e){} 
        catch (IOException ex) {
            Logger.getLogger(Item.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return items;
    }

    @Override
    public void updateData(int row, int col) {
        try {
            List<String[]> items = retrieveData();
            items.get(row)[col] = String.valueOf(quantity);
            try (CSVWriter writer = new CSVWriter(new FileWriter("inventory.csv"))) {
                writer.writeAll(items);
            }
        }   catch (IOException ex) {
            Logger.getLogger(Item.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void deleteData(int index) {
        System.out.println(index);
        List<String[]> items = retrieveData();
        List<String[]> newItems = new ArrayList<>();
        
        int i = 0;
        for(String[] row : items){
            if(index != i){
                newItems.add(row);
                System.out.println(row);
            }
            ++i;
        }
        
        try (CSVWriter writer = new CSVWriter(new FileWriter("inventory.csv"))) {
            writer.writeAll(newItems);
        } catch (IOException ex) {
            Logger.getLogger(Delivery.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
