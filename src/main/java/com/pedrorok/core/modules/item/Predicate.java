package com.pedrorok.core.modules.item;

import com.pedrorok.core.utils.PredicateType;

/**
 * @author Rok, Pedro Lucas nmm. Created on 13/11/2024
 * @project ResourcePackCreator
 */
public record Predicate<T extends Number>(T value, PredicateType type, String modelPath)  {
}
