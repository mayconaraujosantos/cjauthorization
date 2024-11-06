package com.caju.cjauthorization.infrastructure.api.request

import java.math.BigDecimal

data class TransactionRequest(
    val accountId: String,
    val amount: BigDecimal,
    val mcc: String,
    val merchant: String
)