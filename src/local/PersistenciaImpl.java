/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package local;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.logging.Level;
import java.util.logging.Logger;
import modelo.dao.ReactivoDAO;
import modelo.dto.DataTable;
import persistencia.Persistencia;
import remote.InterfaceManager;
import remote.Sitio1Int;
import remote.Sitio2Int;
/**
 *
 * @author jdosornio
 */
public class PersistenciaImpl extends UnicastRemoteObject implements Persistencia  {

    public PersistenciaImpl() throws RemoteException {
        
    }
    
    @Override
    public DataTable getUsuario(String usuario) throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet");
    }

    @Override
    public DataTable getTemas(int idCurso) throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public DataTable getReactivosCreados(String usuario, int idCurso) throws RemoteException {
        
        System.out.println("Obteniendo reactivos con usuario: " + usuario + " y "
                + "idCurso: " + idCurso);
        
        DataTable dt = null;
        
        try {
            Sitio1Int sitio1 = (Sitio1Int) InterfaceManager.getInterface("Sitio1Capacisoft");
            Sitio2Int sitio2 = (Sitio2Int) InterfaceManager.getInterface("Sitio2Capacisoft");
            
            dt = sitio1.getUsuario(usuario);
            
            if(dt != null && dt.getRowCount() > 0) {
                int idUsuario = (int) dt.getValueAt(0, dt.getColumnIndex("id"));
                
                //Obtener los temas
                dt = sitio2.getTemas(idCurso);
                
                if (dt != null && dt.getRowCount() > 0) {
                    //Obtener los reactivos
                    dt = new ReactivoDAO().getReactivosCreadosDeCurso(idUsuario, dt);
                }
            }
            
        } catch (NotBoundException ex) {
            Logger.getLogger(PersistenciaImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return dt;
    }
    
}