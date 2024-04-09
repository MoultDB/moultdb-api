package org.moultdb.api.controller;

import java.util.Collection;
import java.util.Objects;

/**
 * @author Valentine Rech de Laval
 * @since 2024-04-08
 */
public interface MoultdbController {
    
    static boolean hasMultipleParams(Collection<Object> params) {
        if (params != null) {
            return params.stream().noneMatch(Objects::isNull);
        }
        return false;
    }
}
