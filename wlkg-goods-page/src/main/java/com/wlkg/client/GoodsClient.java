package com.wlkg.client;

import com.wlkg.item.api.GoodsAPI;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(value = "item-service")
public interface GoodsClient extends GoodsAPI {
}
