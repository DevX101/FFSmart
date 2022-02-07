/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package smart_fridge.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import smart_fridge.model.Delivery;
import smart_fridge.model.Item;
import smart_fridge.view.Home;
import smart_fridge.view.Inventory;
import smart_fridge.view.Order;

/**
 *
 * @author T0266882
 */
public class InventoryManager {
    
    // Model
    private Item item;
    private Delivery delivery;
    
    // View
    private Inventory inventory;
    private Order order;
    private JFrame frame = new JFrame(); 

    public InventoryManager() {}

    public InventoryManager(Item item, Order order) {
        this.item = item;
        this.order = order;
    }

    public InventoryManager(Delivery delivery, Order order) {
        this.delivery = delivery;
        this.order = order;
    }

    public InventoryManager(Item item, Inventory inventory) {
        this.item = item;
        this.inventory = inventory;
    }
    
    public void displayInventoryTable(JTable inventoryTable) throws IOException{
        DefaultTableModel tblModel = (DefaultTableModel)inventoryTable.getModel();
        
        item = new Item();
        List<String[]> items = item.retrieveData();
        for(String[] row : items){
            tblModel.addRow(row);
        } 
    }
    
    public void checkExpiry(JTable inventoryTable){
        int rowCount = inventoryTable.getRowCount();
        for (int row = 0; row < rowCount; row++) {
            String date = inventoryTable.getValueAt(row, 2).toString();
            
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                Date dt = sdf.parse(date);
                Date today = new Date();
                boolean expired = dt.before(today);
                
                if(expired){
                    inventoryTable.setValueAt("EXPIRED", row, 2);
                    continue;
                }
                
                long time_difference = dt.getTime() - today.getTime();
                int days_difference = (int) (time_difference / (1000*60*60*24));
                if(days_difference <= 7){
                   inventoryTable.setValueAt(date+" (expiring soon)", row, 2); 
                }
            } catch (ParseException ex) {
                Logger.getLogger(Inventory.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public void itemExists(){
        List<String[]> items = item.retrieveData();
        boolean exists = false;
        int rowIndex = 0;
        for(String[] row : items){
            ++rowIndex;
            String name = row[0];
            String expiry = row[2];
            
            int quantity = Integer.parseInt(row[1]);
            int itemQuantity = item.getQuantity();
            
            if((item.getName() == null ? name == null : item.getName().equals(name)) && (item.getExpiry() == null ? expiry == null : item.getExpiry().equals(expiry))){
                quantity = quantity + itemQuantity;
                item.setQuantity(quantity);
                item.updateData(--rowIndex, 1);
                exists = true;
                JOptionPane.showMessageDialog(order, itemQuantity + " added to existing item!");
                break;
            }
        }
        if(!exists){
            JOptionPane.showMessageDialog(order, "Item added!");
            item.storeData();
        }
    }
    
    public boolean checkOrder() throws IOException{
        delivery = new Delivery();
        List<String[]> orders = delivery.retrieveData();
        boolean check = false;
        int index = 0;
        for(String[] row : orders){
            ++index;
            String name = row[0];
            int quantity = Integer.parseInt(row[1]);
            
            if((item.getName() == null ? name == null : item.getName().equals(name)) && item.getQuantity() == quantity){
                check = true;
                delivery.deleteData(index-1);
                break;
            }
        }
        if(!check){
           JOptionPane.showMessageDialog(order, "Item has not been ordered for dilivery!"); 
        }
        
        return check;
    }
    
    public boolean generateReport(){
        Calendar calendar = Calendar.getInstance();
        TimeZone.setDefault(TimeZone.getTimeZone("GMT"));        
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        
        // Write report every Monday.
        boolean monday = false;
        if(day == 2){
            monday = true;
            try {
                File report = new File("report.txt");
                try (FileWriter writeReport = new FileWriter("report.txt")) {
                    
                    item = new Item();
                    List<String[]> items = item.retrieveData();
                    int outStock = 0;
                    for(String[] row : items){
                        int quantity = Integer.parseInt(row[1]);
                        if(quantity == 0){
                            ++outStock;
                        }
                    }
                    
                    writeReport.write(
                            "Weekly Report"
                                    + "\n" + calendar.getTime()
                                    + "\n\nTotal types of items in inventory: " + items.size()
                                    + "\nNumber of items out of stock: " + outStock
                    );
                }
                
            } catch (IOException ex) {
                Logger.getLogger(InventoryManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return monday;
    }
    
    public void displayReport(boolean show, Home home) throws IOException{
        try {
            JPanel panel = new JPanel();
            if(show){
               BufferedReader readReport = new BufferedReader(new FileReader("report.txt"));
            
                String report;
                int line = 0;
                while((report = readReport.readLine()) != null){
                    ++line;
                    JLabel label = new JLabel("Line" + line);
                    label.setText(report);
                    panel.add(label);
                }
                readReport.close();

                frame.setResizable(false);
                frame.add(panel);
                frame.setSize(200, 200);
                frame.setVisible(true); 
            }else{
                frame.dispose();
            }
        } catch (FileNotFoundException ex) {
            JOptionPane.showMessageDialog(home, "No information to report.");
        }
    }
}
