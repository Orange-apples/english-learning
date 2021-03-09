package com.cxylm.springboot.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cxylm.springboot.dao.CourseCardMapper;
import com.cxylm.springboot.dto.form.CourseCardForm;
import com.cxylm.springboot.exception.AppBadRequestException;
import com.cxylm.springboot.model.BookInfo;
import com.cxylm.springboot.model.CourseCard;
import com.cxylm.springboot.model.CourseCardBind;
import com.cxylm.springboot.service.BookInfoService;
import com.cxylm.springboot.service.CourseCardBindService;
import com.cxylm.springboot.service.CourseCardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hashids.Hashids;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class CourseCardServiceImpl extends ServiceImpl<CourseCardMapper, CourseCard> implements CourseCardService {
    private final CourseCardBindService cardBindService;
    private final BookInfoService bookService;

    @Override
    public List<CourseCard> batchGenerate(CourseCardForm form) {
        final QueryWrapper<BookInfo> wrapper = new QueryWrapper<BookInfo>();
        //之前的部分课程为试用课程,部分需要开通,现在更改为所有都需要开通和试用,所以这个字段已经没有逻辑作用
        //wrapper.eq("free", false);
        if (!form.isBindAll()) {
            if (form.getCourseLevel() != null) wrapper.eq("level", form.getCourseLevel());
            if (form.getCoursePublisher() != null) wrapper.eq("edition", form.getCoursePublisher());
        }
        final List<BookInfo> books = bookService.list(wrapper);
        List<CourseCard> cards = new ArrayList<>();
        if (books.isEmpty()) return cards;

        final int amount = form.getAmount();
        final long now = System.currentTimeMillis();
        for (int i = 0; i < amount; i++) {
            CourseCard card = new CourseCard();
            BeanUtils.copyProperties(form, card);

            final long seed = now + i;
            long cardNo = a(seed);
            String password = hasher.encode(seed);
            card.setNo(cardNo);
            card.setPassword(password);

            cards.add(card);
        }
        this.saveBatch(cards);

        final List<CourseCardBind> cardBindsPrototype = books.stream().map(bookInfo -> {
            CourseCardBind cardBind = new CourseCardBind();
            cardBind.setBookId(bookInfo.getId());
            return cardBind;
        }).collect(Collectors.toList());

        List<CourseCardBind> cardBinds = new ArrayList<>();
        for (CourseCard card : cards) {
            final List<CourseCardBind> cardBindsToAdd = cardBindsPrototype.stream().map(courseCardBind -> {
                CourseCardBind cardBind = new CourseCardBind();
                cardBind.setBookId(courseCardBind.getBookId());
                cardBind.setCardId(card.getId());
                return cardBind;
            }).collect(Collectors.toList());

            cardBinds.addAll(cardBindsToAdd);
        }
        cardBindService.saveBatch(cardBinds);

        return cards;
    }


    static final Map<Long, Long> base = new HashMap<Long, Long>() {
        {
            put(0L, 6980494064739410L);
            put(1L, 9626019436048384L);
            put(4L, 6253546097218560L);
            put(5L, 4149466919136256L);

            put(8L, 5523585961421312L);
            put(9L, 2271000048923648L);
            put(12L, 3021185666282496L);
            put(13L, 9772220920416256L);

            put(16L, 2623313812294656L);
            put(17L, 6485247775174592L);
            put(20L, 2206269879292416L);
            put(21L, 1806246009469952L);

            put(24L, 2321625425418496L);
            put(25L, 5818380203583232L);
            put(28L, 3055971397044736L);
            put(29L, 7534864579602944L);
        }
    };
    private static final int MASK = (1 << 0) + (1 << 2) + (1 << 3) + (1 << 4);
    private static Hashids hasher = new Hashids("dkI3*1@", 8);

    public static long a(long n) {
        long idx = n & MASK;
        long xor = base.get(idx) ^ n;
        return ((xor | MASK) ^ MASK) | idx;
    }
}
