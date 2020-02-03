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
	
	public ScheduledBacklogItem build() throws Exception {
		String exceptionMessage = "";
		if(backlogItemId == null || backlogItemId.isEmpty()) {
			exceptionMessage += "The backlog item id of the scheduled backlog item should be required!\n";
		}
		if(releaseId == null || releaseId.isEmpty()) {
			exceptionMessage += "The release id of the scheduled backlog item should be required!\n";
		}
		if(!exceptionMessage.isEmpty()) {
			throw new Exception(exceptionMessage);
		}
		
		ScheduledBacklogItem scheduledBacklogItem = new ScheduledBacklogItem(backlogItemId, releaseId);
		return scheduledBacklogItem;
	}
}
