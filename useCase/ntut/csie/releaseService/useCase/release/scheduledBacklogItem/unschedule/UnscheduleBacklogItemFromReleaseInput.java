package ntut.csie.releaseService.useCase.release.scheduledBacklogItem.unschedule;

import ntut.csie.releaseService.useCase.Input;

public interface UnscheduleBacklogItemFromReleaseInput extends Input {
	public String getBacklogItemId();
	
	public void setBacklogItemId(String backlogItemId);
	
	public String getReleaseId();
	
	public void setReleaseId(String releaseId);
}
