package com.caju.cjauthorization.domain.enums

enum class TransactionMessage(val code: String, val message: String) {
    APPROVED("00", "Transação Aprovada"),
    INSUFFICIENT_FUNDS("51", "Saldo Insuficiente"),
    PROCESSING_ERROR("07", "Erro ao processar a transação"),
    UNKNOWN("06", "Erro desconhecido");// pensei em deixar o padrao 07

    companion object {
        fun fromCode(code: String) = values().find { it.code == code } ?: UNKNOWN
    }
}