package ntut.csie.releaseService.model.release;

import java.util.Date;

import ntut.csie.releaseService.model.DateProvider;
import ntut.csie.releaseService.model.DomainEvent;

public class BacklogItemUnscheduled implements DomainEvent {
	private Date occuredOn;
	private String backlogItemId;
	private String releaseName;

	public BacklogItemUnscheduled(String backlogItemId, String releaseName) {
		this.occuredOn = DateProvider.now();
		this.backlogItemId = backlogItemId;
		this.releaseName = releaseName;
	}
	
	@Override
	public Date occurredOn() {
		return occuredOn;
	}
	
	public String backlogItemId() {
		return backlogItemId;
	}
	
	public String releaseName() {
		return releaseName;
	}
}
