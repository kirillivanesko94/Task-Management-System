package com.example.jira.db.common.search;

import com.example.jira.common.*;
import org.jdbi.v3.core.*;
import org.jdbi.v3.core.statement.*;
import org.springframework.util.*;

import java.util.*;

public class JdbiSearchCriteriaQueryBuilder {
    private JdbiSearchCriteriaQueryBuilder() {
        // this is utility class
    }

    // Строит полностью подготовленный для вызова запрос.
    // Пробрасывает фильтры в секцию where, задает параметры
    public static Query buildQuery(
        Handle conn,
        String baseQuery,
        String orderByClause,
        SearchCriteria searchCriteria,
        Map<SearchFilter, JdbiSearchCriteriaDescriptor> descriptors
    ) {
        Map<String, String> bindMap = new HashMap<>();
        List<String> whereParts = new ArrayList<>();

        for (Map.Entry<SearchFilter, JdbiSearchCriteriaDescriptor> descriptor : descriptors.entrySet()) {
            String value = searchCriteria.getFilterValue(descriptor.getKey());
            if (value != null) {
                whereParts.add(descriptor.getValue().whereClause());
                bindMap.put(descriptor.getValue().paramName(), value);
            }
        }

        String whereClause = String.join(" AND ", whereParts);
        if (!ObjectUtils.isEmpty(whereClause)) {
            whereClause = String.format("where %s", whereClause);
        }

        // Формируем финальный текст запроса со всеми условиями
        String finalQuery = String.format(
            "%s %s order by %s limit %d offset %d",
            baseQuery,
            whereClause,
            orderByClause,
            searchCriteria.getLimit(),
            searchCriteria.getOffset()
        );

        Query query = conn.createQuery(finalQuery);

        // Маппим параметры и возвращаем запрос
        return query.bindMap(bindMap);
    }
}
