/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo.dto;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author jdosornio
 */
public class DataTable extends AbstractTableModel {

    private String[] columns;
    private Object[][] data;
    
    
    public DataTable() {
        
    }
    
    public DataTable(String[] columns, Object[][] data) {

        this.columns = columns;
        this.data = data;
    }
    
    public String[] getColumns() {
        return columns;
    }
    
    @Override
    public int getRowCount() {
        
        return (data != null) ? data.length : 0;
    }

    @Override
    public int getColumnCount() {
        
        return (columns != null) ? columns.length : 0;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return data[rowIndex][columnIndex];
    }
    
    @Override
    public String getColumnName(int column) {
        return columns[column];
    }
    
    public int getColumnIndex(String name) {
        int indx = -1;
        
        for (int i = 0; i < columns.length; i++) {
            if(columns[i].equals(name)) {
                indx = i;
                break;
            }
        }
        
        return indx;
    }
    
    @Override
    public Class getColumnClass(int column) {
        return getValueAt(0, column).getClass();
    }
    
    public void populate(ResultSet rs) throws SQLException {
        
        if(rs != null) {
            
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();
            columns = new String[columnCount];
            List<Object[]> tempData = new ArrayList<>();


            for (int i = 0; i < columnCount; i++) {
                columns[i] = metaData.getColumnLabel(i + 1);
            }

            while(rs.next()) {
                Object[] row = new Object[columnCount];

                for (int i = 0; i < columnCount; i++) {
                    row[i] = rs.getObject(i + 1);
                }
                tempData.add(row);
            }

            data = new Object[tempData.size()][];
            tempData.toArray(data);
        }
    }
}