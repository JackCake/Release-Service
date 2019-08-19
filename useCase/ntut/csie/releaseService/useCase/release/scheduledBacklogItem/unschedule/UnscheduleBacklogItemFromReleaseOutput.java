package ntut.csie.releaseService.useCase.release.scheduledBacklogItem.unschedule;

import ntut.csie.releaseService.useCase.Output;

public interface UnscheduleBacklogItemFromReleaseOutput extends Output {
	public boolean isUnscheduleSuccess();
	
	public void setUnscheduleSuccess(boolean unscheduleSuccess);
	
	public String getErrorMessage();
	
	public void setErrorMessage(String errorMessage);
}
