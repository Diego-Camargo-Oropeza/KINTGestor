/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package pck_customComponents;

/**
 *
 * @author dieca
 */
public interface AccionesHandler {

    void editar(int idProducto, int modelRow);

    void eliminar(int idProducto, int modelRow);

    // Opcional: si alguna vista quiere usar el botón “Solicitar”
    void solicitar(int id, int modelRow);
}
