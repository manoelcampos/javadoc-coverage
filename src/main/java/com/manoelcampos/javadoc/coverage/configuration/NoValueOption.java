package com.manoelcampos.javadoc.coverage.configuration;

import lombok.NonNull;

class NoValueOption extends Option<Void> {

    public NoValueOption(@NonNull String shortName, @NonNull String longName, @NonNull String description) {
        super(shortName, longName, description, 1);
    }

    @Override
    public Void parseValue(String[] value) {
        throw new UnsupportedOperationException("NoValueOption does not have a value.");
    }

    @Override
    public Void getDefaultValue() {
        throw new UnsupportedOperationException("NoValueOption does not have a default value.");
    }
}
