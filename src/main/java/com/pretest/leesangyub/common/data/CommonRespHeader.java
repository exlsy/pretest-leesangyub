package com.pretest.leesangyub.common.data;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommonRespHeader {
    private String trId;
    private String rtnCode;
    private String rtnMessage;
}
