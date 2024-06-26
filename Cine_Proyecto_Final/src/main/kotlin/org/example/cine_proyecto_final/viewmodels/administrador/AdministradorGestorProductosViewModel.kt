package org.example.cine_proyecto_final.viewmodels.administrador

import com.github.michaelbull.result.Result
import com.github.michaelbull.result.onFailure
import com.github.michaelbull.result.onSuccess
import javafx.beans.property.SimpleObjectProperty
import javafx.scene.image.Image
import org.example.cine_proyecto_final.CineApplication
import org.example.cine_proyecto_final.productos.models.Producto
import org.example.cine_proyecto_final.productos.servicio.database.ProductoServicio
import org.example.cine_proyecto_final.productos.validador.ProductoValidator
import org.example.productos.errors.ProductoError
import org.jetbrains.dokka.InternalDokkaApi
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.lighthousegames.logging.logging

private val logger = logging()

@OptIn(InternalDokkaApi::class)
class AdministradorGestorProductosViewModel : KoinComponent {
    // Inyección de dependencias para ProductoServicio y ProductoStorageCSV
    private val productoService: ProductoServicio by inject()
    private val validador: ProductoValidator by inject()

    // Propiedad de estado que contiene el estado actual de los productos
    val state: SimpleObjectProperty<ProductSelectionState> = SimpleObjectProperty(ProductSelectionState())

    init {
        logger.debug { "Inicializando AdministradorGestorProductosViewModel" }

        // Cargar productos desde un archivo CSV y guardarlos en la base de datos
        state.value.productos = productoService.findAll().value
        state.value.allProductos = productoService.findAll().value
    }

    fun nuevoProducto(producto: Producto): Result<Producto, ProductoError> {
        logger.debug { "Añadiendo Producto" }

        return validador.validate(producto)
            .onSuccess {
                productoService.save(producto)
                logger.debug { "Producto creado con éxito" }
                val updatedProductos = state.value.productos + producto
                state.set(state.value.copy(productos = updatedProductos))
            }
            .onFailure {
                logger.debug { "Error al crear producto: $it" }
            }
    }

    fun eliminarProducto(producto: Producto): Result<Unit, ProductoError> {
        logger.debug { "Eliminando Producto" }

        return productoService.delete(producto.id)
            .onSuccess {
                logger.debug { "Producto eliminado con éxito" }
                val updatedProductos = state.value.productos.filterNot { it.id == producto.id }
                state.set(state.value.copy(productos = updatedProductos))
            }
            .onFailure {
                logger.debug { "Error al eliminar producto: $it" }
            }
    }

    fun editarProducto(producto: Producto): Result<Producto, ProductoError> {
        logger.debug { "Editando Producto" }
        return productoService.update(producto.id, producto)
            .onSuccess {
                logger.debug { "Producto editado con éxito" }
                val updatedProductos = state.value.productos.map {
                    if (it.id == producto.id) producto else it
                }
                state.set(state.value.copy(productos = updatedProductos))
            }
            .onFailure {
                logger.debug { "Error al editar producto: $it" }
            }
    }


    /**
     * El objeto observable que contiene las lineas y los productos
     */
    data class ProductSelectionState(
        var allProductos: List<Producto> = emptyList(),
        var productos: List<Producto> = emptyList(),
        var currentImage: Image = Image(CineApplication::class.java.getResourceAsStream("images/NoImage.png")),
        var currentProduct : Producto =
        Producto(
            id = "",
            nombre = "",
            precio = 0.0,
            stock = 0,
            isDeleted = false,
            image = "",
            tipo = null
        )
    )
}
