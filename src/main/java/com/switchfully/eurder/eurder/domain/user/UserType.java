package com.switchfully.eurder.eurder.domain.user;

import java.util.List;

public enum UserType {
    ADMIN(List.of(Feature.ADD_ITEM, Feature.UPDATE_ITEM, Feature.VIEW_ALL_CUSTOMERS, Feature.VIEW_CUSTOMER_DETAILS, Feature.GET_ORDER_OF_ITEM_WITH_SHIPPING_DATE)),
    CUSTOMER(List.of(Feature.REORDER, Feature.VIEW_REPORT));

    private final List<Feature> features;

    UserType(List<Feature> features) {
        this.features = features;
    }

    public boolean containsFeature(Feature feature) { return features.contains(feature); }
}
