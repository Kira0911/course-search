package com.harsh.elastic.course_search.dto;

import java.util.List;
import com.harsh.elastic.course_search.document.CourseDocument;

public class SearchResponse {
    private long total;
    private List<CourseDocument> courses;

    public SearchResponse(long total, List<CourseDocument> courses) {
        this.total = total;
        this.courses = courses;
    }

    public long getTotal() {
        return total;
    }

    public List<CourseDocument> getCourses() {
        return courses;
    }
}
