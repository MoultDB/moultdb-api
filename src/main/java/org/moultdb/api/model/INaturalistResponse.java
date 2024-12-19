package org.moultdb.api.model;

import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * @author Valentine Rech de Laval
 * @since 2024-11-28
 */
public record INaturalistResponse(Integer total_results, Integer page, Integer per_page,
                                  List<INaturalistObservation> results) {
    
    public final static List<String> OPEN_LICENSES = List.of("cc0", "cc-by", "cc-by-nc");
    
    public record INaturalistObservation(Integer id, INaturalistTaxon taxon, String species_guess,
                                         List<INaturalistOfv> ofvs,
                                         List<INaturalistProjectObservation> project_observations,
                                         List<INaturalistObservationPhoto> observation_photos) {}
    
    public record INaturalistTaxon(String id, String place_guess) {}
    
    public record INaturalistProjectObservation(INaturalistProject project, INaturalistUser user) {}
    
    public record INaturalistProject(Integer id) {}
    
    public record INaturalistUser(String login, String name, String orcid) {}
    
    public record INaturalistOfv(String name, String value, INaturalistUser user) {}
    
    public record INaturalistObservationPhoto(Integer photo_id, Integer position, INaturalistPhoto photo) {}
    
    public record INaturalistPhoto(Integer id, String license_code, String url, String attribution) {
        public String  smallPhotoUrl() {
            if (StringUtils.isBlank(url) || url.lastIndexOf("/") < 0) {
                return url;
            }
            return url.substring(0, url.lastIndexOf("/") + 1) + "small.jpeg";
        }
    }
}


