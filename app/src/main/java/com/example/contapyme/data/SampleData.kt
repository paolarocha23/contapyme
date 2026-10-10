package com.example.contapyme.data

data class Cliente(
    val id: String,
    val iniciales: String,
    val nombre: String,
    val ciudad: String,
    val estado: String,
    val telefono: String,
    val correo: String,
    val adeudo: Double,
    val activo: Boolean
)

data class VentaItem(
    val producto: String,
    val codigo: String,
    val cantidad: Int,
    val precioUnitario: Double,
    val disponibilidad: Int? = null,
    val stockBajo: Boolean = false,
    val agotado: Boolean = false
)

data class Venta(
    val codigo: String,
    val fecha: String,
    val vendedor: String,
    val cliente: String,
    val clienteId: String,
    val numProductos: Int,
    val total: Double,
    val saldoPendiente: Double,
    val estado: String,
    val productos: List<VentaItem> = emptyList(),
    val formaPago: String = "",
    val condicion: String = "",
    val vence: String = ""
)

data class Producto(
    val nombre: String,
    val codigo: String,
    val categoria: String,
    val precio: Double,
    val existencia: Int,
    val minimo: Int
)

data class Alerta(
    val id: String,
    val fecha: String,
    val referencia: String,
    val tipo: String,
    val titulo: String,
    val descripcion: String,
    val monto: Double?,
    val badge: String,
    val grupo: String,
    val hora: String
)

data class Cobro(
    val referencia: String,
    val fecha: String,
    val forma: String,
    val monto: Double,
    val estado: String
)

object SampleData {

    val clienteDetalle = Cliente(
        id = "C-0004",
        iniciales = "MF",
        nombre = "María Fernanda López Soto",
        ciudad = "León",
        estado = "Guanajuato",
        telefono = "477 598 2210",
        correo = "mafer.lopez@correo.mx",
        adeudo = 10496.0,
        activo = true
    )

    val clientes = listOf(
        Cliente("C-0001", "CH", "Carlos Hernández Ruiz", "León, Guanajuato", "Guanajuato", "477 712 3045", "carlos.hernandez@correo.mx", 0.0, true),
        Cliente("C-0004", "ML", "María Fernanda López Soto", "León, Guanajuato", "Guanajuato", "477 598 2210", "mafer.lopez@correo.mx", 10496.0, true),
        Cliente("C-0007", "JR", "Jorge Ramírez Díaz", "Silao, Guanajuato", "Guanajuato", "472 101 8873", "jorge.ramirez@correo.mx", 3250.0, true),
        Cliente("C-0009", "AG", "Ana Patricia Gómez Torres", "Irapuato, Guanajuato", "Guanajuato", "462 330 4412", "ana.gomez@correo.mx", 0.0, true),
        Cliente("C-0012", "LO", "Luis Ortega Navarro", "León, Guanajuato", "Guanajuato", "477 845 6120", "luis.ortega@correo.mx", 0.0, false)
    )

    val ventaDetalle = Venta(
        codigo = "V-0245",
        fecha = "9 oct 2026",
        vendedor = "Laura M.",
        cliente = "María Fernanda López Soto",
        clienteId = "C-0004",
        numProductos = 3,
        total = 16496.0,
        saldoPendiente = 10496.0,
        estado = "Pago parcial",
        formaPago = "Efectivo",
        condicion = "Crédito",
        vence = "30 oct 2026",
        productos = listOf(
            VentaItem("Laptop Lenovo IdeaPad 3 15\"", "LEN-IP3", 1, 9499.0, disponibilidad = 8),
            VentaItem("Monitor Samsung 24\" Full HD", "SAM-M24", 2, 2899.0, disponibilidad = 12),
            VentaItem("Audífonos Sony WH-CH520", "SNY-S20", 1, 1199.0, disponibilidad = 3, stockBajo = true)
        )
    )

    val ventas = listOf(
        ventaDetalle,
        Venta("V-0244", "9 oct 2026", "Laura M.", "Carlos Hernández Ruiz", "C-0001", 1, 2899.0, 0.0, "Pagada"),
        Venta("V-0243", "8 oct 2026", "Diego R.", "Público en general", "C-0001", 1, 549.0, 0.0, "Pagada"),
        Venta("V-0241", "7 oct 2026", "Diego R.", "Jorge Ramírez Díaz", "C-0007", 2, 3250.0, 3250.0, "Pendiente"),
        Venta("V-0238", "6 oct 2026", "Laura M.", "Ana Patricia Gómez Torres", "C-0009", 1, 1199.0, 0.0, "Cancelada")
    )

    val productos = listOf(
        Producto("Laptop Lenovo IdeaPad 3 15\"", "LEN-IP3", "Cómputo", 9499.0, 8, 3),
        Producto("Monitor Samsung 24\" Full HD", "SAM-M24", "Monitores", 2899.0, 12, 4),
        Producto("Audífonos Sony WH-CH520", "SNY-S20", "Audio", 1199.0, 3, 5),
        Producto("Teclado Logitech K120", "LOG-K120", "Periféricos", 299.0, 25, 10),
        Producto("Memoria USB 64 GB", "USB-64", "Almacenamiento", 159.0, 40, 15),
        Producto("SSD Kingston 480 GB", "KNG-480", "Almacenamiento", 749.0, 3, 8),
        Producto("Cable HDMI 2 m", "HDMI-2M", "Cables", 129.0, 4, 10),
        Producto("Mouse Logitech M185", "LOG-M185", "Periféricos", 249.0, 0, 5),
        Producto("Cargador USB-C 65 W", "CHG-65W", "Accesorios", 549.0, 0, 4)
    )

    val alertas = listOf(
        Alerta("N-0091", "9 oct 2026", "KNG-480", "stock_bajo", "Stock bajo", "SSD Kingston 480 GB tiene 3 pz y su mínimo es 8. Ref. KNG-480", null, "Stock bajo", "Hoy", "9:15"),
        Alerta("N-0090", "9 oct 2026", "LOG-M185", "agotado", "Producto agotado", "Mouse Logitech M185 llegó a 0 pz. Ref. LOG-M185", null, "Agotado", "Hoy", "8:40"),
        Alerta("N-0089", "9 oct 2026", "V-0231", "por_cobrar_vencida", "Cuenta por cobrar vencida", "Ana Patricia Gómez Torres · $12,400.00 venció hace 5 días. Ref. V-0231", 12400.0, "Vencida", "Hoy", "8:00"),
        Alerta("N-0087", "9 oct 2026", "V-0241", "por_cobrar_vencer", "Cuenta por cobrar por vencer", "Jorge Ramírez Díaz · $3,250.00 vence en 2 días. Ref. V-0241", 3250.0, "Por vencer", "Hoy", "8:00"),
        Alerta("N-0086", "8 oct 2026", "C-0118", "por_pagar_vencida", "Cuenta por pagar vencida", "Tecnomayoreo S.A. · $18,600.00 venció hace 2 días. Ref. C-0118", 18600.0, "Vencida", "Ayer", "8:00"),
        Alerta("N-0085", "8 oct 2026", "C-0121", "por_pagar_vencer", "Cuenta por pagar por vencer", "Distribuidora Digital · $7,350.00 vence en 4 días. Ref. C-0121", 7350.0, "Por vencer", "Ayer", "8:00"),
        Alerta("N-0084", "8 oct 2026", "HDMI-2M", "stock_bajo", "Stock bajo", "Cable HDMI 2 m tiene 4 pz y su mínimo es 10. Ref. HDMI-2M", null, "Stock bajo", "Ayer", "9:10")
    )

    val historialVentasCliente = listOf(
        Triple("V-0245", "9 oct 2026 · Crédito", 16496.0 to "Pago parcial"),
        Triple("V-0198", "21 sep 2026 · Contado", 2899.0 to "Pagada"),
        Triple("V-0173", "2 sep 2026 · Crédito", 9499.0 to "Pagada")
    )

    val historialCobrosCliente = listOf(
        Cobro("V-0245", "9 oct 2026", "Efectivo", 6000.0, "Abono"),
        Cobro("V-0173", "18 sep 2026", "Tarjeta", 9499.0, "Liquidado")
    )

    val ventasDia = listOf(
        "Sáb" to 8200.0, "Dom" to 5100.0, "Lun" to 9800.0, "Mar" to 11400.0,
        "Mié" to 7600.0, "Jue" to 10500.0, "Hoy" to 13420.0
    )
}
