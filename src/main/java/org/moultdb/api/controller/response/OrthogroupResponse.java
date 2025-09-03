package org.moultdb.api.controller.response;

import java.util.List;

/**
 * @author Valentine Rech de Laval
 * @since 2025-07-29
 */
public record OrthogroupResponse(Integer id, String name, List<GeneResponse> genes) {
}
