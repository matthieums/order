package com.switchfully.eurder.eurder.domain.user;

import java.util.List;

public enum UserType {
    ADMIN(List.of(Feature.ADD_ITEM, Feature.UPDATE_ITEM)),
    CUSTOMER(List.of(Feature.REORDER));

    private final List<Feature> features;

    UserType(List<Feature> features) {
        this.features = features;
    }

    public boolean containsFeature(Feature feature) { return features.contains(feature); }
}
