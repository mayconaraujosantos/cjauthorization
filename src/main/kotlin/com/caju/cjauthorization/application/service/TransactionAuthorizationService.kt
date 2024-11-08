package com.caju.cjauthorization.application.service

import com.caju.cjauthorization.domain.model.Balance
import com.caju.cjauthorization.domain.model.BalanceType
import com.caju.cjauthorization.domain.model.Transaction
import com.caju.cjauthorization.domain.port.BalanceRepository
import com.caju.cjauthorization.domain.port.TransactionRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.math.BigDecimal


private const val ERRO_AO_PROCESSAR_TRANSACAO = "Erro ao processar transação"

@Service
class TransactionAuthorizationService(
    private val balanceRepository: BalanceRepository,
    private val transactionRepository: TransactionRepository
) {
    private val logger = LoggerFactory.getLogger(TransactionAuthorizationService::class.java)

    fun authorizeTransaction(transaction: Transaction): Map<String, String> {
        return try {
            if (transaction.amount <= BigDecimal.ZERO) {
                logger.warn("Valor inválido para a transação: ${transaction.amount}. A transação será rejeitada.")
                // Retorna erro com código "07", já que o erro é inesperado.
                return mapOf("code" to "07", "message" to ERRO_AO_PROCESSAR_TRANSACAO)
            }

            val (code, message) = when (val saldo = getBalanceForTransaction(transaction)) {
                null -> {
                    logger.error("Saldo não encontrado para a transação, retornando erro 07")
                    "07" to ERRO_AO_PROCESSAR_TRANSACAO
                }

                else -> {
                    logger.info("Saldo encontrado para a categoria ${saldo.type}: ${saldo.amount}")
                    if (saldo.amount >= transaction.amount) {
                        logger.info("Saldo suficiente. Aprovando transação e atualizando saldo.")
                        saldo.amount -= transaction.amount
                        balanceRepository.save(saldo) // Salva o saldo atualizado
                        transactionRepository.save(transaction) // Salva a transação
                        "00" to "Transação Aprovada"
                    } else {
                        logger.warn("Saldo insuficiente para a transação.")
                        "51" to "Saldo Insuficiente"
                    }
                }
            }
            mapOf("code" to code, "message" to message)
        } catch (e: Exception) {
            logger.error("Erro inesperado ao processar a transação: ${e.message}", e)
            mapOf("code" to "07", "message" to ERRO_AO_PROCESSAR_TRANSACAO)
        }
    }

    private fun getBalanceForTransaction(transaction: Transaction): Balance? {
        // Resolva o MCC para a categoria
        val mcc = resolveMCC(transaction.merchant, transaction.mcc)
        logger.info("MCC resolvido para o comerciante '${transaction.merchant}': $mcc")

        val balanceType = when (mcc) {
            "5411", "5412" -> BalanceType.FOOD
            "5811", "5812" -> BalanceType.MEAL
            else -> BalanceType.CASH
        }

        // Verifica se o saldo da categoria foi encontrado
        var balance = balanceRepository.findByAccountIdAndType(transaction.accountId, balanceType)
        if (balance == null) {
            logger.info("Saldo da categoria $balanceType não encontrado, tentando saldo de CASH.")
            balance = balanceRepository.findByAccountIdAndType(transaction.accountId, BalanceType.CASH)
        }

        return balance
    }

    private fun resolveMCC(merchant: String, mcc: String): String {
        val merchantMccMap = mapOf(
            "UBER EATS" to "5411",
            "UBER TRIP" to "4121",
            "PAG*JoseDaSilva" to "6011",
            "PICPAY*BILHETEUNICO" to "4111"
        )
        for ((merchantPattern, mccValue) in merchantMccMap) {
            if (merchant.contains(merchantPattern, ignoreCase = true)) {
                return mccValue
            }
        }
        return mcc
    }
}