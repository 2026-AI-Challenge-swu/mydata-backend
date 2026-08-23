package com.swu2026.mydata_backend.repository;

import com.swu2026.mydata_backend.domain.Question;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface QuestionRepository extends MongoRepository<Question, String> {

    List<Question> findAllByOrderByDisplayOrderAsc();
}
