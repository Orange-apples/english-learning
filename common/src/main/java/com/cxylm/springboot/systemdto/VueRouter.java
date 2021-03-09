package com.cxylm.springboot.systemdto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class VueRouter<T> implements Serializable {

    private static final long serialVersionUID = 5637146332589423333L;
    @JsonIgnore
    private Long id;

    @JsonIgnore
    private Long pid;

    private String path;

    private String name;

    private String component;

    private Boolean hidden;

    private String icon;

    private String redirect;

    private RouterMeta meta;

    private List<VueRouter<T>> children;

    @JsonIgnore
    private boolean hasParent = false;

    @JsonIgnore
    private boolean hasChildren = false;

    public void initChildren() {
        this.children = new ArrayList<>();
    }

}
