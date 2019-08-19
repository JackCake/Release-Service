package ntut.csie.releaseService.useCase.release.scheduledBacklogItem;

import ntut.csie.releaseService.model.release.ScheduledBacklogItem;

public class ConvertScheduledBacklogItemToDTO {
	public static ScheduledBacklogItemModel transform(ScheduledBacklogItem scheduledBacklogItem) {
		ScheduledBacklogItemModel dto = new ScheduledBacklogItemModel();
		dto.setBacklogItemId(scheduledBacklogItem.getBacklogItemId());
		dto.setReleaseId(scheduledBacklogItem.getReleaseId());
		return dto;
	}
}
