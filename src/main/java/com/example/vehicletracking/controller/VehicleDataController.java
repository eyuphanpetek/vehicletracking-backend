package com.example.vehicletracking.controller;

import com.example.vehicletracking.model.VehicleData;
import com.example.vehicletracking.service.VehicleDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.CrossOrigin;
import java.util.List;

@RestController
@RequestMapping("/api/vehicle_data")
@CrossOrigin(origins = "http://localhost:4200")  // Allow your frontend URL
public class VehicleDataController {

    private final VehicleDataService service;

    @Autowired
    public VehicleDataController(VehicleDataService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<VehicleData> createVehicleData(@RequestBody VehicleData vehicleData) {
        VehicleData savedData = service.saveVehicleData(vehicleData);
        return ResponseEntity.ok(savedData);
    }

    @GetMapping("/search")
    public ResponseEntity<List<VehicleData>> searchByText(@RequestParam String searchText) {
        List<VehicleData> results = service.searchByText(searchText);
        return ResponseEntity.ok(results);
    }

    @GetMapping
    public ResponseEntity<List<VehicleData>> getAllVehicleData() {
        List<VehicleData> vehicleDataList = service.getAllVehicleData();
        return ResponseEntity.ok(vehicleDataList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<VehicleData> getVehicleDataById(@PathVariable String id) {
        VehicleData vehicleData = service.getVehicleDataById(id);
        if (vehicleData != null) {
            return ResponseEntity.ok(vehicleData);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/radiationLevelRange")
    public ResponseEntity<List<VehicleData>> getVehiclesByRadiationLevelRange(@RequestParam double prePassageRadiationLevel, @RequestParam double postPassageRadiationLevel) {
        List<VehicleData> vehicleDataList = service.findVehiclesByRadiationLevelRange(prePassageRadiationLevel, postPassageRadiationLevel);
        return ResponseEntity.ok(vehicleDataList);
    }

    @GetMapping("/organizationNameStartsWith/{prefix}")
    public ResponseEntity<List<VehicleData>> getVehiclesByOrganizationNameStartingWith(@PathVariable String prefix) {
        List<VehicleData> vehicleDataList = service.findVehiclesByOrganizationNameStartingWith(prefix);
        return ResponseEntity.ok(vehicleDataList);
    }

    @GetMapping("/radiationLevelRanges")
    public ResponseEntity<List<VehicleData>> getVehiclesByRadiationLevelRanges(
            @RequestParam double prePassageMin,
            @RequestParam double prePassageMax,
            @RequestParam double postPassageMin,
            @RequestParam double postPassageMax) {
        List<VehicleData> vehicles = service.findVehiclesByRadiationLevelRanges(
                prePassageMin, prePassageMax, postPassageMin, postPassageMax);
        return ResponseEntity.ok(vehicles);
    }

    @GetMapping("/criteria")
    public ResponseEntity<List<VehicleData>> getVehiclesByCriteria(
            @RequestParam double prePassageMin,
            @RequestParam double prePassageMax,
            @RequestParam double postPassageMin,
            @RequestParam double postPassageMax,
            @RequestParam(required = false) String orgNamePrefix,
            @RequestParam(required = false) String orgNameSuffix) {
        List<VehicleData> vehicles = service.findVehiclesByCriteria(
                prePassageMin, prePassageMax, postPassageMin, postPassageMax, orgNamePrefix, orgNameSuffix);
        return ResponseEntity.ok(vehicles);
    }

    @PutMapping("/{id}/radiationLevel")
    public ResponseEntity<VehicleData> updateRadiationLevel(@PathVariable String id, @RequestParam double radiationLevel) {
        VehicleData updatedVehicleData = service.updateRadiationLevel(id, radiationLevel);
        if (updatedVehicleData != null) {
            return ResponseEntity.ok(updatedVehicleData);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVehicleDataById(@PathVariable String id) {
        service.deleteVehicleDataById(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/licensePlate/{licensePlate}")
    public ResponseEntity<Void> deleteVehicleDataByLicensePlate(@PathVariable String licensePlate) {
        service.deleteVehicleDataByLicensePlate(licensePlate);
        return ResponseEntity.noContent().build();
    }
}