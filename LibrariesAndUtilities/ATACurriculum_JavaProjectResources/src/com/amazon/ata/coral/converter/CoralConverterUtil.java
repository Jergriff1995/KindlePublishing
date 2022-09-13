package com.amazon.ata.coral.converter;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Provides common coral conversion utility methods.
 */
public final class CoralConverterUtil {

    private CoralConverterUtil() {}

    /**
     * Converts an input list to a new list by applying the provided function to each item in the new list.
     *
     * Example: inputList = [1, 2, 3, 4, 5], converter = int -> int + 2
     *          returns [3, 4, 5, 6, 7]
     * @param inputList the list that should be converted using the provided function
     * @param converter the functions to apply to each item in the provided input list
     * @param <T> the type of items in the input list
     * @param <R> the type of items desired in the returned list
     * @return the converted list
     */
    public static <T, R>  List<R> convertList(List<T> inputList, Function<T, R> converter) {
        return inputList.stream().map(converter).collect(Collectors.toList());
    }
}
