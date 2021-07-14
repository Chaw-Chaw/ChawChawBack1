package okky.team.chawchaw.utils;

import okky.team.chawchaw.user.Role;

import javax.persistence.AttributeConverter;
import java.util.EnumSet;
import java.util.NoSuchElementException;

public class RoleAttributeConverter implements AttributeConverter<Role, String> {

    @Override
    public String convertToDatabaseColumn(Role attribute) {
        return attribute.getValue();
    }

    @Override
    public Role convertToEntityAttribute(String dbData) {
        return EnumSet.allOf(Role.class).stream()
                .filter(e -> e.getValue().equals(dbData))
                .findAny()
                .orElseThrow(NoSuchElementException::new);
    }
}
