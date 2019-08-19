package ntut.csie.releaseService.unitTest.repository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import ntut.csie.releaseService.model.release.Release;
import ntut.csie.releaseService.model.release.ScheduledBacklogItem;
import ntut.csie.releaseService.useCase.release.ReleaseRepository;

public class FakeReleaseRepository implements ReleaseRepository {
	private Map<String, Release> releases;
	private Map<String, ScheduledBacklogItem> scheduledBacklogItems;
	
	public FakeReleaseRepository() {
		releases = Collections.synchronizedMap(new LinkedHashMap<String, Release>());
		scheduledBacklogItems = Collections.synchronizedMap(new LinkedHashMap<String, ScheduledBacklogItem>());
	}
	
	@Override
	public void save(Release release) {
		for(ScheduledBacklogItem scheduledBacklogItem : release.getScheduledBacklogItems()) {
			addScheduledBacklogItem(scheduledBacklogItem);
		}
		releases.put(release.getReleaseId(), release);
	}

	@Override
	public void remove(Release release) {
		for(ScheduledBacklogItem scheduledBacklogItem : release.getScheduledBacklogItems()) {
			removeScheduledBacklogItem(scheduledBacklogItem);
		}
		releases.remove(release.getReleaseId());
	}

	@Override
	public Release getReleaseById(String releaseId) {
		return releases.get(releaseId);
	}

	@Override
	public Collection<Release> getReleasesByProductId(String productId) {
		List<Release> releaseList = new ArrayList<>();
		for(Release release : releases.values()) {
			if(release.getProductId().equals(productId)) {
				releaseList.add(release);
			}
		}
		return releaseList;
	}
	
	private void addScheduledBacklogItem(ScheduledBacklogItem scheduledBacklogItem) {
		scheduledBacklogItems.put(scheduledBacklogItem.getBacklogItemId(), scheduledBacklogItem);
	}

	private void removeScheduledBacklogItem(ScheduledBacklogItem scheduledBacklogItem) {
		scheduledBacklogItems.remove(scheduledBacklogItem.getBacklogItemId());
	}

	public void clearAll() {
		releases.clear();
		scheduledBacklogItems.clear();
	}
}
