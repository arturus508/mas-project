package com.example.healthfitness.model;

/**
 * Enumeration of supported units for {@link FoodItem}. Using an enum
 * enforces a fixed set of allowed values and prevents typos.
 */
public enum Unit {
    /**
     * Grams. Quantities will be interpreted as grams per 100 units when calculating macros.
     */
    G,

    /**
     * Piece (whole item). Quantities represent whole pieces of the food item.
     */
    PIECE
}