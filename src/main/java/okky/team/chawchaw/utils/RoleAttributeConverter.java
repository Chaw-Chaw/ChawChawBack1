package okky.team.chawchaw.utils;

import javax.persistence.AttributeConverter;

public class RoleAttributeConverter implements AttributeConverter<String, Integer> {

    @Override
    public Integer convertToDatabaseColumn(String attribute) {
        if ("손님".equals(attribute)) {
            return 1;
        }
        else if ("일반 사용자".equals(attribute)) {
            return 2;
        }
        else if ("관리자".equals(attribute)) {
            return 3;
        }
        return 0;
    }

    @Override
    public String convertToEntityAttribute(Integer dbData) {
        if (1 == dbData) {
            return "손님";
        }
        else if (2 == dbData) {
            return "일반 사용자";
        }
        else if (3 == dbData){
            return "관리자";
        }
        return "?";
    }
}
