package ntut.csie.releaseService.useCase;

import java.util.List;

import ntut.csie.releaseService.model.DomainEvent;

public interface EventStore {
	public void save(DomainEvent event);
	
	public List<DomainEvent> getByEventType(String eventType);
	
	public List<DomainEvent> getAllEvent();
}
