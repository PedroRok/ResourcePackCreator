package com.pedrorok.core.utils;

import lombok.Getter;

/**
 * @author Rok, Pedro Lucas nmm. Created on 13/11/2024
 * @project ResourcePackCreator
 */
@Getter
public enum PredicateType {

    CUSTOM_MODEL_DATA("custom_model_data", Integer.class),
    TRIM_TYPE("trim_type", Float.class);

    private final String value;
    private final Class<?> type;
    <T extends Number> PredicateType(String value, Class<T> type) {
        this.value = value;
        this.type = type;
    }


}
