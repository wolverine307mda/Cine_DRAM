package org.example.cine_proyecto_final.controllers.general

import javafx.fxml.FXML
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.control.TextField
import javafx.scene.text.Text
import javafx.stage.FileChooser
import org.example.cine_proyecto_final.routes.RoutesManager
import org.koin.core.component.KoinComponent
import org.lighthousegames.logging.logging

private val logger = logging()

class GeneralComprarEntradaController : KoinComponent {

    @FXML
    private lateinit var duracionLabel: Label

    @FXML
    private lateinit var directorLabel: Label

    @FXML
    private lateinit var actoresLabel: Label

    @FXML
    private lateinit var sinopsisText: Text

    @FXML
    private lateinit var nombreTextField: TextField

    @FXML
    private lateinit var devolver_entrada_button: Button

    @FXML
    private lateinit var iniciar_sesion_button: Button

    @FXML
    private lateinit var comprar_entrada_button: Button

    @FXML
    private lateinit var atras_button: Button

    @FXML
    private fun initialize() {
        logger.debug { "iniciando pantalla general de comprar entrada" }

        devolver_entrada_button.setOnAction { devolverEntradaAction() }
        iniciar_sesion_button.setOnAction { RoutesManager.initSesionInicio() }
        comprar_entrada_button.setOnAction { RoutesManager.changeScene(RoutesManager.View.SELECCION_BUTACAS) }
        atras_button.setOnAction { RoutesManager.changeScene(RoutesManager.View.MAIN) }

        configurarDatosPelicula()
    }

    private fun configurarDatosPelicula() {
        duracionLabel.text = "DURACION"
        directorLabel.text = "DIRECTOR/ES"
        actoresLabel.text = "ACTORES"
        sinopsisText.text = "Lobezno se recupera de sus heridas cuando se cruza con el bocazas, Deadpool, que ha viajado en el tiempo para curarlo con la esperanza de hacerse amigos y formar un equipo para acabar con un enemigo común."
    }

    private fun devolverEntradaAction() {
        logger.debug { "selecciona el archivo para devolver entrada" }
        FileChooser().run {
            title = "Selecciona la entrada"
            extensionFilters.addAll(FileChooser.ExtensionFilter("HTML","*.html"))
            showOpenDialog(RoutesManager.activeStage)
        }?.let {
        }
    }
}