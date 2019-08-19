package ntut.csie.releaseService.model.release;

public class ScheduledBacklogItem {
	private String backlogItemId;
	private String releaseId;
	
	ScheduledBacklogItem() {}
	
	ScheduledBacklogItem(String backlogItemId, String releaseId) {
		this.backlogItemId = backlogItemId;
		this.releaseId = releaseId;
	}

	public String getBacklogItemId() {
		return backlogItemId;
	}

	public void setBacklogItemId(String backlogItemId) {
		this.backlogItemId = backlogItemId;
	}

	public String getReleaseId() {
		return releaseId;
	}

	public void setReleaseId(String releaseId) {
		this.releaseId = releaseId;
	}
}
