package com.caju.cjauthorization.infrastructure.repository

import com.caju.cjauthorization.domain.model.Transaction
import com.caju.cjauthorization.domain.port.TransactionRepository
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface TransactionRepositoryImpl : JpaRepository<Transaction, UUID>, TransactionRepository {
    override fun findByAccountId(accountId: String): List<Transaction>
}