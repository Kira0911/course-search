package com.harsh.elastic.course_search.controller;

import java.io.IOException;
import java.time.Instant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.harsh.elastic.course_search.service.CourseSearchService;

@RestController
@RequestMapping("/api/search")
public class CourseSearchController {

    @Autowired
    private CourseSearchService courseSearchService;

    @GetMapping
    public com.harsh.elastic.course_search.dto.SearchResponse searchCourses(
            @RequestParam(required = false) String q,
            @RequestParam(required = false) Integer minAge,
            @RequestParam(required = false) Integer maxAge,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant startDate, // THIS
            @RequestParam(required = false) String sort,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) throws IOException {
        return courseSearchService.searchCourses(q, minAge, maxAge, category, type, minPrice, maxPrice, startDate, sort,
                page,
                size);
    }
}