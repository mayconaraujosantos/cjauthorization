package com.caju.cjauthorization.infrastructure.api.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import kotlin.test.Test

@SpringBootTest
@AutoConfigureMockMvc
class TransactionControllerTest(@Autowired val mockMvc: MockMvc) {

    @Test
    fun `deve aprovar a transação e usar o saldo FOOD ou MEAL conforme MCC e comerciante`() {
        // Teste para FOOD
        mockMvc.perform(
            post("/transactions/authorize")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    """{
                       "accountId": "1",
                       "amount": "50.00",
                       "mcc": "5812",
                       "merchant": "UBER EATS"
                    }"""
                )
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.code").value("00"))
            .andExpect(jsonPath("$.message").value("Transação Aprovada"))

        // Teste para MEAL
        mockMvc.perform(
            post("/transactions/authorize")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    """{
                       "accountId": "1",
                       "amount": "30.0",
                       "mcc": "5811",
                       "merchant": "PADARIA DO ZE São Paulo"
                    }"""
                )
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.code").value("00"))
            .andExpect(jsonPath("$.message").value("Transação Aprovada"))
    }

    @Test
    fun `deve recusar a transação devido a saldo insuficiente para a categoria MEAL`() {
        mockMvc.perform(
            post("/transactions/authorize")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    """{
                       "accountId": "1",
                       "amount": "60.00",
                       "mcc": "5811",
                       "merchant": "RESTAURO"
                    }"""
                )
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.code").value("51"))
            .andExpect(jsonPath("$.message").value("Saldo Insuficiente"))
    }

    @Test
    fun `deve aprovar a transação com saldo CASH suficiente quando nenhuma categoria for aplicável`() {
        mockMvc.perform(
            post("/transactions/authorize")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    """{
                       "accountId": "1",
                       "amount": "150.00",
                       "mcc": "4121",
                       "merchant": "PAGAMENTO ONLINE"
                    }"""
                )
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.code").value("00"))
            .andExpect(jsonPath("$.message").value("Transação Aprovada"))
    }

    @Test
    fun `deve recusar a transação com valor inválido (zero)`() {
        mockMvc.perform(
            post("/transactions/authorize")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    """{
                       "accountId": "1",
                       "amount": "00.00",
                       "mcc": "5812",
                       "merchant": "UBER EATS"
                    }"""
                )
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.code").value("07"))
            .andExpect(jsonPath("$.message").value("Erro ao processar a transação"))
    }

    @Test
    fun `deve aprovar a transação quando o MCC não for reconhecido (mapeando para CASH)`() {
        mockMvc.perform(
            post("/transactions/authorize")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    """{
                       "accountId": "1",
                       "amount": "30.00",
                       "mcc": "1234",
                       "merchant": "SHOPPING CENTER"
                    }"""
                )
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.code").value("00"))
            .andExpect(jsonPath("$.message").value("Transação Aprovada"))
    }
}