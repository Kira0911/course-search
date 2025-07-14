package com.harsh.elastic.course_search.document;

import java.time.Instant;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(indexName = "courses")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CourseDocument {

    @Id
    private String id;

    private String title;
    private String description;
    private String category;
    private String type;
    private String gradeRange;
    private int minAge;
    private int maxAge;
    private double price;

    @Field(type = FieldType.Date, format = DateFormat.date_time)
    private Instant nextSessionDate;

}
