package CreditSuisseCodeTest;

import java.sql.SQLException;

import org.slf4j.Logger;

public class CodeTest {

	private static final String EVENTS_LOG = "events.log";

  public static void main(String[] args) {
		Logger logger = org.slf4j.LoggerFactory.getLogger("Gradle.test.project.GradleTest");

		logger.info("Starting GradleTest");

		String arg = args[0];

		if (arg.equals("dropDb")) {
		  try {
		    DBHelper.dropDatabase();
		  } catch (SQLException e) {
		    
		  }
		} else if (arg.equals("readDB")) {
		  try {
		    DBHelper.readDatabase();
      } catch (SQLException e) {

      }
		} else {
		  try {
		    DBHelper.createDatabase();
		  } catch (SQLException e) {
		    logger.error("Could not create database", e);
		    System.exit(1);
		  }

		  LogProcessor processor = new LogProcessor();
		  processor.processLog(EVENTS_LOG);
		}
  }

}
