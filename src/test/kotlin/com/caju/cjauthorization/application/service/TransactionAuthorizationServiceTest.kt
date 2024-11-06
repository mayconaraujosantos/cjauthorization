package com.caju.cjauthorization.application.service


import com.caju.cjauthorization.domain.model.Balance
import com.caju.cjauthorization.domain.model.BalanceType
import com.caju.cjauthorization.domain.model.Transaction
import com.caju.cjauthorization.domain.port.BalanceRepository
import com.caju.cjauthorization.domain.port.TransactionRepository
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.BeforeEach
import java.math.BigDecimal
import kotlin.test.Test
import kotlin.test.assertEquals


data class AuthorizationResult(val code: String, val message: String)


class TransactionAuthorizationServiceTest {

    private lateinit var balanceRepository: BalanceRepository
    private lateinit var transactionRepository: TransactionRepository
    private lateinit var transactionAuthorizationService: TransactionAuthorizationService

    @BeforeEach
    fun setup() {
        balanceRepository = mockk()
        transactionRepository = mockk()
        transactionAuthorizationService = TransactionAuthorizationService(balanceRepository, transactionRepository)
    }

    @Test
    fun `Deve aprovar transacao com saldo suficiente na categoria refeicao`() {
        val transaction =
            Transaction(accountId = "1", amount = BigDecimal("50.00"), mcc = "5812", merchant = "Restaurante")
        val balance = Balance(accountId = "1", type = BalanceType.MEAL, amount = BigDecimal("100.00"))

        every { balanceRepository.findByAccountIdAndType("1", BalanceType.MEAL) } returns balance
        every { balanceRepository.save(balance) } returns balance
        every { transactionRepository.save(transaction) } returns transaction

        val result = transactionAuthorizationService.authorizeTransaction(transaction)

        assertEquals("00", result["code"])
        assertEquals("Transação Aprovada", result["message"])
        assertEquals(BigDecimal("50.00"), balance.amount)
    }

    @Test
    fun `Deve aprovar transacao com saldo suficiente na categoria dinheiro apos fallback`() {
        val transaction = Transaction(accountId = "1", amount = BigDecimal("50.00"), mcc = "1234", merchant = "Loja")
        val balance = Balance(accountId = "1", type = BalanceType.CASH, amount = BigDecimal("100.00"))

        every { balanceRepository.findByAccountIdAndType("1", BalanceType.CASH) } returns balance
        every { balanceRepository.save(balance) } returns balance
        every { transactionRepository.save(transaction) } returns transaction

        val result = transactionAuthorizationService.authorizeTransaction(transaction)

        assertEquals("00", result["code"])
        assertEquals("Transação Aprovada", result["message"])
        assertEquals(BigDecimal("50.00"), balance.amount)
    }

    @Test
    fun `Deve rejeitar transacao quando o saldo for insuficiente`() {
        val transaction =
            Transaction(accountId = "1", amount = BigDecimal("150.00"), mcc = "5812", merchant = "Restaurante")
        val balance = Balance(accountId = "1", type = BalanceType.MEAL, amount = BigDecimal("100.00"))

        every { balanceRepository.findByAccountIdAndType("1", BalanceType.MEAL) } returns balance

        val result = transactionAuthorizationService.authorizeTransaction(transaction)

        assertEquals("51", result["code"])
        assertEquals("Saldo Insuficiente", result["message"])
    }

    @Test
    fun `Deve determinar a categoria com base no comerciante`() {
        val transaction =
            Transaction(accountId = "1", amount = BigDecimal("50.00"), mcc = "1234", merchant = "UBER EATS")
        val balance = Balance(accountId = "1", type = BalanceType.FOOD, amount = BigDecimal("100.00"))

        every { balanceRepository.findByAccountIdAndType("1", BalanceType.FOOD) } returns balance
        every { balanceRepository.save(balance) } returns balance
        every { transactionRepository.save(transaction) } returns transaction

        val result = transactionAuthorizationService.authorizeTransaction(transaction)

        assertEquals("00", result["code"])
        assertEquals("Transação Aprovada", result["message"])
        assertEquals(BigDecimal("50.00"), balance.amount)
    }

    @Test
    fun `Deve retornar código 07 quando ocorre um erro inesperado`() {
        // Configurando o mock para lançar uma exceção
        every { balanceRepository.findByAccountIdAndType(any(), any()) } throws RuntimeException("Erro inesperado")

        val transaction = Transaction(
            accountId = "12345",
            amount = BigDecimal(100),
            mcc = "5411",
            merchant = "Supermercado"
        )

        val response = transactionAuthorizationService.authorizeTransaction(transaction)

        assertEquals("07", response["code"])
        assertEquals("Erro ao processar transação", response["message"])
    }

    @Test
    fun `Deve aprovar transação com saldo suficiente na categoria refeição`() {
        val balance = Balance(accountId = "12345", type = BalanceType.MEAL, amount = BigDecimal(200))
        every { balanceRepository.findByAccountIdAndType("12345", BalanceType.MEAL) } returns balance
        every { transactionRepository.save(any()) } returnsArgument 0
        every { balanceRepository.save(any()) } returnsArgument 0

        val transaction = Transaction(
            accountId = "12345",
            amount = BigDecimal(100),
            mcc = "5812",
            merchant = "Restaurante"
        )

        val response = transactionAuthorizationService.authorizeTransaction(transaction)

        assertEquals("00", response["code"])
        assertEquals("Transação Aprovada", response["message"])
    }
}