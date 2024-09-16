package com.example.vehicletracking.service;

//annotationları yaz
//Relational database import
//spring data JPA
//normalization yok
//angular öğren. angular.io node.js
//primeng

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import com.example.vehicletracking.model.VehicleData;
import com.example.vehicletracking.repository.VehicleDataRepository;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Service
@Slf4j
public class VehicleDataService {

    private final VehicleDataRepository repository;
    private final MongoTemplate mongoTemplate;

    @Autowired
    public VehicleDataService(VehicleDataRepository repository, MongoTemplate mongoTemplate) {
        this.repository = repository;
        this.mongoTemplate = mongoTemplate;
    }

    public VehicleData saveVehicleData(VehicleData vehicleData) {
        System.out.println(vehicleData.getLicensePlate());
        log.info(vehicleData.getLicensePlate());
        vehicleData = repository.save(vehicleData);
        System.out.println(vehicleData.getId());
        log.info(vehicleData.toString());
        return vehicleData;
    }

    public List<VehicleData> searchByText(String searchText) {
        Query query = new Query();
        query.addCriteria(Criteria.where("$text").is(new Document("$search", searchText)));
        return mongoTemplate.find(query, VehicleData.class);
    }

    public List<VehicleData> getAllVehicleData() {
        return repository.findAll();
    }

    public VehicleData getVehicleDataById(String id) {
        return repository.findById(id).orElse(null);
    }

    public void deleteVehicleDataById(String id) {
        repository.deleteById(id);
        log.info(id);
    }
    public void deleteVehicleDataByLicensePlate(String licensePlate){
        repository.deleteByLicensePlate(licensePlate);
        log.info(licensePlate);
    }

    public VehicleData updateRadiationLevel(String id, double radiationLevel) {
        VehicleData vehicleData = repository.findById(id).orElse(null);
        if (vehicleData != null) {
            vehicleData.setRadiationLevel(radiationLevel);
            repository.save(vehicleData);
            log.info("Updated radiation level for vehicle with ID: {}", id);
            return vehicleData;
        } else {
            log.warn("Vehicle with ID: {} not found", id);
            return null;
        }
    }
    public List<VehicleData> findVehiclesByRadiationLevelRange(double prePassageRadiationLevel, double postPassageRadiationLevel) {
        Query query = new Query();
        query.addCriteria(Criteria.where("prePassageRadiationLevel").gte(prePassageRadiationLevel).lte(postPassageRadiationLevel));
        return mongoTemplate.find(query, VehicleData.class);
    }

    public List<VehicleData> findVehiclesByOrganizationNameStartingWith(String prefix) {
        Query query = new Query();
        query.addCriteria(Criteria.where("organizationName").regex("^" + prefix));
        return mongoTemplate.find(query, VehicleData.class);
    }

    public List<VehicleData> findVehiclesByRadiationLevelRanges(
            double prePassageMin, double prePassageMax,
            double postPassageMin, double postPassageMax) {
        Query query = new Query();
        query.addCriteria(Criteria.where("prePassageRadiationLevel").gte(prePassageMin).lte(prePassageMax));
        query.addCriteria(Criteria.where("postPassageRadiationLevel").gte(postPassageMin).lte(postPassageMax));
        return mongoTemplate.find(query, VehicleData.class);
    }

    public List<VehicleData> findVehiclesByCriteria(
            double prePassageMin, double prePassageMax,
            double postPassageMin, double postPassageMax,
            String orgNamePrefix, String orgNameSuffix) {

        Query query = new Query();

        try {
            query.addCriteria(Criteria.where("prePassageRadiationLevel").gte(prePassageMin).lte(prePassageMax));
            query.addCriteria(Criteria.where("postPassageRadiationLevel").gte(postPassageMin).lte(postPassageMax));
            String regexPatternPre = "";
            String regexPatternPost = "";
            if (orgNamePrefix != null && !orgNamePrefix.isEmpty()) {
                regexPatternPre += "^" + orgNamePrefix;
            }
            if (orgNameSuffix != null && !orgNameSuffix.isEmpty()) {
                regexPatternPost += orgNameSuffix + "$";
            }
            if (!regexPatternPre.isEmpty() && !regexPatternPost.isEmpty()) {
                query.addCriteria(Criteria.where("organizationName").regex(regexPatternPre).regex(regexPatternPost));
            } else if (!regexPatternPre.isEmpty()) {
                query.addCriteria(Criteria.where("organizationName").regex(regexPatternPre));
            }else {
                query.addCriteria(Criteria.where("organizationName").regex(regexPatternPost));
            }
            return mongoTemplate.find(query, VehicleData.class);
        } catch (Exception e) {
            log.error("Error executing query", e);
            throw e;
        }
    }
}