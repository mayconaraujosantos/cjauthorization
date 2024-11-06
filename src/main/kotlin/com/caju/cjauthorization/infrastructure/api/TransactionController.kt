package com.caju.cjauthorization.infrastructure.api

import com.caju.cjauthorization.application.service.TransactionAuthorizationService
import com.caju.cjauthorization.domain.enums.TransactionMessage
import com.caju.cjauthorization.domain.model.Transaction
import com.caju.cjauthorization.infrastructure.api.request.TransactionRequest
import com.caju.cjauthorization.infrastructure.api.response.TransactionResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import jakarta.validation.Valid
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/transactions")
class TransactionController(
    private val transactionAuthorizationService: TransactionAuthorizationService
) {
    private val logger = LoggerFactory.getLogger(TransactionController::class.java)

    @PostMapping("/authorize")
    @ResponseStatus(HttpStatus.OK) // Status sempre 200
    @Operation(
        summary = "Authorize a transaction",
        description = "This endpoint authorizes a transaction based on the provided details like accountId, amount, and merchant."
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Transaction authorized or processed with message code"),
        ]
    )
    fun authorizeTransaction(@Valid @RequestBody transactionRequest: TransactionRequest): TransactionResponse {
        logger.info("Recebendo solicitação para autorizar transação para conta ${transactionRequest.accountId}")

        return try {
            val transaction = Transaction(
                accountId = transactionRequest.accountId,
                amount = transactionRequest.amount,
                mcc = transactionRequest.mcc,
                merchant = transactionRequest.merchant
            )

            // Chama o serviço e pega o código e mensagem de resposta
            val response = transactionAuthorizationService.authorizeTransaction(transaction)
            val code = response["code"] ?: "07"
            val message = TransactionMessage.fromCode(code).message

            logger.info("Transação processada com código $code para conta ${transactionRequest.accountId}")

            TransactionResponse(code = code, message = message)
        } catch (e: Exception) {
            logger.error("Erro ao processar transação para conta ${transactionRequest.accountId}: ${e.message}", e)
            // Em caso de erro, retorna código e mensagem padrão, mas com status 200
            TransactionResponse(code = "07", message = "Erro ao processar a transação")
        }
    }
}