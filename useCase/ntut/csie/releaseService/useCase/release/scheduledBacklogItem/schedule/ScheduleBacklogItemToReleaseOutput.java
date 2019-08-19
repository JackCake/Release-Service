package ntut.csie.releaseService.useCase.release.scheduledBacklogItem.schedule;

import ntut.csie.releaseService.useCase.Output;

public interface ScheduleBacklogItemToReleaseOutput extends Output {
	public boolean isScheduleSuccess();
	
	public void setScheduleSuccess(boolean scheduleSuccess);
	
	public String getErrorMessage();
	
	public void setErrorMessage(String errorMessage);
}
