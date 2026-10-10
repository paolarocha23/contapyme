package com.example.contapyme.navigation

object Routes {
    const val LOGIN = "login"
    const val DASHBOARD = "dashboard"
    const val REGISTRAR_VENTA = "registrar_venta"
    const val REGISTRAR_GASTO = "registrar_gasto"
    const val CLIENTES = "clientes"
    const val AGREGAR_CLIENTE = "agregar_cliente"
    const val DETALLE_CLIENTE = "detalle_cliente"
    const val VENTAS = "ventas"
    const val DETALLE_VENTA = "detalle_venta"
    const val INVENTARIO = "inventario"
    const val NOTIFICACIONES = "notificaciones"
    const val DETALLE_ALERTA = "detalle_alerta"

    fun detalleCliente(id: String) = "$DETALLE_CLIENTE/$id"
    fun detalleVenta(codigo: String) = "$DETALLE_VENTA/$codigo"
    fun detalleAlerta(id: String) = "$DETALLE_ALERTA/$id"
}
