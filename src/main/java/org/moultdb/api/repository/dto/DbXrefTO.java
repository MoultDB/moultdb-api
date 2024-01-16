package org.moultdb.api.repository.dto;

import java.io.Serial;
import java.util.StringJoiner;

/**
 * @author Valentine Rech de Laval
 * @since 2021-11-01
 */
public class DbXrefTO extends NamedEntityTO<Integer> {
    
    @Serial
    private static final long serialVersionUID = 3728339746982269896L;
    
    private final String accession;
    private final DataSourceTO dataSourceTO;
    
    public DbXrefTO(Integer id, String accession, String name, DataSourceTO dataSourceTO) throws IllegalArgumentException {
        super(id, name, null);
        this.accession = accession;
        this.dataSourceTO = dataSourceTO;
    }
    
    public String getAccession() {
        return accession;
    }
    
    public DataSourceTO getDataSourceTO() {
        return dataSourceTO;
    }
    
    @Override
    public String toString() {
        return new StringJoiner(", ", DbXrefTO.class.getSimpleName() + "[", "]")
                .add("id='" + getId() + "'")
                .add("accession='" + accession + "'")
                .add("name='" + getName() + "'")
                .add("dataSourceTO=" + dataSourceTO)
                .toString();
    }
}
