package com.caju.cjauthorization.domain.model

import jakarta.persistence.*
import java.math.BigDecimal
import java.util.*

enum class BalanceType(val type: String) {
    FOOD("FOOD"),
    MEAL("MEAL"),
    CASH("CASH")
}


@Entity
data class Balance(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: UUID = UUID.randomUUID(),
    val accountId: String,
    @Enumerated(EnumType.STRING)
    val type: BalanceType,
    var amount: BigDecimal
) {
    constructor() : this(UUID.randomUUID(), "", BalanceType.CASH, BigDecimal.ZERO)
}