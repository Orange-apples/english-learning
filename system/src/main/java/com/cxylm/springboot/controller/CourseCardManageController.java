package com.cxylm.springboot.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cxylm.springboot.constant.CacheName;
import com.cxylm.springboot.dto.form.CourseCardForm;
import com.cxylm.springboot.factory.ApiPageFactory;
import com.cxylm.springboot.model.CourseCard;
import com.cxylm.springboot.model.CourseCardBind;
import com.cxylm.springboot.response.AppResponse;
import com.cxylm.springboot.service.CourseCardBindService;
import com.cxylm.springboot.service.CourseCardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.hashids.Hashids;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/api/manager")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class CourseCardManageController extends ApiController {
    private final CourseCardService courseCardService;
    private final CourseCardBindService courseCardBindServiceService;
    private final RedissonClient redisson;

    private long time = 0;


    /**
     * 生成卡密
     * @param form
     * @return
     */
    //     @PostMapping("/course/{courseId}/card")
    @PostMapping("/course/card")
    @RequiresPermissions("card:add")
    public Object addWords(/*@PathVariable Integer courseId,*/ @RequestBody @Validated CourseCardForm form) {
        /*CourseCard courseCard = courseCardService.getOne(new QueryWrapper<CourseCard>().eq("no", form.getNo()));
        if (courseCard != null) return AppResponse.badRequest("该卡号已存在");
        CourseCard card = new CourseCard();
        BeanUtils.copyProperties(form, card);
//        card.setCourseId(courseId);
        courseCardService.save(card);*/


        final RLock lock = redisson.getLock(CacheName.CARD_BATCH_LOCK);
        try {
            final boolean isLocked = lock.tryLock(2, 600, TimeUnit.SECONDS);
            if (!isLocked) {
                return AppResponse.badRequest("服务器繁忙，请稍后再试");
            }
            if (System.currentTimeMillis() < time) return AppResponse.badRequest("服务器繁忙, 请稍后再试");
            time = System.currentTimeMillis();

            final List<CourseCard> courseCards = courseCardService.batchGenerate(form);
            time = System.currentTimeMillis() + 5000;
            return AppResponse.ok(courseCards);
        } catch (InterruptedException e) {
            log.error("Error while getting COURSE_LOCK redis lock.", e);
            return AppResponse.badRequest("服务器繁忙，请稍后再试");
        } finally {
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }

    @PostMapping("/course/card/{cardId}/course/{bookId}/bind")
    @RequiresPermissions("card:add")
    public Object bindCardCourse(@PathVariable Long cardId, @PathVariable Integer bookId) {
        CourseCardBind bind = new CourseCardBind();
        bind.setBookId(bookId);
        bind.setCardId(cardId);
        courseCardBindServiceService.save(bind);
        return SUCCESS;
    }

    @DeleteMapping("/course/card/{cardId}/course/{bookId}/bind")
    @RequiresPermissions("card:add")
    public Object unbindCardCourse(@PathVariable Integer cardId, @PathVariable Integer bookId) {
        courseCardBindServiceService.remove(new QueryWrapper<CourseCardBind>().eq("card_id", cardId).eq("book_id", bookId));
        return SUCCESS;
    }

    @GetMapping("/course/card/{cardId}/courses")
    @RequiresPermissions("card:add")
    public Object getBindCourses(@PathVariable Integer cardId) {
        final List<CourseCardBind> list = courseCardBindServiceService.list(new QueryWrapper<CourseCardBind>().eq("card_id", cardId));
        return AppResponse.ok(list);
    }

    @GetMapping("/course/cards")
    @RequiresPermissions("card:view")
    public Object getCards() {
        IPage<CourseCard> res = courseCardService.page(ApiPageFactory.getPage(), new QueryWrapper<CourseCard>().orderByDesc("id"));
        return AppResponse.ok(res);
    }


    @GetMapping("/course/{courseId}/cards")
    @RequiresPermissions("card:view")
    public Object getCards(@PathVariable Integer courseId) {
        IPage<CourseCard> res = courseCardService.page(ApiPageFactory.getPage(), new QueryWrapper<CourseCard>().eq("course_id", courseId).orderByDesc("id"));
        return AppResponse.ok(res);
    }

    @GetMapping("/course/card/preview")
    public Object previewCard() {
        Map<String, Object> res = new HashMap<>();
        res.put("no", a(System.currentTimeMillis()));
        res.put("password", hasher.encode(System.currentTimeMillis()));
        return AppResponse.ok(res);
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
