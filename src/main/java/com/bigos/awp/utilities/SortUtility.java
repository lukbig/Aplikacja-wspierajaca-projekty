package com.bigos.awp.utilities;

import org.springframework.data.domain.Sort;

/**
 * Created by bigos on 04.12.16.
 */
public class SortUtility {

    public static Sort orderBy(String direction, String attribute) {
        return new Sort(Sort.Direction.fromStringOrNull(direction), attribute);
    }
}
