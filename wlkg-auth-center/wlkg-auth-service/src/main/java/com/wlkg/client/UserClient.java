package com.wlkg.client;

import com.wlkg.api.UserApi;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "user-service")
public interface UserClient extends UserApi {
}
