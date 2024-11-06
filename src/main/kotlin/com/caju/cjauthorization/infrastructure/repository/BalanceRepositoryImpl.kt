package com.caju.cjauthorization.infrastructure.repository

import com.caju.cjauthorization.domain.model.Balance
import com.caju.cjauthorization.domain.model.BalanceType
import com.caju.cjauthorization.domain.port.BalanceRepository
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface BalanceRepositoryImpl : JpaRepository<Balance, UUID>, BalanceRepository {
    override fun findByAccountIdAndType(accountId: String, type: BalanceType): Balance?
}