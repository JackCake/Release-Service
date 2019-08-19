package ntut.csie.releaseService.model.release;

public class ScheduledBacklogItemBuilder {
	private String backlogItemId;
	private String releaseId;
	
	public static ScheduledBacklogItemBuilder newInstance() {
		return new ScheduledBacklogItemBuilder();
	}
	
	public ScheduledBacklogItemBuilder backlogItemId(String backlogItemId) {
		this.backlogItemId = backlogItemId;
		return this;
	}
	
	public ScheduledBacklogItemBuilder releaseId(String releaseId) {
		this.releaseId = releaseId;
		return this;
	}
	
	public ScheduledBacklogItem build() {
		ScheduledBacklogItem scheduledBacklogItem = new ScheduledBacklogItem(backlogItemId, releaseId);
		return scheduledBacklogItem;
	}
}
