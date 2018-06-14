package digital.cookbook.mkk;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.sql.*;

/**
 * Class providing with building up connection to DB and closing connection
 * 
 * @author Zifeng Zhang
 * @version 06.01.2018 
 */
public class DBUtil {
	private static String username;
	private static String password;
	private static String driver;
	private static String url;
	private static Connection connection;
	
	/**
	 * static statement for fetching the info needed to set up connection from
	 * properties file
	 */
	static {
		Properties prop = new Properties();

		try {
			InputStream inputStream = new FileInputStream("src/configure.properties");
			prop.load(inputStream);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

        driver = prop.getProperty("driver");
        url = prop.getProperty("url");
        username = prop.getProperty("username");
        password = prop.getProperty("password");
        
		try {
			Class.forName(driver);
			connection = DriverManager.getConnection(url, username, password);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static Connection getConnection() {
		return connection;
	}
	
	/**
	 * Close given connection to the database
	 * @param connection
	 */
	public static void close() {
		try {
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
