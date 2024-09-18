package com.pretest.leesangyub.common.data;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ErrorData {
    private String code;
    private String message;
}
