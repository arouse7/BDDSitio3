/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import modelo.dto.DataTable;

/**
 *
 * @author jdosornio
 */
public class ReactivoDAO {

    //Atributos entidad
    private static final String ID_AUTOR = "idAutor";
    private static final String ID_TEMA = "idTema";

    private static final String CREATE_TEMP_TABLE = "CREATE TEMPORARY TABLE "
            + "tema_temp(idTema INT)";

    private static final String ADD_TEMP_TEMAS = "INSERT INTO tema_temp VALUES (?)";

    private static final String GET_REACTIVOS_BY_AUTOR = "SELECT * "
            + "FROM reactivo WHERE " + ID_AUTOR + " = ? AND " + ID_TEMA + " IN "
            + "(SELECT idTema FROM tema_temp)";

    public DataTable getReactivosCreadosDeCurso(int idUsuario, DataTable temas) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        DataTable result = null;
        Connection conexion;

        try {
            conexion = ConnectionManager.conectar();

            addTempTemas(temas, conexion, ps);
            
            ps = conexion.prepareStatement(GET_REACTIVOS_BY_AUTOR);
            ps.setInt(1, idUsuario);

            rs = ps.executeQuery();

            result = new DataTable();

            result.populate(rs);

            //ConnectionManager.commit();
        } catch (SQLException ex) {
            ConnectionManager.rollback();
        } finally {
            ConnectionManager.cerrarTodo(ps, rs);
        }
        return result;
    }

    private void addTempTemas(DataTable temas, Connection conexion, PreparedStatement ps)
            throws SQLException {

        ps = conexion.prepareStatement(CREATE_TEMP_TABLE);
        ps.execute();

        ps = conexion.prepareStatement(ADD_TEMP_TEMAS);

        for (int i = 0; i < temas.getRowCount(); i++) {
            ps.setInt(1, (int) temas.getValueAt(i, temas.getColumnIndex("id")));
            ps.addBatch();
            //System.out.println("Tema: " + i + 1);
        }

        ps.executeBatch();
    }
    
//    public static void main(String[] args) throws SQLException {
//        DataTable dt = new DataTable();
//        
//        Connection conexion = ConnectionManager.conectar();
//        
//        PreparedStatement ps = conexion.prepareStatement("SELECT t.id, t.nombre "
//                + "FROM tema AS t, curso AS c, curso_tema AS ct "
//                + "WHERE c.id = ct.idCurso AND t.id = ct.idTema AND c.id = 13");
//        
//        ResultSet rs = ps.executeQuery();
//        dt.populate(rs);
//        
//        ConnectionManager.cerrarTodo(ps, rs);
//        
//        System.out.println(dt.getRowCount());
//        
//        DataTable reactivos = new ReactivoDAO().getReactivosCreadosDeCurso(67, dt);
//        
//        System.out.println(reactivos.getRowCount());
//        
//        for (int i = 0; i < reactivos.getRowCount(); i++) {
//            System.out.println("Id: " + reactivos.getValueAt(i, 0) + " "
//                    + "Nombre: " + reactivos.getValueAt(i, 1));
//        }
//    }

}