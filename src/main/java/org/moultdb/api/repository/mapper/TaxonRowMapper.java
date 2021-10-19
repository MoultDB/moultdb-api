package org.moultdb.api.repository.mapper;

import org.moultdb.api.model.Taxon;
import org.moultdb.api.repository.dto.TaxonDto;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Valentine Rech de Laval
 * @since 2021-10-18
 */
public class TaxonRowMapper implements RowMapper<TaxonDto> {
    
    @Override
    public TaxonDto mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new TaxonDto(rs.getInt("id"), rs.getString("scientific_name"), rs.getString("common_name"),
                rs.getString("taxon_rank"), rs.getInt("ncbi_tax_id"), rs.getBoolean("extinct"), rs.getString("path"));
    }
    
}
