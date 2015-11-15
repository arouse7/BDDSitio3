/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package remote;

import modelo.dto.DataTable;

/**
 *
 * @author jdosornio
 */
public interface Sitio {
    
    public void insert(String tabla, DataTable datos);
    
    public void update(String tabla, DataTable datos);
    
    public void delete(String tabla, DataTable ids);
    
    public void commit();
    
    public void rollback();
}