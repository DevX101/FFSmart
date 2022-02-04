/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package smart_fridge.model;

import java.util.List;

/**
 *
 * @author Admin
 */
public interface DataManagement {
    
    void storeData();
    
    List<String[]> retrieveData();
    
    void updateData(int row, int col);
    
    void deleteData(int index);
}