package emaaredespacio.gui.controlador;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import emaaredespacio.modelo.Alumno;
import emaaredespacio.modelo.IAlumno;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;

/**
 * FXML Controller class
 *
 * @author Miguel Leonardo Jimenez Jimenez
 */
public class FXMLEditarAlumnoController implements Initializable {

    @FXML
    private JFXTextField tfNombre;
    @FXML
    private JFXTextField tfApellidos;
    @FXML
    private JFXTextField tfCorreo;
    @FXML
    private JFXTextField tfTelefono;
    @FXML
    private JFXTextField tfDireccion;
    @FXML
    private ImageView imgPerfil;
    @FXML
    private CheckBox checkEstado;
    @FXML
    private TableView<Alumno> tbListaAlumnos;
    @FXML
    private TableColumn<Alumno, String> columnaNombre;
    @FXML
    private TableColumn<Alumno, String> columnaApellidos;
    private String nombreImagen;
    private List<Alumno> lista;
    @FXML
    private JFXButton btnGuardar;
    @FXML
    private JFXButton btnCancelar;
    @FXML
    private JFXTextField tfPalabraClave;
    @FXML
    private JFXButton btnBuscar;
    private Alumno seleccion;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        columnaApellidos.setCellValueFactory(new PropertyValueFactory<>("apellidos"));
        columnaNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        nombreImagen = "No";
        lista = new ArrayList();
        Image imagen = new Image("emaaredespacio/imagenes/perfil.jpg", 300, 300, false, true, true);
        imgPerfil.setImage(imagen);
    }

    @FXML
    private void quitaSeleccion(MouseEvent event) {
        tbListaAlumnos.getSelectionModel().clearSelection();
    }

    private void llenarTabla() {
        tbListaAlumnos.getItems().clear();
        tbListaAlumnos.setItems(FXCollections.observableArrayList(lista));
    }

    @FXML
    private void posicion(MouseEvent event) {
        if (tbListaAlumnos.getSelectionModel().getSelectedIndex() >= 0) {
            Image imagen = null;
            seleccion = tbListaAlumnos.getSelectionModel().getSelectedItem();
            tfNombre.setText(seleccion.getNombre());
            tfApellidos.setText(seleccion.getApellidos());
            tfTelefono.setText(seleccion.getTelefono());
            tfDireccion.setText(seleccion.getDireccion());
            tfCorreo.setText(seleccion.getCorreo());

            File rutaImagen = new File(System.getProperty("user.home") + "\\imagenesAredEspacio\\imagenesAlumnos\\" + seleccion.getImagenPerfil());
            try {
                imagen = new Image(rutaImagen.toURI().toURL().toString(), 225, 225, false, true, true);
            } catch (MalformedURLException ex) {
                Logger.getLogger(FXMLEditarColaboradorController.class.getName()).log(Level.SEVERE, null, ex);
            }
            imgPerfil.setImage(imagen);
            nombreImagen = rutaImagen.getName();
            checkEstado.setSelected(seleccion.getEstado().equals("A"));
        }
    }

    @FXML
    private void accionBuscar() {
        if (tfPalabraClave.getText().isEmpty()) {
            System.out.println("No ha ingersado ningun caracter");
        } else {
            IAlumno metodosAlumno = new Alumno();
            lista.clear();
            lista = metodosAlumno.buscarAlumno(tfPalabraClave.getText());
            llenarTabla();
        }
    }

    @FXML
    private void accionGuardarCambios(ActionEvent event) {
        if(validarCamposVacios()){
            System.out.println("Campos vacios");
        }else{
            IAlumno metodosAlumnos = new Alumno();
            Alumno alumno = new Alumno();
            alumno.setNombre(tfNombre.getText());
            alumno.setApellidos(tfApellidos.getText());
            alumno.setCorreo(tfCorreo.getText());
            alumno.setDireccion(tfDireccion.getText());
            alumno.setImagenPerfil(nombreImagen);
            alumno.setTelefono(tfTelefono.getText());
            alumno.setMatricula(seleccion.getMatricula());
            if(checkEstado.isSelected()){
                alumno.setEstado("A");
            }else{
                alumno.setEstado("B");
            }
            
            
            
            boolean[] validacion = metodosAlumnos.validarCampos(alumno);
            if(validacion[5]){
                if(metodosAlumnos.editarAlumo(alumno)){
                    System.out.println("Alumno guardado");
                }else{
                    System.out.println("No se pudo");
                }
            }else{
                System.out.println("Campos invaldios");
            }
        }
    }

    @FXML
    public void elegirImagen(ActionEvent elegirImagen) throws IOException {
        FileChooser elegir = new FileChooser();
        FileChooser.ExtensionFilter extension = new FileChooser.ExtensionFilter("Add Files(*.jpg)", "*.jpg");
        FileChooser.ExtensionFilter extensionPNG = new FileChooser.ExtensionFilter("Add Files(*.png)", "*.png");
        FileChooser.ExtensionFilter extensionJPEG = new FileChooser.ExtensionFilter("Add Files(*.jpeg)", "*.jpeg");
        elegir.getExtensionFilters().add(extension);
        elegir.getExtensionFilters().add(extensionPNG);
        elegir.getExtensionFilters().add(extensionJPEG);
        File rutaImagen = elegir.showOpenDialog(null);
//    File rutaImagen = new File(System.getProperty("user.home") + "\\imagenesAredEspacio\\perfil.jpg");
        if (rutaImagen == null) {
            System.out.println("no es imagen");
        } else {
            Image image = null;
            String rutaNueva = System.getProperty("user.home") + "\\imagenesAredEspacio\\imagenesAlumnos";
            String rutaOringen = rutaImagen.getAbsolutePath();
            StringBuilder comando = new StringBuilder();
            comando.append("copy ").append('"' + rutaOringen + '"').append(" ").append('"' + rutaNueva + '"');
            ProcessBuilder builder = new ProcessBuilder("cmd.exe", "/c", comando.toString());
            builder.redirectErrorStream(true);
            Process proceso = builder.start();
            try {
                image = new Image(rutaImagen.toURI().toURL().toString(), 225, 225, false, true, true);
            } catch (MalformedURLException ex) {
                Logger.getLogger(FXMLEditarColaboradorController.class.getName()).log(Level.SEVERE, null, ex);
            }
            imgPerfil.setImage(image);
            nombreImagen = rutaImagen.getName();
        }
    }

    private boolean validarCamposVacios() {
        return tfNombre.getText().isEmpty() || tfApellidos.getText().isEmpty() || tfTelefono.getText().isEmpty()
                || tfDireccion.getText().isEmpty() || tfCorreo.getText().isEmpty();
    }
}
