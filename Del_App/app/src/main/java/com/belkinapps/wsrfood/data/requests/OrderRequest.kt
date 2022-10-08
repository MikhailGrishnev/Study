package com.belkinapps.wsrfood.data.requests

data class OrderRequest(
    val address: String?,
    val date: String,
    val dishes: MutableList<DishesOrder>
)

data class DishesOrder(
    val dishId: String,
    var count: Int
)
