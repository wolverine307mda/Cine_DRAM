package org.example.cine_proyecto_final.viewmodels.cliente

import com.github.michaelbull.result.onSuccess
import org.example.cine_final.productos.servicio.storage.ProductoStorage
import org.example.cine_proyecto_final.productos.models.Producto
import org.example.cine_proyecto_final.productos.models.TipoProducto
import org.example.cine_proyecto_final.productos.servicio.database.ProductoServicio
import org.example.cine_proyecto_final.ventas.models.LineaVenta
import org.lighthousegames.logging.logging
import javafx.beans.property.SimpleObjectProperty
import org.example.cine_proyecto_final.CineApplication
import org.jetbrains.dokka.InternalDokkaApi
import org.jetbrains.dokka.utilities.ServiceLocator.toFile
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

private val logger = logging()

/**
 * ViewModel para gestionar la selección de productos por parte del cliente.
 */
@OptIn(InternalDokkaApi::class)
class ClienteSeleccionProductosViewModel : KoinComponent {

    // Propiedad de estado que contiene el estado actual de la selección de productos
    val state: SimpleObjectProperty<ProductSelectionState> = SimpleObjectProperty(ProductSelectionState())
    val productoServicio : ProductoServicio by inject()

    /*
     * Inicializamos los datos de los productos con un fichero que deberia estar en el producto
     */
    init {
        logger.debug { "Inicializando ClienteSeleccionProductosViewModel" }
        productoServicio.findAll().onSuccess {
            state.value.allProductos = it
            state.value.productos = it
        }
    }

    /**
     * Añade una línea de venta a la lista de líneas de venta
     * @param producto el producto que formará parte de la línea de venta
     */
    fun addLinea (producto: Producto) {
        state.value= state.value.copy(
            lineas = state.value.lineas.plus(
                LineaVenta(
                    producto = producto,
                    precio = producto.precio,
                    cantidad = 1
                )
            )
        )
    }

    /**
     * Borra todas las lineas de venta
     */
    fun clearList() {
        state.value = state.value.copy(
            lineas = emptyList()
        )
    }

    /**
     * Elimina un elemento específico de la lista de elementos seleccionados.
     *
     * @param linea El elemento a eliminar
     */
    fun removeLinea(linea: LineaVenta) {
        val index = state.value.lineas.indexOf(linea)
        if (index != -1) {
            state.value = state.value.copy(
                lineas = state.value.lineas.filter { it.id != linea.id }
            )
        }
    }

    /**
     * Filtra la lista de productos por tipo de producto
     * @param type el tipo de producto a filtrar
     */
    fun filterListByType(type: TipoProducto) {
        state.value = state.value.copy (
            productos = state.value.allProductos.filter { it.tipo == type }
        )
    }

    /**
     * Actualiza la cantidad de un producto de una linea de venta
     * @param linea la linea de venta que ya tiene la cantidad correcta
     */
    fun updateItem(linea: LineaVenta) {
        val lineas = state.value.lineas
        lineas.forEach {
            if (it.id == linea.id) it.cantidad = linea.cantidad
        }
        state.value = state.value.copy ( //Para que se actualice
            lineas = emptyList()
        )
        state.value = state.value.copy (
            lineas = lineas
        )
        state.value.lineas.forEach {
            logger.debug { it.cantidad }
        }
    }

    /**
     * Actualiza la lista para enseñar todos los productos
     */
    fun showAllProducts() {
        state.value = state.value.copy(
            productos = state.value.allProductos,
        )
    }

    /**
     * Actualiza la lista de productos
     */
    fun updateProducts(){
        productoServicio.findAll().onSuccess {
            state.value.allProductos = it.filter { it.stock <= 0 || !it.isDeleted }
            state.value.productos = it.filter { it.stock <= 0 || !it.isDeleted }
        }
    }

    /**
     * El objeto observable que contiene las lineas y los productos
     */
    data class ProductSelectionState(
        var allProductos: List<Producto> = emptyList(),
        var productos: List<Producto> = emptyList(),
        var lineas: List<LineaVenta> = mutableListOf(),
    )
}
