package com.swu2026.mydata_backend.repository;

import com.swu2026.mydata_backend.domain.PersonaConnectionDocument;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PersonaConnectionRepository extends MongoRepository<PersonaConnectionDocument, String> {

    Optional<PersonaConnectionDocument> findByPersonaId(String personaId);
}
