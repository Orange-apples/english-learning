package com.cxylm.springboot.dto.form;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AppUserSearchForm {
    private String phone;
    private String username;
    private Integer state;
    private Integer isMerchant;
    private Integer rank;
}
