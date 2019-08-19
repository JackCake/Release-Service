package ntut.csie.releaseService.useCase.history.get;

import ntut.csie.releaseService.useCase.Input;

public interface GetHistoriesByBacklogItemIdInput extends Input{
	public String getBacklogItemId();
	
	public void setBacklogItemId(String backlogItemId);
}
