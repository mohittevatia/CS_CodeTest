package CreditSuisseCodeTest;

public class Event {

	private String id;
	private boolean alert;
	private long duration;
	private String type;
	private String host;
	
	
	public Event(String id, boolean alert, long duration, String type, String host) {
		this.id = id;
		this.alert = alert;
		this.duration = duration;
		this.type = type;
		this.host = host;
	}
	
	public Event(String id, long duration, String type, String host) {
		this.id = id;
		this.duration = duration;
		this.type = type;
		this.host = host;
	}

	public long getDuration() {
		return duration;
	}

	public void setDuration(long timestamp) {
		this.duration = timestamp;
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

	public boolean getAlert() {
		return alert;
	}

	public void setAlert(boolean state) {
		this.alert = state;
	}
	
}
