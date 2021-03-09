package com.cxylm.springboot.systemdto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RouterMeta implements Serializable {
    private static final long serialVersionUID = 7688431168305687115L;
    private Boolean closeable;
    private Boolean isShow;
    private String title;
    private String icon;

    public RouterMeta(Boolean closeable,Boolean isShow){
        this.closeable = closeable;
        this.isShow = isShow;
    }
}
