package org.moultdb.api.model;

import org.apache.commons.lang3.StringUtils;

import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * @author Valentine Rech de Laval
 * @since 2024-11-28
 */
public record INaturalistResponse(Integer total_results, Integer page, Integer per_page,
                                  List<INaturalistObservation> results) {
    
    public record INaturalistObservation(Integer id, INaturalistUser user, INaturalistTaxon taxon, String species_guess,
                                         List<INaturalistOfv> ofvs,
                                         List<INaturalistObservationPhoto> observation_photos,
                                         List<INaturalistIdentification> identifications,
                                         Date observed_on) {}
    
    public record INaturalistTaxon(String id, String name, String place_guess, String parent_id) {}
    
    public record INaturalistIdentification(INaturalistTaxon taxon, INaturalistUser user, Date created_at) {}
    
    public record INaturalistUser(String login, String name, String orcid) {
        @Override
        public boolean equals(Object o) {
            if (o == null || getClass() != o.getClass()) return false;
            INaturalistUser that = (INaturalistUser) o;
            return Objects.equals(login, that.login);
        }
        
        @Override
        public int hashCode() {
            return Objects.hashCode(login);
        }
    }
    
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


