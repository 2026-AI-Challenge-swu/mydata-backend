package com.swu2026.mydata_backend.repository;

import com.swu2026.mydata_backend.domain.InvestmentProfileType;
import com.swu2026.mydata_backend.domain.PortfolioTemplate;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PortfolioTemplateRepository extends MongoRepository<PortfolioTemplate, String> {

    Optional<PortfolioTemplate> findByProfileType(InvestmentProfileType profileType);
}
