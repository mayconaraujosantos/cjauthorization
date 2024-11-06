package com.caju.cjauthorization.domain.port

import com.caju.cjauthorization.domain.model.Balance
import com.caju.cjauthorization.domain.model.BalanceType
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface BalanceRepository : JpaRepository<Balance, UUID> {
    fun findByAccountIdAndType(accountId: String, type: BalanceType): Balance?
}