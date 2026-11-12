package com.switchfully.eurder.eurder.api;

import com.switchfully.eurder.eurder.api.dtos.CreateItemDTO;
import com.switchfully.eurder.eurder.api.dtos.ItemDTO;
import com.switchfully.eurder.eurder.domain.UrgencyLevel;
import com.switchfully.eurder.eurder.domain.user.Feature;
import com.switchfully.eurder.eurder.service.ItemService;
import com.switchfully.eurder.eurder.service.SecurityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

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
    @Operation(summary = "Add a new item", description = "Creates a new item in the catalog")
    @ApiResponse(responseCode = "201", description = "Item created successfully")
    @ApiResponse(responseCode = "400", description = "Invalid item data")
    @ApiResponse(responseCode = "401", description = "Unauthorized - invalid authorization header")
    @ApiResponse(responseCode = "403", description = "Forbidden - insufficient permissions")
    @ResponseStatus(HttpStatus.CREATED)
    public ItemDTO addItem(@RequestHeader(value = "Authorization", required = false) String authorization, @Valid @RequestBody CreateItemDTO createItemDTO) {
        securityService.validateAuthorization(authorization, Feature.ADD_ITEM);
        return itemService.addItem(createItemDTO);
    }

    @PutMapping(value = "/{id}", consumes = APPLICATION_JSON, produces = APPLICATION_JSON)
    @Operation(summary = "Update an item", description = "Updates an existing item by ID")
    @ApiResponse(responseCode = "202", description = "Item updated successfully")
    @ApiResponse(responseCode = "400", description = "Invalid item data")
    @ApiResponse(responseCode = "401", description = "Unauthorized - invalid authorization header")
    @ApiResponse(responseCode = "403", description = "Forbidden - insufficient permissions")
    @ApiResponse(responseCode = "404", description = "Item not found")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ItemDTO updateItem(@RequestHeader(value = "Authorization", required = false) String authorization, @Valid @RequestBody CreateItemDTO createItemDTO, @PathVariable Long id) {
        securityService.validateAuthorization(authorization, Feature.UPDATE_ITEM);
        return itemService.updateItem(id, createItemDTO);
    }

    @GetMapping(value = "overview", produces = APPLICATION_JSON)
    @Operation(summary = "Get stock overview of items", description = "Get stock overview of items ordered by resupply urgency")
    @ApiResponse(responseCode = "200", description = "Items with specified urgency retrieved")
    @ApiResponse(responseCode = "401", description = "Unauthorized - invalid authorization header")
    @ApiResponse(responseCode = "403", description = "Forbidden - insufficient permissions")
    @ApiResponse(responseCode = "404", description = "No items found for the requested supply urgency")
    public List<ItemDTO> getItemsWithUrgency(
            @RequestHeader(value = "Authorization", required = false) String authorization,
            @RequestParam(name = "level", required = false) UrgencyLevel urgency
    ) {
        securityService.validateAuthorization(authorization, Feature.GET_ITEM_OVERVIEW);
        return itemService.getOverview(urgency);
    };
}
