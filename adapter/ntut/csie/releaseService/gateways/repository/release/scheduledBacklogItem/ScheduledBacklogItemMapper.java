package ntut.csie.releaseService.gateways.repository.release.scheduledBacklogItem;

import ntut.csie.releaseService.model.release.ScheduledBacklogItem;

public class ScheduledBacklogItemMapper {
	public ScheduledBacklogItemData transformToScheduledBacklogItemData(ScheduledBacklogItem scheduledBacklogItem) {
		ScheduledBacklogItemData data = new ScheduledBacklogItemData();
		data.setBacklogItemId(scheduledBacklogItem.getBacklogItemId());
		data.setReleaseId(scheduledBacklogItem.getReleaseId());
		return data;
	}
}
