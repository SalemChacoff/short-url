package com.java.admin.repository.url.specification;

import com.java.admin.dto.url.request.GetUrlsRequestDto;
import com.java.admin.entity.url.UrlEntity;
import com.java.admin.entity.url.UrlEntity_;
import com.java.admin.entity.user.UserEntity_;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.NonNull;

import java.io.Serial;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

public class UrlSpecification implements Specification<UrlEntity> {

    @Serial
    private static final long serialVersionUID = 4276451895291483569L;

    private final transient GetUrlsRequestDto criteria;

    public UrlSpecification(GetUrlsRequestDto criteria) {
        this.criteria = criteria;
    }

    @Override
    public Predicate toPredicate(
            Root<UrlEntity> root,
            CriteriaQuery<?> query,
            @NonNull CriteriaBuilder criteriaBuilder) {

        Path<Long> userId = root.get(UrlEntity_.USER).get(UserEntity_.ID);
        Path<String> customAlias = root.get(UrlEntity_.CUSTOM_ALIAS);
        Path<String> originalUrl = root.get(UrlEntity_.ORIGINAL_URL);
        Path<String> shortUrl = root.get(UrlEntity_.SHORT_URL);
        Path<String> description = root.get(UrlEntity_.DESCRIPTION);
        Path<Boolean> isActive = root.get(UrlEntity_.IS_ACTIVE);
        Path<OffsetDateTime> validSince = root.get(UrlEntity_.VALID_SINCE);
        Path<OffsetDateTime> validUntil = root.get(UrlEntity_.VALID_UNTIL);

        final List<Predicate> predicates = new ArrayList<>();

        if (criteria.userId() != null) {
            predicates.add(criteriaBuilder.equal(userId, criteria.userId()));
        }
        if (criteria.filterBy() != null && !criteria.filterBy().isBlank()
        && criteria.filterValue() != null && !criteria.filterValue().isBlank()) {
            switch (criteria.filterBy()) {
                case UrlEntity_.CUSTOM_ALIAS -> predicates.add(criteriaBuilder.like(customAlias, "%" + criteria.filterValue() + "%"));
                case UrlEntity_.ORIGINAL_URL -> predicates.add(criteriaBuilder.like(originalUrl, "%" + criteria.filterValue() + "%"));
                case UrlEntity_.DESCRIPTION -> predicates.add(criteriaBuilder.like(description, "%" + criteria.filterValue() + "%"));
                case UrlEntity_.SHORT_URL -> predicates.add(criteriaBuilder.like(shortUrl, "%" + criteria.filterValue() + "%"));
                default -> throw new IllegalArgumentException("Unsupported filterBy: " + criteria.filterBy());
            }
        }

        if (criteria.isActive() != null) {
            predicates.add(criteriaBuilder.equal(isActive, criteria.isActive()));
        }

        if (criteria.validSince() != null) {
            predicates.add(criteriaBuilder.greaterThanOrEqualTo(validSince, criteria.validSince()));
        }

        if (criteria.validUntil() != null) {
            predicates.add(criteriaBuilder.lessThanOrEqualTo(validUntil, criteria.validUntil()));
        }

        if (criteria.sortBy() != null && !criteria.sortBy().isBlank()) {
            if (criteria.ascending() != null && criteria.ascending()) {
                query.orderBy(criteriaBuilder.asc(root.get(criteria.sortBy())));
            } else {
                query.orderBy(criteriaBuilder.desc(root.get(criteria.sortBy())));
            }
        } else {
            query.orderBy(criteriaBuilder.desc(root.get(UrlEntity_.CREATED_AT)));
        }

        predicates.add(criteriaBuilder.isFalse(root.get(UrlEntity_.IS_DELETED)));

        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }
}
