package ntut.csie.releaseService.gateways.repository.release;

import ntut.csie.releaseService.gateways.repository.release.scheduledBacklogItem.ScheduledBacklogItemData;
import ntut.csie.releaseService.model.release.Release;

public class ReleaseMapper {
	public Release transformToRelease(ReleaseData data) {
		Release release = new Release();
		release.setReleaseId(data.getReleaseId());
		release.setOrderId(data.getOrderId());
		release.setName(data.getName());
		release.setStartDate(data.getStartDate());
		release.setEndDate(data.getEndDate());
		release.setDescription(data.getDescription());
		release.setProductId(data.getProductId());
		for(ScheduledBacklogItemData scheduledBacklogItemData : data.getScheduledBacklogItemDatas()) {
			release.addScheduledBacklogItem(scheduledBacklogItemData.getBacklogItemId());
		}
		return release;
	}
	
	public ReleaseData transformToSprintData(Release release) {
		ReleaseData data = new ReleaseData();
		data.setReleaseId(release.getReleaseId());
		data.setOrderId(release.getOrderId());
		data.setName(release.getName());
		data.setStartDate(release.getStartDate());
		data.setEndDate(release.getEndDate());
		data.setDescription(release.getDescription());
		data.setProductId(release.getProductId());
		return data;
	}
}
