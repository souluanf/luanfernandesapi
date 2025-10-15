package dev.luanfernandes.config.init;

import static java.nio.charset.StandardCharsets.UTF_8;

import dev.luanfernandes.domain.enums.TransactionType;
import dev.luanfernandes.domain.enums.UserRole;
import dev.luanfernandes.dto.request.TransactionRequest;
import dev.luanfernandes.dto.request.UserRegisterRequest;
import dev.luanfernandes.service.TransactionService;
import dev.luanfernandes.service.UserService;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class Loader implements CommandLineRunner {

    private final UserService userService;
    private final TransactionService transactionService;

    @Override
    public void run(String... args) throws Exception {

        ClassPathResource usersResource = new ClassPathResource("data/users.csv");
        InputStreamReader usersReader = new InputStreamReader(usersResource.getInputStream(), UTF_8);
        BufferedReader usersBuffer = new BufferedReader(usersReader);
        String linha = usersBuffer.readLine();
        String[] campos;

        while (linha != null) {

            campos = linha.split(";");
            UserRegisterRequest userRegisterRequest = UserRegisterRequest.builder()
                    .name(campos[0])
                    .email(campos[1])
                    .role(UserRole.valueOf(campos[2]))
                    .build();

            userService.createUser(userRegisterRequest);
            linha = usersBuffer.readLine();
        }

        userService.listUsers().forEach(user -> log.info("User: {}", user));

        usersBuffer.close();

        ClassPathResource transactionsResource = new ClassPathResource("data/transactions.csv");
        InputStreamReader transactionsReader = new InputStreamReader(transactionsResource.getInputStream(), UTF_8);

        BufferedReader transactionsBuffer = new BufferedReader(transactionsReader);
        linha = transactionsBuffer.readLine();

        while (linha != null) {
            campos = linha.split(";");
            Integer userId = Integer.parseInt(campos[5]);
            TransactionRequest transactionRequest = TransactionRequest.builder()
                    .type(TransactionType.valueOf(campos[0]))
                    .category(campos[1])
                    .amount(new BigDecimal(campos[2]))
                    .transactionDate(LocalDate.parse(campos[3]))
                    .description(campos[4])
                    .build();

            transactionService.createTransaction(transactionRequest, userId);
            linha = transactionsBuffer.readLine();
        }

        log.info(
                "Total de transações gravadas: {}",
                transactionService.listTransactions(1).size()
                        + transactionService.listTransactions(2).size()
                        + transactionService.listTransactions(3).size()
                        + transactionService.listTransactions(4).size());

        transactionsBuffer.close();
    }
}
