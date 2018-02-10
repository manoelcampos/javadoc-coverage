package com.manoelcampos.javadoc.coverage.configuration;

import java.util.Optional;

import lombok.NonNull;

class StringOption extends Option<String> {

    private final Optional<String> defaultValue;

    public StringOption(@NonNull String shortName, @NonNull String longName, @NonNull String description,
            @NonNull Optional<String> defaultValue) {
        super(shortName, longName, description, 2);
        this.defaultValue = defaultValue;
    }

    @Override
    public String parseValue(String[] value) {
        if (value.length != 2) {
            throw new IllegalStateException("Wrong option line passed, string options do only have 2 values");
        }
        return value[1];
    }

    @Override
    public boolean hasDefaultValue() {
        return defaultValue.isPresent();
    }

    @Override
    public String getDefaultValue() {
        return defaultValue.get();
    }
}
