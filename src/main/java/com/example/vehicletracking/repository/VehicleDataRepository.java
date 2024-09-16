package com.example.vehicletracking.repository;

import com.example.vehicletracking.model.VehicleData;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VehicleDataRepository extends MongoRepository<VehicleData, String> {
    public void deleteByLicensePlate(String licensePlate);
   // public
    // Custom query methods (if needed) can be defined here
}
