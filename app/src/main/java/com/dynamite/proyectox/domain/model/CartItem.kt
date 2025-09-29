package com.dynamite.proyectox.domain.model

import java.math.BigDecimal
// Podrías necesitar importar Product si no está en el mismo paquete exacto,
// pero asumiré que es accesible.

data class CartItem(
    val product: Product,
    var quantity: Int
) {
    // Propiedad calculada para el subtotal de este ítem del carrito
    val subtotal: BigDecimal
        get() = product.price * quantity.toBigDecimal()
}
