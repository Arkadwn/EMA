/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package emaaredespacio.gui.controlador;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import emaaredespacio.modelo.Colaborador;
import emaaredespacio.modelo.IColaborador;
import emaaredespacio.modelo.IPromocion;
import emaaredespacio.modelo.Promocion;
import emaaredespacio.persistencia.entidad.Colaboradores;
import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

/**
 * FXML Controller class
 *
 * @author enriq
 */
public class FXMLRegistrarPromocionController implements Initializable {

    @FXML
    private JFXTextField tfNombre;

    private List<Colaborador> lista;
    @FXML
    private JFXButton btnGuardar;
    @FXML
    private JFXButton btnCancelar;

    private Colaborador seleccion;
    @FXML
    private JFXTextField tfPromocion;
    @FXML
    private Spinner spinnerDescuento;
    String nombreColaborador = "armando";

    @FXML
    private RadioButton radioButtonMensual;
    @FXML
    private RadioButton radioButtonInscripcion;
    private ToggleGroup group;

    public Colaborador getSeleccion() {
        return seleccion;
    }

    public void setSeleccion(Colaborador seleccion) {
        this.seleccion = seleccion;
        tfNombre.setText(seleccion.getNombre() + " " + seleccion.getApellidos());
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        lista = new ArrayList();
        buscarColaborador();
        group = new ToggleGroup();
        radioButtonMensual.setToggleGroup(group);
        radioButtonInscripcion.setToggleGroup(group);
        radioButtonInscripcion.setSelected(true);

        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100, 50);
        spinnerDescuento.setValueFactory(valueFactory);

    }
    
    public boolean buscarColaborador() {
        boolean encontrado = false;
        IColaborador metodosColaborador = new Colaborador();
        lista.clear();
        lista = metodosColaborador.buscarColaborador(nombreColaborador);
        if (lista.isEmpty()) {
            MensajeController.mensajeInformacion("Maestro no encontrado");
        } else {
            seleccion = lista.get(0);
            tfNombre.setText(seleccion.getNombre() + " " + seleccion.getApellidos());
            encontrado = true;
        }
        return encontrado;
    }

    private boolean validarCamposVacios() {
        return tfPromocion.getText().trim().isEmpty() || seleccion.getIdColaborador().equals(0);
    }

    @FXML
    private void accionGuardar(ActionEvent evento) {
        if (validarCamposVacios()) {
            MensajeController.mensajeAdvertencia("Hay campos vacíos o erroneos");
        } else {
            if (tfPromocion.getText().length() < 50) {
                IPromocion metodosPromocion = new Promocion();
                Promocion promo = new Promocion();
                promo.setNombrePromocion(tfPromocion.getText());
                if (radioButtonMensual.isSelected()) {
                    promo.setTipoDescuento(true);
                } else {
                    promo.setTipoDescuento(false);
                }
                promo.setPorcentajeDescuento(spinnerDescuento.getValue().toString());
                promo.setEstado("A");
                System.out.println(seleccion.getIdColaborador());
                promo.setIdColaborador(seleccion.getIdColaborador());
                if (metodosPromocion.crearPromocion(promo)) {
                    MensajeController.mensajeInformacion("Promoción guardada");
                } else {
                    MensajeController.mensajeAdvertencia("No se pudo guardar la información");
                }
            } else {
                MensajeController.mensajeInformacion("Nombre de promoción inválido");
            }

        }
    }

}
