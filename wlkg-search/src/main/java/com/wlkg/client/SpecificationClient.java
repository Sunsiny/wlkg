package com.wlkg.client;

import com.wlkg.item.api.SpecificationAPI;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "item-service")
public interface SpecificationClient extends SpecificationAPI {
}
