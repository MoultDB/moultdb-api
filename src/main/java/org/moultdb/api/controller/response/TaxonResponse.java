package org.moultdb.api.controller.response;

import java.util.List;

public record TaxonResponse(String id, String scientificName, String accession, List<OrthogroupResponse> orthogroups) {
}
