package com.cxylm.springboot.repository.redis;

import com.cxylm.springboot.model.AppUser;
import org.springframework.data.repository.CrudRepository;

public interface AppUserRepository extends CrudRepository<AppUser, Integer> {
    AppUser findAppUserById(Integer userId);
}
