package com.wlkg.client;

import com.wlkg.item.api.CategoryAPI;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(value = "item-service")
public interface CategoryClient extends CategoryAPI {
}
