package org.example.ubkie.Database;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import org.example.ubkie.GetAndSet.*;

import java.io.InputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

/**
 * 提供與資料庫交互的實用工具方法。
 */
public class DatabaseUtils {
    private static final String URL = "jdbc:mysql://localhost:3306/ubike";
    private static final String USER = "root";
    private static final String PASSWORD = "Ho111030026";

    /**
     * 獲取資料庫連接。
     *
     * @return 資料庫連接
     * @throws SQLException 如果獲取連接失敗
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    /**
     * 將JSON數據同步到資料庫。
     *
     * @param resourceStream JSON數據流
     * @param tableName      資料庫表名
     */
    public static void syncJsonToDatabase(InputStream resourceStream, String tableName) {
        try (Connection connection = getConnection()) {
            ObjectMapper objectMapper = new ObjectMapper();
            List<?> items = objectMapper.readValue(resourceStream, new TypeReference<List<?>>() {
                @Override
                public Type getType() {
                    switch (tableName) {
                        case "member_id":
                            return objectMapper.getTypeFactory().constructCollectionType(List.class, Member.class);
                        case "dock_data":
                            return objectMapper.getTypeFactory().constructCollectionType(List.class, Dock.class);
                        case "history":
                            return objectMapper.getTypeFactory().constructCollectionType(List.class, History.class);
                        case "ubike_data":
                            return objectMapper.getTypeFactory().constructCollectionType(List.class, Vehicle.class);
                        case "yoyo_history":
                            return objectMapper.getTypeFactory().constructCollectionType(List.class, YoyoHistory.class);
                        case "taipei":
                        case "newtaipei":
                            return objectMapper.getTypeFactory().constructCollectionType(List.class, Station.class);
                        default:
                            return super.getType();
                    }
                }
            });
            executeInsert(items, tableName, connection);
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 執行插入操作。
     *
     * @param items      要插入的項目列表
     * @param tableName  資料庫表名
     * @param connection 資料庫連接
     * @throws SQLException 如果插入操作失敗
     */
    public static void executeInsert(List<?> items, String tableName, Connection connection) throws SQLException {
        String insertQuery = "";
        switch (tableName) {
            case "member_id":
                insertQuery = "INSERT INTO member_id (MemberID, Member_password, id_number, email, YOYO_Card, YOYO_ID, rent_status) VALUES (?, ?, ?, ?, ?, ?, ?)";
                break;
            case "dock_data":
                insertQuery = "INSERT INTO dock_data (Status, BikeUID, DockUID, StationUID, MaintenanceInfo) VALUES (?, ?, ?, ?, ?)";
                break;
            case "history":
                insertQuery = "INSERT INTO history (rent_time, return_time, rent_fee, rent_place, return_place, rent_BikeUID, MemberID) VALUES (?, ?, ?, ?, ?, ?, ?)";
                break;
            case "ubike_data":
                insertQuery = "INSERT INTO ubike_data (Status, Type, BikeUID, AuthorityID, DockUID, StationUID, MaintenanceInfo) VALUES (?, ?, ?, ?, ?, ?, ?)";
                break;
            case "yoyo_history":
                insertQuery = "INSERT INTO yoyo_history (MemberID, transaction, action, time) VALUES (?, ?, ?, ?)";
                break;
            case "taipei":
            case "newtaipei":
                insertQuery = "INSERT INTO " + tableName + " (StationUID, StationID, AuthorityID, StationName_Zh_tw, StationName_En, PositionLon, PositionLat, GeoHash, StationAddress_Zh_tw, StationAddress_En, BikesCapacity, bike_available) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                break;
        }

        try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
            for (Object item : items) {
                if (item instanceof Member) {
                    Member member = (Member) item;
                    preparedStatement.setString(1, member.getMemberID());
                    preparedStatement.setString(2, member.getMember_password());
                    preparedStatement.setString(3, member.getId_number());
                    preparedStatement.setString(4, member.getEmail());
                    preparedStatement.setString(5, member.getYOYO_Card());
                    preparedStatement.setString(6, member.getYOYO_ID());
                    preparedStatement.setString(7, member.getRent_status());
                } else if (item instanceof Dock) {
                    Dock dock = (Dock) item;
                    preparedStatement.setString(1, dock.getStatus());
                    preparedStatement.setString(2, dock.getBikeUID());
                    preparedStatement.setString(3, dock.getDockUID());
                    preparedStatement.setString(4, dock.getStationUID());
                    preparedStatement.setString(5, dock.getMaintenanceInfo());
                } else if (item instanceof History) {
                    History history = (History) item;
                    preparedStatement.setString(1, history.getRent_time());
                    preparedStatement.setString(2, history.getReturn_time());
                    preparedStatement.setInt(3, history.getRent_fee());
                    preparedStatement.setString(4, history.getRent_place());
                    preparedStatement.setString(5, history.getReturn_place());
                    preparedStatement.setString(6, history.getRent_BikeUID());
                    preparedStatement.setString(7, history.getMemberID());
                } else if (item instanceof Vehicle) {
                    Vehicle vehicle = (Vehicle) item;
                    preparedStatement.setString(1, vehicle.getStatus());
                    preparedStatement.setString(2, vehicle.getType());
                    preparedStatement.setString(3, vehicle.getBikeUID());
                    preparedStatement.setString(4, vehicle.getAuthorityID());
                    preparedStatement.setString(5, vehicle.getDockUID());
                    preparedStatement.setString(6, vehicle.getStationUID());
                    preparedStatement.setString(7, vehicle.getMaintenanceInfo());
                } else if (item instanceof YoyoHistory) {
                    YoyoHistory yoyoHistory = (YoyoHistory) item;
                    preparedStatement.setString(1, yoyoHistory.getMemberID());
                    preparedStatement.setString(2, yoyoHistory.getTransaction());
                    preparedStatement.setString(3, yoyoHistory.getAction());
                    preparedStatement.setString(4, yoyoHistory.getTime());
                } else if (item instanceof Station) {
                    Station station = (Station) item;
                    preparedStatement.setString(1, station.getStationUID());
                    preparedStatement.setString(2, station.getStationID());
                    preparedStatement.setString(3, station.getAuthorityID());
                    preparedStatement.setString(4, station.getStationName().getZh_tw());
                    preparedStatement.setString(5, station.getStationName().getEn());
                    preparedStatement.setDouble(6, station.getStationPosition().getPositionLon());
                    preparedStatement.setDouble(7, station.getStationPosition().getPositionLat());
                    preparedStatement.setString(8, station.getStationPosition().getGeoHash());
                    preparedStatement.setString(9, station.getStationAddress().getZh_tw());
                    preparedStatement.setString(10, station.getStationAddress().getEn());
                    preparedStatement.setInt(11, station.getBikesCapacity());
                    preparedStatement.setInt(12, station.getBike_available());
                }
                preparedStatement.addBatch();
            }
            preparedStatement.executeBatch();
        }
    }
}
