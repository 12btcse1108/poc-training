package reference.classes;

import java.io.Serializable;
import java.util.Date;

public class Payload implements Serializable {

	private String from;
	private String message;
	private Long sequence;
	private Date timestamp;
	private String to;

	public Payload(Long sequence, String from, String to, Date timestamp, String message) {
		this.sequence = sequence;
		this.from = from;
		this.to = to;
		this.timestamp = timestamp;
		this.message = message;
	}

	public String getFrom() {
		return from;
	}

	public String getMessage() {
		return message;
	}

	public Long getSequence() {
		return sequence;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public String getTo() {
		return to;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public void setSequence(Long sequence) {
		this.sequence = sequence;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	public void setTo(String to) {
		this.to = to;
	}

	@Override
	public String toString() {
		return this.sequence + ", " + this.from + ", " + this.to + ", " + this.timestamp + ", " + this.message;
	}
}