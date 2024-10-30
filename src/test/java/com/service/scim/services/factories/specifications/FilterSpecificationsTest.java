package com.service.scim.services.factories.specifications;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import com.service.scim.services.specifications.FilterSpecifications;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.jpa.domain.Specification;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.util.HashMap;
import java.util.Map;

public class FilterSpecificationsTest {

    private Root<Object> root;
    private CriteriaQuery<?> query;
    private CriteriaBuilder criteriaBuilder;

    @BeforeEach
    void setUp() {
        root = mock(Root.class);
        query = mock(CriteriaQuery.class);
        criteriaBuilder = mock(CriteriaBuilder.class);
    }

    @Test
    void createSpecificationWithActiveTrue() {
        Map<String, String> params = new HashMap<>();
        params.put("filter", "active eq \"true\"");

        when(criteriaBuilder.and(any())).thenReturn(mock(Predicate.class));
        Specification<Object> specification = FilterSpecifications.createSpecification(params);
        Predicate predicate = specification.toPredicate(root, query, criteriaBuilder);

        assertNotNull(predicate);
        verify(criteriaBuilder, times(1)).equal(root.get("active"), true);
    }

    @Test
    void createSpecificationWithActiveFalse() {
        Map<String, String> params = new HashMap<>();
        params.put("filter", "active eq \"false\"");

        when(criteriaBuilder.and(any())).thenReturn(mock(Predicate.class));

        Specification<Object> specification = FilterSpecifications.createSpecification(params);
        Predicate predicate = specification.toPredicate(root, query, criteriaBuilder);

        assertNotNull(predicate);
        verify(criteriaBuilder, times(1)).equal(root.get("active"), false);
    }

    @Test
    void createSpecificationWithEqualOperator() {
        Map<String, String> params = new HashMap<>();
        params.put("filter", "name eq \"John\"");

        when(criteriaBuilder.and(any())).thenReturn(mock(Predicate.class));

        Specification<Object> specification = FilterSpecifications.createSpecification(params);
        Predicate predicate = specification.toPredicate(root, query, criteriaBuilder);

        assertNotNull(predicate);
        verify(criteriaBuilder, times(1)).equal(root.get("name"), "John");
    }

    @Test
    void createSpecificationWithContainsOperator() {
        Map<String, String> params = new HashMap<>();
        params.put("filter", "name co \"John\"");

        when(criteriaBuilder.and(any())).thenReturn(mock(Predicate.class));

        Specification<Object> specification = FilterSpecifications.createSpecification(params);
        Predicate predicate = specification.toPredicate(root, query, criteriaBuilder);

        assertNotNull(predicate);
        verify(criteriaBuilder, times(1)).like(root.get("name"), "%John%");
    }

    @Test
    void createSpecificationWithInvalidFilter() {
        Map<String, String> params = new HashMap<>();
        params.put("filter", "invalid filter");

        Specification<Object> specification = FilterSpecifications.createSpecification(params);
        Predicate predicate = specification.toPredicate(root, query, criteriaBuilder);

        assertNull(predicate);
    }

    @Test
    void createSpecificationWithNullFilter() {
        Map<String, String> params = new HashMap<>();
        params.put("filter", null);

        Specification<Object> specification = FilterSpecifications.createSpecification(params);
        Predicate predicate = specification.toPredicate(root, query, criteriaBuilder);

        assertNull(predicate);
        verify(criteriaBuilder, never()).equal(any(), any());
        verify(criteriaBuilder, never()).like(any(), anyString());
    }
}