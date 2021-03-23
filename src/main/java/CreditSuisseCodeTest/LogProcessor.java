package CreditSuisseCodeTest;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Scanner;

import org.json.*;
import org.slf4j.Logger;

public class LogProcessor {

  Logger logger = org.slf4j.LoggerFactory.getLogger("Gradle.test.project.ReadJson");
  
  public void processLog(String filePath) {

    HashMap<String, Event> eventMap = readLog(filePath);
    saveEvents(eventMap);
  }
  
  private HashMap<String, Event> readLog(String filePath) {
    InputStream inputStream = null;
    try {
      logger.info("Reading from file: "+ filePath);
      inputStream = new FileInputStream(filePath);
    } catch (FileNotFoundException e) {
      logger.error("File not found:", e);
      return null;
    }
    
    HashMap<String, LogEntry> logEntryMap = new HashMap<String, LogEntry>();
    HashMap<String, Event> eventMap = new HashMap<String, Event>();

    try (Scanner fileScanner = new Scanner(inputStream, StandardCharsets.UTF_8.name())) {
      while (fileScanner.hasNextLine()) {
        String logEntry = fileScanner.nextLine();
        JSONObject jsonLogEntry = new JSONObject(logEntry);
        String id = jsonLogEntry.getString("id");
        String state = jsonLogEntry.getString("state");
        long timestamp = jsonLogEntry.getLong("timestamp");
        String type = jsonLogEntry.optString("type");
        String host = jsonLogEntry.optString("host");

        if (logger.isDebugEnabled()) {
          logger.debug("id: " + id +
              ", state: " + state + 
              ", timestamp: " + timestamp + 
              ", type: " + type + 
              ", host: " + host);
        }

        LogEntry firstEntry = logEntryMap.get(id);
        logger.debug(id + firstEntry);
        if (firstEntry != null) {
          long startTime = 0L;
          long finishTime = 0L;
          if (state.equals("STARTED")) {
            startTime = timestamp;
            finishTime = firstEntry.getTimestamp();
          } else {
            startTime = firstEntry.getTimestamp();
            finishTime = timestamp;
          }
          long durationMs = finishTime - startTime;
          logger.debug(finishTime + " - " + startTime + ": " + durationMs);
          boolean alertEvent = false;
          if (durationMs > 4) {
            alertEvent = true;
            if (logger.isDebugEnabled()) {
              logger.debug("Finished event with alert: " + id + " " + durationMs + "ms ");
            }
          } else if (logger.isDebugEnabled()) {
            logger.debug("Finished event: " + id + " " + durationMs + "ms ");
          }
          Event newEvent = new Event(id, alertEvent, durationMs, type, host);
          eventMap.put(id, newEvent);
          logEntryMap.remove(id);
        } else {
          LogEntry newEntry = new LogEntry(id, state, timestamp, type, host);
          logEntryMap.put(id, newEntry);
        }
      }
    }
    logger.info("Finished reading from file");
    return eventMap;
  }
  
  private void saveEvents(HashMap<String, Event> eventMap) {
    logger.info("Saving events to DB");
    Connection connection = null;
    PreparedStatement ps = null;
    try {
      connection = DBHelper.connect("ifexists=true");
      String sql = "INSERT INTO EVENT " +
          "VALUES (?, ?, ?, ?, ?)";
      ps = connection.prepareStatement(sql);

      for (Event event : eventMap.values()) {
        try {
          ps.setString(1, event.getId());
          ps.setString(2, event.getType());
          ps.setString(3, event.getHost());
          ps.setLong(4, event.getDuration());
          ps.setBoolean(5, event.getAlert());        
          ps.executeUpdate();
        } catch (SQLException e) {
          logger.error("Error saving to DB", e);
        }
      }
      logger.info("Events saved to DB");
    } catch (SQLException e) {
      logger.error("Error saving to DB", e);
    } finally {

      if (ps != null) {
        try {
          ps.close();
        } catch (SQLException e) {          
          logger.debug("Error closing DB", e);
        }
      }
      if (connection != null) {
        try {
          connection.close();
          logger.debug("DB Connection closed");

        } catch (SQLException e) {
          logger.debug("Error closing DB", e);
        }
      }
    }
  }

}
