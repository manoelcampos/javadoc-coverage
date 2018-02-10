package com.manoelcampos.javadoc.coverage.configuration;

import lombok.*;

@ToString
@Getter
@AllArgsConstructor
abstract class Option<T> {
    @NonNull
    private final String shortName;
    @NonNull 
    private final String longName;
    @NonNull 
    private final String description;
    private final int length;

    public abstract T parseValue(String[] opt);

    public final boolean isOption(String name) {
        return shortName.equals(name) || longName.equals(name);
    }

    public boolean hasDefaultValue() {
        return false;
    }
    
    public abstract T getDefaultValue();
    
    public int getLength() {
        return length;
    }
}
