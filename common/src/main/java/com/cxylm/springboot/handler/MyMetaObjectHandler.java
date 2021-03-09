package com.cxylm.springboot.handler;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;
import java.util.Date;
import java.util.Objects;

import static com.cxylm.springboot.constant.ApiConstant.REQUEST_ATTR_USERNAME;

/**
 * 自动填充实体类中的属性
 */
@Slf4j
@Component
public class MyMetaObjectHandler implements MetaObjectHandler {

    public static HttpServletRequest getRequest() {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return requestAttributes == null ? null : requestAttributes.getRequest();
    }
    protected Object getAttr(String name) {
        return Objects.requireNonNull(getRequest()).getAttribute(name);
    }

    protected String getUserName() {
        return (String) getAttr(REQUEST_ATTR_USERNAME);
    }

    @Override
    public void insertFill(MetaObject metaObject) {
        exeFill("createTime",new Date(),metaObject);
        exeFill("createUser",getUserName(),metaObject);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        exeFill("updateTime",new Date(),metaObject);
        exeFill("updateUser",getUserName(),metaObject);
    }

    /**
     * 填充的属性需要有FieldFill注解,并且不是 DEFAULT 属性
     * @param fieldName
     * @param fieldVal
     * @param metaObject
     */
    public void exeFill(String fieldName, Object fieldVal, MetaObject metaObject){
        if (metaObject.hasSetter(fieldName) && metaObject.hasGetter(fieldName)) {
            Object classObj = metaObject.getOriginalObject();
            Field[] declaredFields = classObj.getClass().getDeclaredFields();
            if(declaredFields!=null){
                for(Field field:declaredFields){
                    if(field.getName().equals(fieldName)){
                        TableField tableField = field.getAnnotation(TableField.class);
                        if(tableField!=null && tableField.fill()!= FieldFill.DEFAULT){
                            this.setFieldValByName(fieldName, fieldVal, metaObject);
                        }
                    }
                }
            }
        }
    }

}