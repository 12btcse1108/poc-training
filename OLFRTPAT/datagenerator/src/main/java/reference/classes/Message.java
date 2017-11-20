package reference.classes;

public class Message {

	private Data data;
	private Extras extras;
	private Long groupId;
	private Long recordId;
	private Long referenceId;
	private Source source;

	private Long timestamp;

	public Data getData() {
		return data;
	}

	public Extras getExtras() {
		return extras;
	}

	public Long getGroupId() {
		return groupId;
	}

	public Long getRecordId() {
		return recordId;
	}

	public Long getReferenceId() {
		return referenceId;
	}

	public Source getSource() {
		return source;
	}

	public Long getTimestamp() {
		return timestamp;
	}

	public void setData(Data data) {
		this.data = data;
	}

	public void setExtras(Extras extras) {
		this.extras = extras;
	}

	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}

	public void setRecordId(Long recordId) {
		this.recordId = recordId;
	}

	public void setReferenceId(Long referenceId) {
		this.referenceId = referenceId;
	}

	public void setSource(Source source) {
		this.source = source;
	}

	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}

}
