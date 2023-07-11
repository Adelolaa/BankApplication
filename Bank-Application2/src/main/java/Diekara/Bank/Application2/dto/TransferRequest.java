package Diekara.Bank.Application2.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
    public class TransferRequest {

        private String receivingAccountNumber;
        private String sendingAccountNumber;
        private BigDecimal amount;
    }
