package ntut.csie.releaseService.model;

public interface DomainEventSubscriber<T> {
	public void handleEvent(final T domainEvent);
	public Class<T> subscribedToEventType();
}
