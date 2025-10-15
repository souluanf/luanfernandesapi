package dev.luanfernandes.controller;

import static dev.luanfernandes.constants.PathConstants.TRANSACTIONS;
import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

import dev.luanfernandes.dto.request.TransactionRequest;
import dev.luanfernandes.dto.response.MonthlyBalanceResponse;
import dev.luanfernandes.dto.response.TransactionResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "Transações", description = "Endpoints para gerenciamento de receitas e despesas")
@RequestMapping(value = TRANSACTIONS, produces = APPLICATION_JSON_VALUE)
public interface TransactionController {

    @Operation(
            summary = "Criar nova transação",
            description = "Cria uma nova transação (receita ou despesa) para o usuário")
    @ApiResponse(responseCode = "201", description = "Transação criada com sucesso")
    @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos")
    @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    @PostMapping
    ResponseEntity<TransactionResponse> createTransaction(
            @Valid @RequestBody TransactionRequest request, @RequestHeader Integer userId);

    @Operation(summary = "Buscar transação por ID", description = "Retorna os detalhes de uma transação específica")
    @ApiResponse(responseCode = "200", description = "Transação encontrada com sucesso")
    @ApiResponse(responseCode = "404", description = "Transação ou usuário não encontrado")
    @GetMapping("/{id}")
    ResponseEntity<TransactionResponse> getTransactionById(@PathVariable Integer id, @RequestHeader Integer userId);

    @Operation(
            summary = "Listar transações do usuário",
            description = "Retorna todas as transações (receitas e despesas) do usuário")
    @ApiResponse(responseCode = "200", description = "Lista de transações retornada com sucesso")
    @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    @GetMapping
    ResponseEntity<List<TransactionResponse>> listTransactions(@RequestHeader Integer userId);

    @Operation(summary = "Atualizar transação", description = "Atualiza os dados de uma transação existente")
    @ApiResponse(responseCode = "200", description = "Transação atualizada com sucesso")
    @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos")
    @ApiResponse(responseCode = "404", description = "Transação ou usuário não encontrado")
    @PutMapping("/{id}")
    ResponseEntity<TransactionResponse> updateTransaction(
            @PathVariable Integer id, @Valid @RequestBody TransactionRequest request, @RequestHeader Integer userId);

    @Operation(summary = "Deletar transação", description = "Remove uma transação do sistema")
    @ApiResponse(responseCode = "204", description = "Transação deletada com sucesso")
    @ApiResponse(responseCode = "404", description = "Transação ou usuário não encontrado")
    @DeleteMapping("/{id}")
    ResponseEntity<Void> deleteTransaction(@PathVariable Integer id, @RequestHeader Integer userId);

    @Operation(
            summary = "Calcular saldo mensal",
            description = "Calcula o saldo mensal do usuário (total de receitas - total de despesas)")
    @ApiResponse(responseCode = "200", description = "Saldo mensal calculado com sucesso")
    @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    @GetMapping("/balance")
    ResponseEntity<MonthlyBalanceResponse> getMonthlyBalance(
            @RequestParam int year, @RequestParam int month, @RequestHeader Integer userId);
}
