package com.switchfully.eurder.eurder.api;

import com.switchfully.eurder.eurder.api.dtos.CreateItemDTO;
import com.switchfully.eurder.eurder.api.dtos.ItemDTO;
import com.switchfully.eurder.eurder.service.ItemService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "items")
public class ItemController {
    private static final String APPLICATION_JSON = "application/json";

    private final ItemService itemService;

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @PostMapping(consumes = APPLICATION_JSON, produces = APPLICATION_JSON)
    @ResponseStatus(HttpStatus.CREATED)
    public ItemDTO addItem(@Valid @RequestBody CreateItemDTO createItemDTO) {
        return itemService.addItem(createItemDTO);
    }

    @PutMapping(value = {"/{id}"}, consumes = APPLICATION_JSON, produces = APPLICATION_JSON)
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ItemDTO updateItem(@Valid @RequestBody CreateItemDTO createItemDTO, @PathVariable String id) {
        return itemService.updateItem(id, createItemDTO);
    }
}
