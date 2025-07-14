package com.harsh.elastic.course_search.config;

import java.io.InputStream;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.harsh.elastic.course_search.document.CourseDocument;
import com.harsh.elastic.course_search.repository.CourseRepository;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private CourseRepository courseRepository;

    @Override
    public void run(String... args) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.findAndRegisterModules();
        InputStream inputStream = getClass().getResourceAsStream("/sample-courses.json");

        if (inputStream == null) {
            throw new RuntimeException("sample-courses.json not found in resources!");
        }

        List<CourseDocument> courses = mapper.readValue(inputStream, new TypeReference<>() {
        });
        courseRepository.saveAll(courses);

        System.out.println("✅ Indexed " + courses.size() + " courses into Elasticsearch");
    }
}