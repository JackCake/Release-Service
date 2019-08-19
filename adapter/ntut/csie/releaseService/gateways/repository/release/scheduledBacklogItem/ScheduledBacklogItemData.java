package ntut.csie.releaseService.gateways.repository.release.scheduledBacklogItem;

public class ScheduledBacklogItemData {
	private String backlogItemId;
	private String releaseId;

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
