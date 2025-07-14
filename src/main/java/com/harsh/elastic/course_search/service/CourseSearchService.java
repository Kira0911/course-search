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

        if (q != null && !q.isBlank()) {

            // This give autocomplete like functionality
            // Matches titles starting with the query string or matches descriptions
            // containing the query string
            Criteria titlePrefix = new Criteria("title").startsWith(q);
            Criteria descMatch = new Criteria("description").matches(q);
            criteria = criteria.and(new Criteria().or(titlePrefix).or(descMatch));

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
