package com.koraspot.dao;

import com.koraspot.model.Location;
import com.koraspot.util.DatabaseUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class LocationDAO {

    public List<Location> listAllLocations() throws Exception {
        List<Location> locations = new ArrayList<>();
        String sql = "SELECT location_id, address, city, latitude, longitude FROM locations";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Location loc = new Location();
                loc.setLocationId(rs.getInt("location_id"));
                loc.setAddress(rs.getString("address"));
                loc.setCity(rs.getString("city"));
                loc.setLatitude(rs.getDouble("latitude"));
                loc.setLongitude(rs.getDouble("longitude"));
                locations.add(loc);
            }
        }
        return locations;
    }

    public Location getLocationById(int locationId) throws Exception {
        String sql = "SELECT location_id, address, city, latitude, longitude FROM locations WHERE location_id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, locationId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Location loc = new Location();
                    loc.setLocationId(rs.getInt("location_id"));
                    loc.setAddress(rs.getString("address"));
                    loc.setCity(rs.getString("city"));
                    loc.setLatitude(rs.getDouble("latitude"));
                    loc.setLongitude(rs.getDouble("longitude"));
                    return loc;
                }
            }
        }
        return null;
    }
}
