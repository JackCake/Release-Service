package ntut.csie.releaseService.useCase;

import ntut.csie.releaseService.model.DomainEventPublisher;
import ntut.csie.releaseService.model.DomainEventSubscriber;
import ntut.csie.releaseService.model.release.BacklogItemScheduled;
import ntut.csie.releaseService.model.release.BacklogItemUnscheduled;

public class DomainEventListener {
	private static DomainEventListener instance = null;
	
	private EventStore eventStore;
	
	private DomainEventListener() {}
	
	public static synchronized DomainEventListener getInstance() {
		if(instance == null) {
			instance = new DomainEventListener();
		}
		return instance;
	}
	
	public void init(EventStore eventStore) {
		this.eventStore = eventStore;
		DomainEventPublisher.getInstance().reset();
		eventSubscribe();
	}
	
	private void eventSubscribe() {
		DomainEventPublisher.getInstance().subscribe(new DomainEventSubscriber<BacklogItemScheduled>() {

			@Override
			public void handleEvent(BacklogItemScheduled domainEvent) {
				eventStore.save(domainEvent);
			}

			@Override
			public Class<BacklogItemScheduled> subscribedToEventType() {
				return BacklogItemScheduled.class;
			}
           
        });
		
		DomainEventPublisher.getInstance().subscribe(new DomainEventSubscriber<BacklogItemUnscheduled>() {

			@Override
			public void handleEvent(BacklogItemUnscheduled domainEvent) {
				eventStore.save(domainEvent);
			}

			@Override
			public Class<BacklogItemUnscheduled> subscribedToEventType() {
				return BacklogItemUnscheduled.class;
			}
           
        });
	}
}
