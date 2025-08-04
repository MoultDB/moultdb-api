package org.moultdb.api.controller.response;

import org.moultdb.api.model.Orthogroup;
import org.moultdb.api.model.Pathway;

import java.util.Set;

/**
 * @author Valentine Rech de Laval
 * @since 2025-07-29
 */
public record PathwayOrthogroupResponse(String id, String name, String description, Set<Orthogroup> orthogroups) {
    public PathwayOrthogroupResponse(Pathway pathway, Set<Orthogroup> orthogroups) {
        this(pathway.getId(), pathway.getName(), pathway.getDescription(), orthogroups);
    }
}