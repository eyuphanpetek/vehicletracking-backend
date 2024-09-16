package com.example.vehicletracking.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.index.TextIndexed;

import java.time.LocalDateTime;

@Document(collection = "vehicle_data")
@ToString
@Getter
@Setter
public class VehicleData {
    @Id
    private String id;
    private String licensePlate;
    @TextIndexed
    private String organizationName;
    private LocalDateTime passageDateTime;
    private double speed;
    private double measurementAccuracy;
    private double radiationLevel;
    private double prePassageRadiationLevel;
    private double postPassageRadiationLevel;
}

