package CreditSuisseCodeTest;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DBHelper {

	private static Logger logger = LoggerFactory.getLogger("Gradle.test.project.DBConnection");

	public static Connection connect() {
		return connect(null);
	}

	/**
	 * Returns a DB connection.
	 * @param properties
	 * @return
	 */
	public static Connection connect(String properties) {
	  InputStream input = null;
		try {
			input = new FileInputStream("database.properties");

			Properties prop = new Properties();

			prop.load(input);

			String URL = prop.getProperty("database.url");
			String user = prop.getProperty("database.user");
			String password = prop.getProperty("database.password");
			if (properties != null) {
			  URL = URL + properties;
			}
			logger.debug("Connecting to DB " + URL);

			return DriverManager.getConnection(URL, user, password);
		} catch (SQLException e) {
			logger.error("Error connecting to DB", e);
		} catch (FileNotFoundException e) {
			logger.error("File Not Found", e);
		} catch (IOException e) {
			logger.error("IOException", e);
		} finally {
		  if (input != null) {
		    try {
		      input.close();
		    } catch (IOException e) {
		    
		    }
		  }
		}
		return null;
	}

	/**
	 * Creates the DB and tables if it does not exist
	 * @throws SQLException
	 */
	public static void createDatabase() throws SQLException {
	  logger.info("Creating or updating database");
	  Connection dbConnection = null;
	  Statement stmt = null;

	  try {
	    dbConnection = connect();
	    stmt = dbConnection.createStatement();

	    String sql = "CREATE TABLE IF NOT EXISTS EVENT " +
	        "(id VARCHAR(255) not NULL, " + 
	        " type VARCHAR(255), " +
	        " host VARCHAR(255), " + 
	        " duration INTEGER, " + 
	        " alert boolean, " + 
	        " PRIMARY KEY ( id ))";

	    stmt.executeUpdate(sql);
	    logger.info("Database updated");
	  } catch (Exception e) {
	    logger.error("Error creating DB tables", e);
	    throw e;
	  } finally {

	    try {
	      if (stmt != null) {
	        stmt.close();
	      }
	    } catch (Exception e) {
	      logger.error("Error closing DB statement", e);
	      throw e;
	    } 
	    if (dbConnection != null) {
	      try {
	        dbConnection.close();
	      } catch (SQLException e) {
	        logger.error("Error closing DB", e);
	        throw e;
	      }
	    }
	  }
	 }
	
	/**
	 * Drops the database
	 */
  public static void dropDatabase() throws SQLException {
    logger.info("Dropping database");
    Connection dbConnection = null;
    Statement stmt = null;

    try {
      dbConnection = connect();
      stmt = dbConnection.createStatement();

      String sql = "DROP SCHEMA PUBLIC CASCADE";

      stmt.executeUpdate(sql);
      logger.info("Database drop OK");
    } catch (Exception e) {
      logger.error("Error dropping DB tables", e);
      throw e;
    } finally {

      try {
        if (stmt != null) {
          stmt.close();
        }
      } catch (Exception e) {
        logger.error("Error closing DB statement", e);
        throw e;
      } 
      if (dbConnection != null) {
        try {
          dbConnection.close();
        } catch (SQLException e) {
          logger.error("Error closing DB", e);
          throw e;
        }
      }
    }
  }
  
  /**
   * 
   * Reads and logs the contents of the database
   * @throws SQLException
   */
  public static void readDatabase() throws SQLException {
    logger.info("Reading database");
    Connection dbConnection = null;
    Statement stmt = null;

    try {
      dbConnection = connect("ifexists=true");
      stmt = dbConnection.createStatement();

      String sql = "SELECT * FROM EVENT";

      ResultSet results = stmt.executeQuery(sql);
      
      while (results.next()) {
        String id = results.getString("id");
        boolean alert = results.getBoolean("alert");
        long duration = results.getLong("duration");
        String type = results.getString("type");
        String host = results.getString("host");
        logger.info("Event id: " + id +
            ", alert: " + alert + 
            ", duration: " + duration + 
            ", type: " + type + 
            ", host: " + host);
      }
      logger.info("Database read");
    } catch (Exception e) {
      logger.error("Error reading DB tables", e);
      throw e;
    } finally {

      try {
        if (stmt != null) {
          stmt.close();
        }
      } catch (Exception e) {
        logger.error("Error closing DB statement", e);
        throw e;
      } 
      if (dbConnection != null) {
        try {
          dbConnection.close();
        } catch (SQLException e) {
          logger.error("Error closing DB", e);
          throw e;
        }
      }
    }
   }

}
