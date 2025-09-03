package org.moultdb.api.controller.response;

import java.util.List;

/**
 * @author Valentine Rech de Laval
 * @since 2025-07-29
 */
public record PathwayResponse(String id, String name, List<TaxonResponse> taxa) {
}
