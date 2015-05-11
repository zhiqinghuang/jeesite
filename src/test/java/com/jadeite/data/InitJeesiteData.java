package com.jadeite.data;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.dbunit.DatabaseUnitException;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.excel.XlsDataSet;
import org.dbunit.operation.DatabaseOperation;

import com.thinkgem.jeesite.common.config.Global;

public class InitJeesiteData {

	private static final String TEST_DATA_DIRECTORY = "D:/workspace/jeesiteStudy/jeesite/db/sys";
	private static final String DATA_FILE_NAME = "dbUnitTest.xls";

	public static void main(String[] args) {
		dataImportWithXLS();
	}

	public static void dataImportWithXLS() {
		try {
			IDatabaseConnection connection = getIDatabaseConnection();
			System.out.println("Schema : " + connection.getSchema());
			IDataSet partialDataSet = new XlsDataSet(new FileInputStream(new File("D:/workspace/jeesiteStudy/jeesite/db/sys/jeesite_data.xls")));
			DatabaseOperation.CLEAN_INSERT.execute(connection, partialDataSet);
			partialDataSet = new XlsDataSet(new FileInputStream(new File("D:/workspace/jeesiteStudy/jeesite/db/cms/jeesite_data.xls")));
			DatabaseOperation.CLEAN_INSERT.execute(connection, partialDataSet);
			partialDataSet = new XlsDataSet(new FileInputStream(new File("D:/workspace/jeesiteStudy/jeesite/db/oa/jeesite_data.xls")));
			DatabaseOperation.CLEAN_INSERT.execute(connection, partialDataSet);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (DatabaseUnitException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void dataExportWithXLS() {
		try {
			IDatabaseConnection connection = getIDatabaseConnection();
			System.out.println("Schema : " + connection.getSchema());
			String[] tableNames = { "USER_TABLE", "WIKI_USER" };
			for (int idx = 0; idx < tableNames.length; idx++) {
				System.out.println(tableNames[idx]);
			}
			IDataSet partialDataSet = connection.createDataSet(tableNames);
			XlsDataSet.write(partialDataSet, new FileOutputStream(new File(TEST_DATA_DIRECTORY + DATA_FILE_NAME)));
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (DatabaseUnitException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static IDatabaseConnection getIDatabaseConnection() {
		try {
			DatabaseProperties databaseProperties = getDatabaseProperties();
			Class.forName(databaseProperties.getDriverClassName());
			Connection jdbcConnection = DriverManager.getConnection(databaseProperties.getDataBaseUrl(), databaseProperties.getUserName(), databaseProperties.getPassword());
			IDatabaseConnection connection = new DatabaseConnection(jdbcConnection, databaseProperties.getSchema());
			return connection;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (DatabaseUnitException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static DatabaseProperties getDatabaseProperties() {
		String lstrJdbcType = Global.getConfig("jdbc.type");
		String lstrDriverClassName = Global.getConfig("jdbc.driver");
		String lstrDataBaseUrl = Global.getConfig("jdbc.url");
		String lstrUserName = Global.getConfig("jdbc.username");
		String lstrPassword = Global.getConfig("jdbc.password");
		String lstrSchema = Global.getConfig("jdbc.schema");
		DatabaseProperties databaseProperties = new DatabaseProperties();
		databaseProperties.setJdbcType(lstrJdbcType);
		databaseProperties.setDriverClassName(lstrDriverClassName);
		databaseProperties.setDataBaseUrl(lstrDataBaseUrl);
		databaseProperties.setUserName(lstrUserName);
		databaseProperties.setPassword(lstrPassword);
		databaseProperties.setSchema(lstrSchema);
		return databaseProperties;
	}
}