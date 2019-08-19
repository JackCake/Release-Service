package ntut.csie.releaseService.useCase.release.scheduledBacklogItem.get;

import java.util.List;

import ntut.csie.releaseService.useCase.Output;
import ntut.csie.releaseService.useCase.release.scheduledBacklogItem.ScheduledBacklogItemModel;

public interface GetScheduledBacklogItemsByReleaseIdOutput extends Output {
	public List<ScheduledBacklogItemModel> getScheduledBacklogItemList();
	
	public void setScheduledBacklogItemList(List<ScheduledBacklogItemModel> scheduledBacklogItemList);
}
