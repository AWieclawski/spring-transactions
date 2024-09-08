package edu.awieclawski.enums;

import lombok.Getter;

/**
 * A unit of measurement, or unit of measure, is a definite magnitude of a quantity.
 */
@Getter
public enum UoM {
    PC("Pieces"),
    KG("Kilograms"),
    M("Meters"),
    M2("Square meters"),
    M3("Cubic meters");

    private final String description;

    UoM(String description) {
        this.description = description;
    }
}
