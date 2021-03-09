package com.cxylm.springboot.controller;

import com.cxylm.springboot.response.AppResponse;
import com.cxylm.springboot.service.BookInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/manager")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class BookController extends ApiController {
    private final BookInfoService booksService;

    @GetMapping("/book/publishers")
    public Object selectBookPublishers() {
        return AppResponse.ok(booksService.selectBookPublishers());
    }
}
