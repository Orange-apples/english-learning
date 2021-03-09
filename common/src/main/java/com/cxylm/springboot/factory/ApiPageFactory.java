package com.cxylm.springboot.factory;

import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Objects;

/**
 * 分页工厂
 */
public class ApiPageFactory {
    private static final int MAX_SIZE_PER_PAGE = 100;
    private static final int DEFAULT_SIZE = 10;

    public static <T> Page<T> getPage(org.springframework.data.domain.Page<T> springPage) {
        final List<T> content = springPage.getContent();
        return new Page<T>(springPage.getNumber(), springPage.getTotalElements()).setRecords(content);
    }

    public static <T> Page<T> getPage() {
        HttpServletRequest request = HttpContext.getRequest();
        int pageParam, sizeParam;
        try {
            String pageStr = Objects.requireNonNull(request).getParameter("p");
            if (pageStr == null) {
                pageParam = 1;
            } else {
                pageParam = Integer.parseInt(pageStr);
            }

            String sizeStr = request.getParameter("size");
            if (sizeStr == null) {
                sizeParam = DEFAULT_SIZE;
            } else {
                sizeParam = Integer.parseInt(sizeStr);
                if (sizeParam < 1 || sizeParam > MAX_SIZE_PER_PAGE) {
                    sizeParam = DEFAULT_SIZE;
                }
            }
        } catch (NumberFormatException e) {
            return new Page<>();
        }

        return new Page<>(pageParam, sizeParam);
    }

    public static <T> Page<T> getSortedPage() {
        HttpServletRequest request = HttpContext.getRequest();
        final Page<T> page = getPage();
        // 排序字段名
        String order = Objects.requireNonNull(request).getParameter("order");
        // asc或desc(升序或降序)
        String sort = request.getParameter("sort");

        if (StringUtils.isNotEmpty(order)) {
            if ("desc".equalsIgnoreCase(sort)) {
                page.addOrder(OrderItem.desc(order));
            } else {
                page.addOrder(OrderItem.asc(order));
            }
        }
        return page;
    }
}
