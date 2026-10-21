package com.switchfully.eurder.eurder.api;

import com.switchfully.eurder.eurder.api.dtos.CreateItemDTO;
import com.switchfully.eurder.eurder.api.dtos.ItemDTO;
import com.switchfully.eurder.eurder.domain.user.Feature;
import com.switchfully.eurder.eurder.service.ItemService;
import com.switchfully.eurder.eurder.service.SecurityService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "items")
public class ItemController {
    private static final String APPLICATION_JSON = "application/json";
    private final SecurityService securityService;

    private final ItemService itemService;

    public ItemController(ItemService itemService, SecurityService securityService) {
        this.itemService = itemService;
        this.securityService = securityService;
    }

    @PostMapping(consumes = APPLICATION_JSON, produces = APPLICATION_JSON)
    @ResponseStatus(HttpStatus.CREATED)
    public ItemDTO addItem(@RequestHeader(value = "Authorization", required = false) String authorization, @Valid @RequestBody CreateItemDTO createItemDTO) {
        securityService.validateAuthorization(authorization, Feature.ADD_ITEM);
        return itemService.addItem(createItemDTO);
    }

    @PutMapping(value = "/{id}", consumes = APPLICATION_JSON, produces = APPLICATION_JSON)
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ItemDTO updateItem(@RequestHeader(value = "Authorization", required = false) String authorization, @Valid @RequestBody CreateItemDTO createItemDTO, @PathVariable String id) {
        securityService.validateAuthorization(authorization, Feature.UPDATE_ITEM);
        return itemService.updateItem(id, createItemDTO);
    }
}
