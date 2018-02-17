package com.manoelcampos.javadoc.coverage.configuration;

import java.util.Optional;

import lombok.NonNull;

class CoverageLimitsOption extends Option<CoverageLimits> {

    private final Optional<CoverageLimits> defaultValue;

    public CoverageLimitsOption(@NonNull String shortName, @NonNull String longName, @NonNull String description,
            @NonNull Optional<CoverageLimits> defaultValue) {
        super(shortName, longName, description, 2);
        this.defaultValue = defaultValue;
    }

    @Override
    public CoverageLimits parseValue(String[] value) {
        if (value.length != 5) {
            throw new IllegalArgumentException(
                    "Wrong option line passed, min coverage options do have exactly 5 values");
        }
        return CoverageLimits.fromOptionsArray(value);
    }

    @Override
    public boolean hasDefaultValue() {
        return defaultValue.isPresent();
    }

    @Override
    public CoverageLimits getDefaultValue() {
        return defaultValue.get();
    }
}
