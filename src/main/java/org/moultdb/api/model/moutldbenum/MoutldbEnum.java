package org.moultdb.api.model.moutldbenum;

import com.fasterxml.jackson.annotation.JsonValue;

import java.util.EnumSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Valentine Rech de Laval
 * @since 2022-04-05
 */
public interface MoutldbEnum {
    
    @JsonValue
    public String getStringRepresentation();
    
    /**
     * Defining whether {@code representation} is a representation of
     * an element of the {@code enumClass}.
     *
     * @param enumClass         A {@code Class<T>} that is the type of {@code MoutldbEnum}.
     * @param representation    A {@code String} that is the representation to be checked.
     * @return                  The {@code boolean} defining whether {@code representation} is a
     *                          representation of an element of the {@code enumClass}.
     */
    public static <T extends Enum<T> & MoutldbEnum> boolean isInEnum(Class<T> enumClass, String representation) {
        String lowCaseRepresentation = representation.toLowerCase(Locale.ENGLISH);
        for (T moultdbEnum: EnumSet.allOf(enumClass)) {
            if (moultdbEnum.getStringRepresentation().toLowerCase(Locale.ENGLISH).equals(lowCaseRepresentation) ||
                    moultdbEnum.name().toLowerCase(Locale.ENGLISH).equals(lowCaseRepresentation)) {
                return true;
            }
        }
        return false;
    }
    
    public static <T extends Enum<T> & MoutldbEnum> List<String> getAllStringRepresentations(Class<T> enumClass) {
        return EnumSet.allOf(enumClass)
                      .stream()
                      .map(MoutldbEnum::getStringRepresentation)
                      .collect(Collectors.toList());
    }
    
    static <T extends Enum<T> & MoutldbEnum> T valueOfByStringRepresentation(Class<T> enumClass, String representation) throws IllegalArgumentException {
        if (representation == null) {
            return null;
        }
        for (T element: enumClass.getEnumConstants()) {
            if (element.getStringRepresentation().equalsIgnoreCase(representation) ||
                    element.name().equalsIgnoreCase(representation)) {
                return element;
            }
        }
        throw new IllegalArgumentException("'" + representation + "' is unknown for " + enumClass.getName());
    }
    
    static <T extends Enum<T> & MoutldbEnum> Set<T> valueOfByStringRepresentation(Class<T> enumClass, Set<String> representations) throws IllegalArgumentException {
        if (representations == null) {
            return null;
        }
        return representations.stream()
                .map(s -> valueOfByStringRepresentation(enumClass, s))
                .collect(Collectors.toSet());
    }
    
}
