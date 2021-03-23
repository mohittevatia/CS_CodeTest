package CreditSuisseCodeTest;

/**
 * An entry (line) of the events log.
 * 
 * @author joel
 *
 */
public class LogEntry {

	private String id;
	private String state;
	private long timestamp;
	private String type;
	private String host;
	
	
	public LogEntry(String id, String state, long timestamp, String type, String host) {
		this.setId(id);
		this.setState(state);
		this.timestamp = timestamp;
		this.type = type;
		this.host = host;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}
	
}
