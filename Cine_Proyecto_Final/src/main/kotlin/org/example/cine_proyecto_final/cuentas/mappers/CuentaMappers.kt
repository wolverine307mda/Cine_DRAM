package org.example.cine_proyecto_final.cuentas.mappers

import database.CuentaEntity
import org.example.cine_proyecto_final.cuentas.dto.CuentaDto
import org.example.cine_proyecto_final.cuentas.models.Cuenta
import org.example.cine_proyecto_final.cuentas.models.TipoCuenta
import java.time.LocalDateTime

/**
 * Convierte un objeto [CuentaDTO] en un objeto [Cuenta].
 * @return el objeto [Cuenta] resultante.
 */
fun CuentaDto.toCuenta() : Cuenta {
    return Cuenta(
        email = this.email,
        password =  this.password,
        apellido = this.apellido,
        tipo = chooseTypeCuenta(this.tipo),
        updatedAt = LocalDateTime.parse(this.updatedAt),
        createdAt = LocalDateTime.parse(this.createdAt),
        nombre = this.nombre,
        imagen = this.imagen
    )
}

/**
 * Convierte un objeto [Cuenta] en un objeto [CuentaDTO].
 * @return el objeto [CuentaDTO] resultante.
 */
fun Cuenta.toDto() : CuentaDto {
    return CuentaDto(
        email = this.email,
        createdAt = this.createdAt.toString(),
        updatedAt = this.updatedAt.toString(),
        apellido = this.apellido,
        password =  this.password,
        tipo = this.tipo!!.name,
        nombre = this.nombre,
        imagen = this.imagen
    )
}

/**
 * Elige el tipo de cuenta dependiendo del string que se le da o un null si no existe
 * @param input el tipo de cuenta
 * @return el tipo de cuenta que corresponde a la string dada
 */
fun chooseTypeCuenta(input: String) : TipoCuenta?{
    return when(input) {
        "ADMIN" -> TipoCuenta.ADMINISTRADOR
        "USUARIO" -> TipoCuenta.USUARIO
        else -> null
    }
}


/**
 * Convierte un objeto [CuentaEntity] en un objeto [Cuenta].
 * @return el objeto [Cuenta] resultante.
 */
fun CuentaEntity.toCuenta(): Cuenta{
    return Cuenta(
        email = this.email,
        nombre = this.nombre,
        apellido = this.apellido,
        imagen = this.imagen,
        password =  this.password,
        tipo = chooseTypeCuenta(this.tipo),
        createdAt = LocalDateTime.parse(this.createdAt),
        updatedAt = LocalDateTime.parse(this.updatedAt)
    )
}

/**
 * Convierte un valor booleano en un valor Long.
 * @return 1 si el valor booleano es verdadero, 0 si es falso.
 */
fun Boolean.toLong(): Long {
    if (this) return 1
    else return 0
}

