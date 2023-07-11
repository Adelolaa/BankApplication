package Diekara.Bank.Application2.dto;

import lombok.*;



    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @Getter
    @Setter


    public class RegisterDto {
        private  String firstName;
        private String lastName;
        private String otherName;
        private String username;
        private String email;
        private String password;
    }


