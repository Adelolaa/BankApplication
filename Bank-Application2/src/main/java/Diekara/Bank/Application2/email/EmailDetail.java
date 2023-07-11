package Diekara.Bank.Application2.email;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class EmailDetail {
    private String recipient;
    private String messageBody;
    private String subject;
    private String attachment;


}
