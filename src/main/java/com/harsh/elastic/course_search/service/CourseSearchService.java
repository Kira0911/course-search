package com.harsh.elastic.course_search.service;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.stereotype.Service;

import com.harsh.elastic.course_search.document.CourseDocument;
import com.harsh.elastic.course_search.dto.SearchResponse;

@Service
public class CourseSearchService {

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    public SearchResponse searchCourses(
            String q,
            Integer minAge, Integer maxAge,
            String category, String type,
            Double minPrice, Double maxPrice,
            Instant startDate,
            String sort,
            int page, int size) {

        Criteria criteria = new Criteria(); // start with empty

        boolean hasAnyFilter = false;

        // auto complete logic
        // If the query is null or empty, we won't apply any keyword search criteria
        // This allows us to handle cases where the user might not input a search term
        // or when we want to show all courses without filtering by keyword.
        // If the query is not null or empty, we apply the keyword search criteria
        // based on the length of the query.
        // If the query length is 3 or more, we apply fuzzy, startsWith, and contains
        // criteria.
        // If the query length is less than 3, we only apply startsWith and contains
        // criteria.
        // This way, we can provide a more flexible search experience for the user.
        if (q != null && !q.isBlank()) {
            Criteria keywordCriteria;

            if (q.length() >= 3) {
                keywordCriteria = new Criteria().or(new Criteria("title").fuzzy(q))
                        .or(new Criteria("title").startsWith(q))
                        .or(new Criteria("title").contains(q))
                        .or(new Criteria("description").contains(q));
            } else {
                keywordCriteria = new Criteria().or(new Criteria("title").startsWith(q))
                        .or(new Criteria("description").contains(q));
            }

            criteria = criteria.and(keywordCriteria);
            hasAnyFilter = true;
        }

        if (category != null) {
            criteria = criteria.and("category").is(category);
            hasAnyFilter = true;
        }

        if (type != null) {
            criteria = criteria.and("type").is(type);
            hasAnyFilter = true;
        }

        if (minAge != null || maxAge != null) {
            Criteria ageCriteria = new Criteria("minAge");
            if (minAge != null)
                ageCriteria = ageCriteria.greaterThanEqual(minAge);
            if (maxAge != null)
                ageCriteria = ageCriteria.lessThanEqual(maxAge);
            criteria = criteria.and(ageCriteria);
            hasAnyFilter = true;
        }

        if (minPrice != null || maxPrice != null) {
            Criteria priceCriteria = new Criteria("price");
            if (minPrice != null)
                priceCriteria = priceCriteria.greaterThanEqual(minPrice);
            if (maxPrice != null)
                priceCriteria = priceCriteria.lessThanEqual(maxPrice);
            criteria = criteria.and(priceCriteria);
            hasAnyFilter = true;
        }

        // ✅ Add startDate filter
        if (startDate != null) {
            criteria = criteria.and("nextSessionDate").greaterThanEqual(startDate);
            hasAnyFilter = true;
        }

        // fallback if no filter — use match_all
        if (!hasAnyFilter) {
            criteria = new Criteria(); // match all documents
        }

        // Sorting
        Sort sortBy = Sort.by("nextSessionDate").ascending();
        if ("priceAsc".equalsIgnoreCase(sort)) {
            sortBy = Sort.by("price").ascending();
        } else if ("priceDesc".equalsIgnoreCase(sort)) {
            sortBy = Sort.by("price").descending();
        }

        PageRequest pageRequest = PageRequest.of(page, size, sortBy);

        CriteriaQuery query = new CriteriaQuery(criteria, pageRequest);

        SearchHits<CourseDocument> searchHits = elasticsearchTemplate.search(query, CourseDocument.class);

        List<CourseDocument> courseList = searchHits.stream().map(SearchHit::getContent).collect(Collectors.toList());

        long totalHits = searchHits.getTotalHits();

        return new SearchResponse(totalHits, courseList);
    }
}
