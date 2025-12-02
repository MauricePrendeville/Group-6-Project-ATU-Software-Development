package com.hotel.Model;

/**
 * Enum describing different Room Types
 */
public enum RoomType {
    SINGLE,
    DOUBLE,
    DELUXE,
    SUITE,
    FAMILY,
    PRESIDENTIAL;

    /**
     * Formats the RoomType into a readable format with the first letter capitalised.
     * @return RoomType
     */
    @Override
    public String toString() {
        // Format enum values like "Single" instead of "SINGLE"
        String name = name().toLowerCase();
        return Character.toUpperCase(name.charAt(0)) + name.substring(1);
    }
}