package com.caju.cjauthorization.domain.model

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import java.math.BigDecimal
import java.util.*

@Entity
data class Transaction(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: UUID = UUID.randomUUID(),
    val accountId: String,
    val amount: BigDecimal,
    val mcc: String,
    val merchant: String
) {
    constructor() : this(UUID.randomUUID(), "", BigDecimal.ZERO, "", "")
}