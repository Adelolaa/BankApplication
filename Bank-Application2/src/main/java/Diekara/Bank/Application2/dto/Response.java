package Diekara.Bank.Application2.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@lombok.Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Response {

    private  String responseCode;
    private String responseMessage;
    private Data data;
}
