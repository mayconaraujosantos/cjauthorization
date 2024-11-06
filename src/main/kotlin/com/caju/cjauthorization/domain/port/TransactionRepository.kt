package com.caju.cjauthorization.domain.port

import com.caju.cjauthorization.domain.model.Transaction

interface TransactionRepository {
    fun save(transaction: Transaction): Transaction
    fun findByAccountId(accountId: String): List<Transaction>
}