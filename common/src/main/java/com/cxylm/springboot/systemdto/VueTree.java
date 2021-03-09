package com.cxylm.springboot.systemdto;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class VueTree<T> implements Serializable {
    private static final long serialVersionUID = 2894885797283550801L;
    private Long id;

    private Long pid;

    private String name;

    private String permission;

    private int orderNum;

    private List<VueTree<T>> children;

    private boolean hasParent = false;

    private boolean hasChildren = false;

    public void initChildren() {
        this.children = new ArrayList<>();
    }

}
