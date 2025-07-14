package com.harsh.elastic.course_search.repository;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import com.harsh.elastic.course_search.document.CourseDocument;

public interface CourseRepository extends ElasticsearchRepository<CourseDocument, String> {

}
