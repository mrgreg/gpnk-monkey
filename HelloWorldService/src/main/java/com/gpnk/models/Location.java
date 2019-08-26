package com.gpnk.models;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Location {
    private String zipCode;
    private double latitude;
    private double longitude;
}
