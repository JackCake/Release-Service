package ntut.csie.releaseService.useCase.release.scheduledBacklogItem.get;

import ntut.csie.releaseService.useCase.Input;

public interface GetScheduledBacklogItemsByReleaseIdInput extends Input {
	public String getReleaseId();
	
	public void setReleaseId(String releaseId);
}
