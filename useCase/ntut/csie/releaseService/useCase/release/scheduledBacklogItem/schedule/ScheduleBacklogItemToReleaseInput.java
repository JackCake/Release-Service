package ntut.csie.releaseService.useCase.release.scheduledBacklogItem.schedule;

import ntut.csie.releaseService.useCase.Input;

public interface ScheduleBacklogItemToReleaseInput extends Input {
	public String getBacklogItemId();
	
	public void setBacklogItemId(String backlogItemId);
	
	public String getReleaseId();
	
	public void setReleaseId(String releaseId);
}
