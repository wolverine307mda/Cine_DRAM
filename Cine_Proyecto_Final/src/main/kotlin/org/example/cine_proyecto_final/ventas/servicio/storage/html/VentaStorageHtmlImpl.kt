package org.example.cine_proyecto_final.ventas.servicio.storage.html

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import org.example.cine_proyecto_final.butacas.models.Butaca
import org.example.cine_proyecto_final.database.logger
import org.example.cine_proyecto_final.ventas.errors.VentaError
import org.example.cine_proyecto_final.ventas.models.Venta
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths

/**
 * Implementación del almacenamiento de ventas desde y hacia archivos HTML.
 */
class VentaStorageHtmlImpl : VentaStorageHtml {

    init {
        Files.createDirectories(Paths.get("data","ventas"))
    }

    /**
     * Carga productos desde un archivo CSV.
     * @param venta la venta que quieres guardar.
     * @return Un resultado que contiene un Unit si lo pudo hacer bien o un error si hubo algún problema durante el procesoa.
     */
    override fun export(venta : Venta, file : File) : Result<Unit, VentaError>{
        try {
            val output = """
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Recibo</title>
    <style>
      body {
        font-family: Arial, sans-serif;
        margin: 0;
        padding: 20px;
        display: flex;
        justify-content: center;
        align-items: center;
        height: 100vh;
      }
      .receipt {
        max-width: 600px;
        border: 1px solid #ccc;
        padding: 20px;
        border-radius: 5px;
        background-color: #f9f9f9;
      }
      .receipt h2 {
        text-align: center;
      }
      .receipt p {
        margin: 5px 0;
      }
    </style>
  </head>
  <body>
    <div class="receipt">
      <h2>Recibo</h2>
      <p><strong>Cliente:</strong> ${venta.cliente.email}</p>
      <ul>
           ${getButacaList(venta.butacas)}
      </ul>
      <p><strong>Complementos:</strong></p>
      <ul>
           ${getProductList(venta)}
      </ul>
      <p><strong>Precio Total:</strong> ${getPrice(venta)}€</p>
    </div>
  </body>
</html>
"""
            val date = venta.updatedAt
            file.writeText(output)
            return Ok(Unit)
        }catch (e : Exception){
            logger.error { "Hubo un fallo al generar su recibo" }
            return Err(VentaError.VentaStorageError("Hubo un fallo al generar su recibo de la venta con id: ${venta.id}"))
        }
    }

    private fun getPrice(venta: Venta): String {
        var price = 0.0
        venta.lineasVenta.forEach {
            price += (it.precio * it.cantidad)
        }
        venta.butacas.forEach {
            price += it.precio
        }
        return price.toString()
    }

    private fun getProductList(venta: Venta) : String{
        var output = ""
        venta.lineasVenta.forEach {
            output += "      <li>${it.producto.nombre} \$ ${it.producto.precio} x ${it.cantidad} </li>"
        }
        if (output.isBlank()) return "<li>No se compraron complementos</li>"
        else return output
    }

    private fun getButacaList(butacas : List<Butaca>) : String{
        var output = ""
        butacas.forEach {
            output += "      <li>${it.id}</li>"
        }
        return output
    }

}