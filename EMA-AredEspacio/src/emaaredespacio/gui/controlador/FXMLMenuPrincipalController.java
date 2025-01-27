package emaaredespacio.gui.controlador;

import com.jfoenix.controls.JFXButton;
import emaaredespacio.EMAAredEspacio;
import emaaredespacio.modelo.Colaborador;
import emaaredespacio.modelo.Aviso;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.AnchorPane;
import javafx.stage.WindowEvent;

/**
 * FXML Controller class
 *
 * @author Miguel Leonardo Jimenez Jimenez
 */
public class FXMLMenuPrincipalController implements Initializable {

    private boolean menuDesplegado;
    @FXML
    private JFXButton btnSalir;
    @FXML
    private JFXButton btnInicio;
    @FXML
    private AnchorPane barraMenu;
    @FXML
    private AnchorPane panelPrincipal;
    @FXML
    private Label labelNombreSesion;
    private Colaborador colaborador;
    private EMAAredEspacio main;
    @FXML
    private JFXButton buttonSiguiente;
    @FXML
    private TextField tfNombreAlumno;
    @FXML
    private TextField tfTipoPago;
    @FXML
    private JFXButton buttonAtras;
    @FXML
    private AnchorPane anchorPaneAviso;
    private List<Aviso> listaAvisos;
    @FXML
    private TextField tfGrupo;
    @FXML
    private JFXButton btnAvisos;
    private boolean avisosDesplegados;
    private boolean esDirector;
    private Aviso avisos;
    @FXML
    private Label lbGrupo;
    @FXML
    private Label lbPagoCorrespondiente;
    @FXML
    private Label lbTipoDePago;
    @FXML
    private JFXButton btnAdministrarPagoAColaborador;
    @FXML
    private JFXButton btnRegistrarPagoDeRenta;
    @FXML
    private JFXButton btnGenerarReporte;
    @FXML
    private JFXButton btnHistorialPagoDeEspacio;
    @FXML
    private JFXButton btnRegistrarGrupo;
    @FXML
    private JFXButton btnModificarGrupo;
    @FXML
    private JFXButton btnAdministrarHorarios;
    @FXML
    private JFXButton btnAdministrarRentas;
    @FXML
    private JFXButton btnRegistrarAlumnos;
    @FXML
    private JFXButton btnEditarAlumnos;
    @FXML
    private TitledPane titledPaneEgresos;
    @FXML
    private TitledPane titleCilenteColaborador;

    public EMAAredEspacio getMain() {
        return main;
    }

    public void setMain(EMAAredEspacio main) {
        this.main = main;
        this.main.getVentana().setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                System.exit((0));
            }
        });
    }

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        menuDesplegado = false;
        panelPrincipal.setStyle("-fx-background-image: url('emaaredespacio/imagenes/fondo.jpg');"
                + "-fx-background-position: center center; -fx-background-repeat: stretch;");
        btnInicio.setStyle("-fx-background-image: url('emaaredespacio/imagenes/inicio.png');"
                + "-fx-background-position: center center; -fx-background-repeat: stretch; "
                + "-fx-background-size: 30px 30px 30px 30px;");
        btnSalir.setStyle("-fx-background-image: url('emaaredespacio/imagenes/salir.png');"
                + "-fx-background-position: center center; -fx-background-repeat: stretch;"
                + " -fx-background-size: 30px 30px 30px 30px;");
        btnAvisos.setStyle("-fx-background-image: url('emaaredespacio/imagenes/aviso.png');"
                + "-fx-background-position: center center; -fx-background-repeat: stretch;"
                + " -fx-background-size: 30px 30px 30px 30px;");
        avisosDesplegados = false;
    }

    public void actualizarAvisos(List<Aviso> avisos) {
        this.listaAvisos = avisos;
        System.out.println("actualizado");
        if (!avisos.isEmpty()) {
            mostrarCamposDeAviso();
            this.listaAvisos = avisos;
            tfNombreAlumno.setText(String.valueOf(avisos.get(0).getNombre()));
            tfTipoPago.setText(avisos.get(0).getTipoDePago());
            if (avisos.get(0).getGrupo() == "") {
                tfGrupo.setVisible(false);
                lbGrupo.setText("Pago de colaborador");
            } else {
                tfGrupo.setText(avisos.get(0).getGrupo());
                tfGrupo.setVisible(true);
                lbGrupo.setText("Grupo");
            }
        } else {
            ocultarCamposDeAviso();
        }
    }

    public void ocultarCamposDeAviso() {
        lbPagoCorrespondiente.setText("No hay avisos pendientes");
        lbTipoDePago.setVisible(false);
        tfGrupo.setVisible(false);
        tfTipoPago.setVisible(false);
        lbGrupo.setVisible(false);
        tfNombreAlumno.setVisible(false);
    }

    public void mostrarCamposDeAviso() {
        lbPagoCorrespondiente.setText("Pago correspondiente de");
        lbTipoDePago.setVisible(true);
        tfGrupo.setVisible(true);
        tfTipoPago.setVisible(true);
        lbGrupo.setVisible(false);
        tfNombreAlumno.setVisible(true);
    }

    public void setColaborador(Colaborador colaborador) {
        this.colaborador = colaborador;
        System.getProperties().put("colaborador", colaborador.getNombre() + " " + colaborador.getApellidos());
        System.getProperties().put("idColaborador", "" + colaborador.getIdColaborador());
        labelNombreSesion.setText(this.colaborador.getNombre() + " " + this.colaborador.getApellidos());
        avisos = new Aviso();
        listaAvisos = new ArrayList<>();
        avisos.setColaborador(colaborador);
        avisos.setVentanaPricipal(this);
        Thread hiloAvisos = new Thread(avisos);


        if (colaborador.getCargo().equals(1)) {

            btnAdministrarHorarios.setVisible(false);
            btnAdministrarPagoAColaborador.setVisible(false);
            btnAdministrarRentas.setVisible(false);
            btnEditarAlumnos.setVisible(false);
            btnGenerarReporte.setVisible(false);
            btnHistorialPagoDeEspacio.setVisible(false);
            btnModificarGrupo.setVisible(false);
            btnRegistrarAlumnos.setVisible(false);
            btnRegistrarGrupo.setVisible(false);
            btnRegistrarPagoDeRenta.setVisible(false);
            titledPaneEgresos.setVisible(false);
            titleCilenteColaborador.setVisible(false);
        }
        hiloAvisos.start();
    }

    @FXML
    private void mostrarMenu(ActionEvent evento) {
        menuDesplegado = !menuDesplegado;
        barraMenu.setVisible(menuDesplegado);
    }

    @FXML
    private void ocultarMenu() {
        barraMenu.setVisible(false);
        menuDesplegado = false;
    }

    @FXML
    private void desplegarVentanaAdministrarColaboradores(ActionEvent evento) throws IOException {
        panelPrincipal.getChildren().clear();
        Parent fxml = FXMLLoader.load(getClass().getResource("/emaaredespacio/gui/vista/FXMLRegistrarColaborador.fxml"));
        panelPrincipal.getChildren().addAll(fxml.getChildrenUnmodifiable());
        barraMenu.setVisible(false);
        menuDesplegado = false;
    }

    @FXML
    private void desplegarVentanaAdministrarHorarios(ActionEvent evento) throws IOException {
        panelPrincipal.getChildren().clear();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/emaaredespacio/gui/vista/FXMLAdministrarHorarios.fxml"));
        Parent fxml = (Parent) loader.load();
        FXMLAdministrarHorariosController control = loader.getController();
        control.setPanelPrincipal(panelPrincipal);
        panelPrincipal.getChildren().addAll(fxml.getChildrenUnmodifiable());
        barraMenu.setVisible(false);
        menuDesplegado = false;
    }

    @FXML
    private void desplegarVentanaRegistrarGrupo(ActionEvent evento) throws IOException {
        panelPrincipal.getChildren().clear();
        Parent fxml = FXMLLoader.load(getClass().getResource("/emaaredespacio/gui/vista/FXMLRegistrarGrupo.fxml"));
        panelPrincipal.getChildren().addAll(fxml.getChildrenUnmodifiable());
        barraMenu.setVisible(false);
        menuDesplegado = false;
    }

    @FXML
    private void desplegarVentanaModificarGrupo(ActionEvent evento) throws IOException {
        panelPrincipal.getChildren().clear();
        Parent fxml = FXMLLoader.load(getClass().getResource("/emaaredespacio/gui/vista/FXMLModificarGrupo.fxml"));
        panelPrincipal.getChildren().addAll(fxml.getChildrenUnmodifiable());
        barraMenu.setVisible(false);
        menuDesplegado = false;
    }

    @FXML
    private void desplegarVentanaRegistrarPromocion(ActionEvent evento) throws IOException {
        panelPrincipal.getChildren().clear();
        FXMLLoader cargador = new FXMLLoader(getClass().getResource("/emaaredespacio/gui/vista/FXMLRegistrarPromocion.fxml"));
        Parent fxml = (Parent) cargador.load();
        FXMLRegistrarPromocionController controler = cargador.getController();
        panelPrincipal.getChildren().addAll(fxml.getChildrenUnmodifiable());
        barraMenu.setVisible(false);
        menuDesplegado = false;
    }

    @FXML
    private void desplegarVentanaModificarPromocion(ActionEvent evento) throws IOException {
        panelPrincipal.getChildren().clear();
        FXMLLoader cargador = new FXMLLoader(getClass().getResource("/emaaredespacio/gui/vista/FXMLModificarPromocion.fxml"));
        Parent fxml = (Parent) cargador.load();
        FXMLModificarPromocionController controler = cargador.getController();
        panelPrincipal.getChildren().addAll(fxml.getChildrenUnmodifiable());
        barraMenu.setVisible(false);
        menuDesplegado = false;
    }

    @FXML
    private void desplegarVentanaRegistrarCliente(ActionEvent evento) throws IOException {
        panelPrincipal.getChildren().clear();
        Parent fxml = FXMLLoader.load(getClass().getResource("/emaaredespacio/gui/vista/FXMLRegistrarCliente.fxml"));
        panelPrincipal.getChildren().addAll(fxml.getChildrenUnmodifiable());
        barraMenu.setVisible(false);
        menuDesplegado = false;
    }

    @FXML
    private void desplegarVentanaModificarCliente(ActionEvent evento) throws IOException {
        panelPrincipal.getChildren().clear();
        Parent fxml = FXMLLoader.load(getClass().getResource("/emaaredespacio/gui/vista/FXMLModificarCliente.fxml"));
        panelPrincipal.getChildren().addAll(fxml.getChildrenUnmodifiable());
        barraMenu.setVisible(false);
        menuDesplegado = false;
    }

    @FXML
    private void limpiar(ActionEvent evento) throws IOException {
        avisos.cerrarAvisos();
        main.desplegarInicioDeSesion();
    }

    @FXML
    public void desplegarVentana(ActionEvent evento) throws IOException {
        panelPrincipal.getChildren().clear();
        Parent fxml = FXMLLoader.load(getClass().getResource("/emaaredespacio/gui/vista/FXMLEditarColaborador.fxml"));
        panelPrincipal.getChildren().addAll(fxml.getChildrenUnmodifiable());
        barraMenu.setVisible(false);
        menuDesplegado = false;
    }

    @FXML
    private void desplegarAdminstrarAlumnos(ActionEvent evento) throws IOException {
        panelPrincipal.getChildren().clear();
        Parent fxml = FXMLLoader.load(getClass().getResource("/emaaredespacio/gui/vista/FXMLRegistrarAlumno.fxml"));
        panelPrincipal.getChildren().addAll(fxml.getChildrenUnmodifiable());
        barraMenu.setVisible(false);
        menuDesplegado = false;
    }

    @FXML
    private void desplegarEditarAlumno(ActionEvent evento) throws IOException {
        panelPrincipal.getChildren().clear();
        Parent fxml = FXMLLoader.load(getClass().getResource("/emaaredespacio/gui/vista/FXMLEditarAlumno.fxml"));
        panelPrincipal.getChildren().addAll(fxml.getChildrenUnmodifiable());
        barraMenu.setVisible(false);
        menuDesplegado = false;
    }

    @FXML
    private void desplegarAdministrarRentas(ActionEvent evento) throws IOException {
        panelPrincipal.getChildren().clear();
        Parent fxml = FXMLLoader.load(getClass().getResource("/emaaredespacio/gui/vista/FXMLAdministrarRentas.fxml"));
        panelPrincipal.getChildren().addAll(fxml.getChildrenUnmodifiable());
        barraMenu.setVisible(false);
        menuDesplegado = false;
    }

    @FXML
    private void desplegarRegistrarEgresoFacebook(ActionEvent evento) throws IOException {
        panelPrincipal.getChildren().clear();
        Parent fxml = FXMLLoader.load(getClass().getResource("/emaaredespacio/gui/vista/FXMLRegistrarEgresoFacebook.fxml"));
        panelPrincipal.getChildren().addAll(fxml.getChildrenUnmodifiable());
        barraMenu.setVisible(false);
        menuDesplegado = false;
    }

    @FXML
    private void desplegarEditarEgresoFacebook(ActionEvent evento) throws IOException {
        panelPrincipal.getChildren().clear();
        Parent fxml = FXMLLoader.load(getClass().getResource("/emaaredespacio/gui/vista/FXMLEditarEgresoFacebook.fxml"));
        panelPrincipal.getChildren().addAll(fxml.getChildrenUnmodifiable());
        barraMenu.setVisible(false);
        menuDesplegado = false;
    }

    @FXML
    private void desplegarAdministrarAlumnosDeGrupos(ActionEvent evento) throws IOException {
        panelPrincipal.getChildren().clear();
        Parent fxml = FXMLLoader.load(getClass().getResource("/emaaredespacio/gui/vista/FXMLAdministrarAlumnosDeGrupos.fxml"));
        panelPrincipal.getChildren().addAll(fxml.getChildrenUnmodifiable());
        barraMenu.setVisible(false);
        menuDesplegado = false;
    }

    @FXML
    private void desplegarAdministrarPagosAColaborador(ActionEvent evento) throws IOException {
        panelPrincipal.getChildren().clear();
        Parent fxml = FXMLLoader.load(getClass().getResource("/emaaredespacio/gui/vista/FXMLAdministrarPagosAColaborador.fxml"));
        panelPrincipal.getChildren().addAll(fxml.getChildrenUnmodifiable());
        barraMenu.setVisible(false);
        menuDesplegado = false;
    }

    @FXML
    private void desplegarRegistrarPagoAlumno() throws IOException {
        panelPrincipal.getChildren().clear();
        Parent fxml = FXMLLoader.load(getClass().getResource("/emaaredespacio/gui/vista/FXMLRegistrarPagoDeAlumnos.fxml"));
        panelPrincipal.getChildren().addAll(fxml.getChildrenUnmodifiable());
        barraMenu.setVisible(false);
        menuDesplegado = false;
    }

    @FXML
    private void desplegarVisualizarPagosAlumnos() throws IOException {
        panelPrincipal.getChildren().clear();
        Parent fxml = FXMLLoader.load(getClass().getResource("/emaaredespacio/gui/vista/FXMLVisualizarHistorialDePagosDeAlumno.fxml"));
        panelPrincipal.getChildren().addAll(fxml.getChildrenUnmodifiable());
        barraMenu.setVisible(false);
        menuDesplegado = false;
    }

    @FXML
    private void siguiente(ActionEvent event) {
        for (int i = 0; i < listaAvisos.size(); i++) {
            if (tfNombreAlumno.getText().equals(listaAvisos.get(i).getNombre()) && tfTipoPago.getText().equals(listaAvisos.get(i).getTipoDePago())) {
                if (i < listaAvisos.size() - 1) {
                    tfNombreAlumno.setText(listaAvisos.get(i + 1).getNombre());
                    tfTipoPago.setText(listaAvisos.get(i + 1).getTipoDePago());
                    if (listaAvisos.get(i + 1).getGrupo() == "") {
                        tfGrupo.setVisible(false);
                        lbGrupo.setText("Pago de colaborador");
                    } else {
                        tfGrupo.setText(listaAvisos.get(i + 1).getGrupo());
                        tfGrupo.setVisible(true);
                        lbGrupo.setText("Grupo");
                    }
                    break;
                }
            }
        }
    }

    @FXML
    private void anterior(ActionEvent event) {
        for (int i = 0; i < listaAvisos.size(); i++) {
            if (tfNombreAlumno.getText().equals(listaAvisos.get(i).getNombre()) && tfTipoPago.getText().equals(listaAvisos.get(i).getTipoDePago())) {
                if (i > 0) {
                    tfNombreAlumno.setText(listaAvisos.get(i - 1).getNombre());
                    tfTipoPago.setText(listaAvisos.get(i - 1).getTipoDePago());
                    if (listaAvisos.get(i - 1).getGrupo() == "") {
                        tfGrupo.setVisible(false);
                        lbGrupo.setText("Pago de colaborador");
                    } else {
                        tfGrupo.setText(listaAvisos.get(i - 1).getGrupo());
                        tfGrupo.setVisible(true);
                        lbGrupo.setText("Grupo");
                    }
                    break;
                }
            }
        }
    }

    @FXML
    private void mostrarAvisos(ActionEvent event) {
        avisosDesplegados = !avisosDesplegados;
        anchorPaneAviso.setVisible(avisosDesplegados);
    }

    @FXML
    private void desplegarAdministrarEgresos() throws IOException {
        panelPrincipal.getChildren().clear();
        Parent fxml = FXMLLoader.load(getClass().getResource("/emaaredespacio/gui/vista/FXMLAdministrarEgresos.fxml"));
        panelPrincipal.getChildren().addAll(fxml.getChildrenUnmodifiable());
        barraMenu.setVisible(false);
        menuDesplegado = false;
    }

    @FXML
    private void desplegarRegistrarPagosRenta() throws IOException {
        panelPrincipal.getChildren().clear();
        Parent fxml = FXMLLoader.load(getClass().getResource("/emaaredespacio/gui/vista/FXMLRegistrarPagosRenta.fxml"));
        panelPrincipal.getChildren().addAll(fxml.getChildrenUnmodifiable());
        barraMenu.setVisible(false);
        menuDesplegado = false;
    }

    @FXML
    private void desplegarGenerarRepote() throws IOException {
        panelPrincipal.getChildren().clear();
        Parent fxml = FXMLLoader.load(getClass().getResource("/emaaredespacio/gui/vista/FXMLGenerarReporte.fxml"));
        panelPrincipal.getChildren().addAll(fxml.getChildrenUnmodifiable());
        barraMenu.setVisible(false);
        menuDesplegado = false;
    }

    @FXML
    private void desplegarTomarAsistencia() throws IOException {
        panelPrincipal.getChildren().clear();
        Parent fxml = FXMLLoader.load(getClass().getResource("/emaaredespacio/gui/vista/FXMLTomarAsistencia.fxml"));
        panelPrincipal.getChildren().addAll(fxml.getChildrenUnmodifiable());
        barraMenu.setVisible(false);
        menuDesplegado = false;
    }

    @FXML
    private void desplegarHistorialPagoDeEspacio(ActionEvent event) throws IOException {
        panelPrincipal.getChildren().clear();
        Parent fxml = FXMLLoader.load(getClass().getResource("/emaaredespacio/gui/vista/FXMLVisualizarHistorialDePagoDeEspacio.fxml"));
        panelPrincipal.getChildren().addAll(fxml.getChildrenUnmodifiable());
        barraMenu.setVisible(false);
        menuDesplegado = false;
    }
}
