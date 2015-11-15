/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package persistencia;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Arrays;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import local.AccesoLocal;
import local.InicioServidor;
import local.PersistenciaImpl;
import modelo.dto.DataTable;
import remote.InterfaceManager;
import remote.Sitio1Int;
import remote.Sitio2Int;

/**
 *
 * @author jdosornio
 */
public class Sitio3Capacisoft {

    /**
     * @param args the command line arguments
     * @throws java.rmi.RemoteException
     * @throws java.rmi.NotBoundException
     */
    public static void main(String[] args) throws RemoteException, NotBoundException {
        //Iniciar servidor
        InicioServidor.iniciarServidor(11973, "Sitio3Capacisoft", AccesoLocal.class);
        
        InicioServidor.iniciarServidor(11977, "PersistInt", PersistenciaImpl.class);
        
        Scanner read = new Scanner(System.in);
        
        System.out.println("Esperar a que el otro servidor se conecte, presiona"
                + " enter cuando esté listo");
        
        read.nextLine();
        
        try {
            //Aquí va algún método para registrar todas las demás
            //interfaces obteniendo su ip....
            //Registrar los demás sitios
            
            int puerto = 11973;
            
            //Sitio 1
            InterfaceManager.addInterface("192.168.1.70", puerto, "Sitio1Capacisoft");
            //Sitio 2
            InterfaceManager.addInterface("192.168.1.72", puerto, "Sitio2Capacisoft");
        
        } catch (RemoteException | NotBoundException ex) {
            Logger.getLogger(Sitio3Capacisoft.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        //metodos de prueba
        String op;
        
        do {
            System.out.println("Opciones de Sitio1:\n-----------------------");
            System.out.println("getUsuario: 1");
            System.out.println("Opciones de Sitio2:\n-----------------------");
            System.out.println("getTemas: 2");
            op = read.nextLine();
            
            switch(op) {
                case "1":
                    DataTable dt = ((Sitio1Int)InterfaceManager.getInterface("Sitio1Capacisoft"))
                            .getUsuario("kaguilar");

                    System.out.println("Columnas: " + Arrays.toString(dt.getColumns()));

                    for (int i = 0; i < dt.getRowCount(); i++) {
                        for (int j = 0; j < dt.getColumnCount(); j++) {
                            System.out.println(dt.getColumnName(j) + ": " + dt.getValueAt(i, j));
                        }
                    }
                break;
                case "2": 
                    dt = ((Sitio2Int)InterfaceManager.getInterface("Sitio2Capacisoft"))
                            .getTemas(13);

                    System.out.println("Columnas: " + Arrays.toString(dt.getColumns()));

                    for (int i = 0; i < dt.getRowCount(); i++) {
                        for (int j = 0; j < dt.getColumnCount(); j++) {
                            System.out.println(dt.getColumnName(j) + ": " + dt.getValueAt(i, j));
                        }
                    }
                break;
            }
            
        } while(!op.equalsIgnoreCase("exit"));
    }
    
}