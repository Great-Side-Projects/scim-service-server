package com.service.scim.services.specifications;

import com.service.scim.models.User;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserSpecifications {

    public static Specification<User> createUserSpecification(Map<String, String> params) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Extraer el filtro de los parámetros
            String filter = params.get("filter");
            if (filter != null) {
                // Detectar el formato de filtro y extraer la clave, el operador y el valor
                String[] filterParts = extractKeyOperatorAndValue(filter);
                if (filterParts != null) {
                    String key = filterParts[0];
                    String operator = filterParts[1];
                    String value = filterParts[2];

                    // Crear predicados basados en el operador
                    switch (key) {
                        case "active":
                            Boolean isActive = Boolean.parseBoolean(value);
                            predicates.add(criteriaBuilder.equal(root.get("active"), isActive));
                            break;
                        default:
                            if ("eq".equals(operator)) {
                                predicates.add(criteriaBuilder.equal(root.get(key), value));
                            } else if ("co".equals(operator)) {
                                predicates.add(criteriaBuilder.like(root.get(key), "%" + value + "%"));
                            }
                            break;
                        // añadir más casos según otros atributos y operadores
                    }
                }
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

    // Metodo para extraer clave, operador y valor de un filtro SCIM
    private static String[] extractKeyOperatorAndValue(String filter) {
        // Regex para soportar operadores como eq y co
        String regex = "(\\w+) (eq|co) \"([^\"]*)\"";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(filter);

        if (matcher.find()) {
            return new String[]{matcher.group(1), matcher.group(2), matcher.group(3)};
        }
        return null;
    }
}

