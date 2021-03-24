package dev.chenjr.attendance;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.sql.SQLException;

@Component
public class DatabaseInitializer {

	@Autowired
	DataSource dataSource;

//	@PostConstruct
//	public void init() throws SQLException {
//		try (var conn = dataSource.getConnection()) {
//			try (var stmt = conn.createStatement()) {
//				stmt.executeUpdate("CREATE TABLE IF NOT EXISTS users (" //
//						+ "id BIGINT IDENTITY NOT NULL PRIMARY KEY, " //
//						+ "email VARCHAR(100) NOT NULL, " //
//						+ "password VARCHAR(100) NOT NULL, " //
//						+ "name VARCHAR(100) NOT NULL, " //
//						+ "createdAt BIGINT NOT NULL, " //
//						+ "UNIQUE (email))");
//			}
//		}
//	}
}
