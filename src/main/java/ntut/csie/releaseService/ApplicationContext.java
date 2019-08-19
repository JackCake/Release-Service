package ntut.csie.releaseService;

import ntut.csie.releaseService.gateways.repository.event.MySqlEventStoreImpl;
import ntut.csie.releaseService.gateways.repository.release.MySqlReleaseRepositoryImpl;
import ntut.csie.releaseService.useCase.EventStore;
import ntut.csie.releaseService.useCase.history.get.GetHistoriesByBacklogItemIdUseCase;
import ntut.csie.releaseService.useCase.history.get.GetHistoriesByBacklogItemIdUseCaseImpl;
import ntut.csie.releaseService.useCase.release.ReleaseRepository;
import ntut.csie.releaseService.useCase.release.add.AddReleaseUseCase;
import ntut.csie.releaseService.useCase.release.add.AddReleaseUseCaseImpl;
import ntut.csie.releaseService.useCase.release.delete.DeleteReleaseUseCase;
import ntut.csie.releaseService.useCase.release.delete.DeleteReleaseUseCaseImpl;
import ntut.csie.releaseService.useCase.release.edit.EditReleaseUseCase;
import ntut.csie.releaseService.useCase.release.edit.EditReleaseUseCaseImpl;
import ntut.csie.releaseService.useCase.release.get.GetReleasesByProductIdUseCase;
import ntut.csie.releaseService.useCase.release.get.GetReleasesByProductIdUseCaseImpl;
import ntut.csie.releaseService.useCase.release.scheduledBacklogItem.get.GetScheduledBacklogItemsByReleaseIdUseCase;
import ntut.csie.releaseService.useCase.release.scheduledBacklogItem.get.GetScheduledBacklogItemsByReleaseIdUseCaseImpl;
import ntut.csie.releaseService.useCase.release.scheduledBacklogItem.schedule.ScheduleBacklogItemToReleaseUseCase;
import ntut.csie.releaseService.useCase.release.scheduledBacklogItem.schedule.ScheduleBacklogItemToReleaseUseCaseImpl;
import ntut.csie.releaseService.useCase.release.scheduledBacklogItem.unschedule.UnscheduleBacklogItemFromReleaseUseCase;
import ntut.csie.releaseService.useCase.release.scheduledBacklogItem.unschedule.UnscheduleBacklogItemFromReleaseUseCaseImpl;

public class ApplicationContext {
	private static ApplicationContext instance = null;
	
	private static ReleaseRepository releaseRepository = null;
	private static EventStore eventStore = null;
	
	private AddReleaseUseCase addReleaseUseCase;
	private GetReleasesByProductIdUseCase getReleasesByProductIdUseCase;
	private EditReleaseUseCase editReleaseUseCase;
	private DeleteReleaseUseCase deleteReleaseUseCase;
	
	private ScheduleBacklogItemToReleaseUseCase scheduleBacklogItemToReleaseUseCase;
	private GetScheduledBacklogItemsByReleaseIdUseCase getScheduledBacklogItemsByReleaseIdUseCase;
	private UnscheduleBacklogItemFromReleaseUseCase unscheduleBacklogItemFromReleaseUseCase;
	private GetHistoriesByBacklogItemIdUseCase getHistoriesByBacklogItemIdUseCase;
	
	private ApplicationContext() {}
	
	public static synchronized ApplicationContext getInstance() {
		if(instance == null){
			instance = new ApplicationContext();
		}
		return instance;
	}
	
	public ReleaseRepository newReleaseRepository() {
		if(releaseRepository == null) {
			releaseRepository = new MySqlReleaseRepositoryImpl();
		}
		return releaseRepository;
	}
	
	public EventStore newEventStore() {
		if(eventStore == null) {
			eventStore = new MySqlEventStoreImpl();
		}
		return eventStore;
	}
	
	public AddReleaseUseCase newAddReleaseUseCase() {
		addReleaseUseCase = new AddReleaseUseCaseImpl(newReleaseRepository());
		return addReleaseUseCase;
	}
	
	public GetReleasesByProductIdUseCase newGetReleasesByProductIdUseCase() {
		getReleasesByProductIdUseCase = new GetReleasesByProductIdUseCaseImpl(newReleaseRepository());
		return getReleasesByProductIdUseCase;
	}
	
	public EditReleaseUseCase newEditReleaseUseCase() {
		editReleaseUseCase = new EditReleaseUseCaseImpl(newReleaseRepository());
		return editReleaseUseCase;
	}
	
	public DeleteReleaseUseCase newDeleteReleaseUseCase() {
		deleteReleaseUseCase = new DeleteReleaseUseCaseImpl(newReleaseRepository());
		return deleteReleaseUseCase;
	}
	
	public ScheduleBacklogItemToReleaseUseCase newScheduleBacklogItemToReleaseUseCase() {
		scheduleBacklogItemToReleaseUseCase = new ScheduleBacklogItemToReleaseUseCaseImpl(newReleaseRepository());
		return scheduleBacklogItemToReleaseUseCase;
	}
	
	public GetScheduledBacklogItemsByReleaseIdUseCase newGetScheduledBacklogItemsByReleaseIdUseCase() {
		getScheduledBacklogItemsByReleaseIdUseCase = new GetScheduledBacklogItemsByReleaseIdUseCaseImpl(newReleaseRepository());
		return getScheduledBacklogItemsByReleaseIdUseCase;
	}
	
	public UnscheduleBacklogItemFromReleaseUseCase newUnscheduleBacklogItemFromReleaseUseCase() {
		unscheduleBacklogItemFromReleaseUseCase = new UnscheduleBacklogItemFromReleaseUseCaseImpl(newReleaseRepository());
		return unscheduleBacklogItemFromReleaseUseCase;
	}
	
	public GetHistoriesByBacklogItemIdUseCase newGetHistoriesByBacklogItemIdUseCase() {
		getHistoriesByBacklogItemIdUseCase = new GetHistoriesByBacklogItemIdUseCaseImpl(newEventStore());
		return getHistoriesByBacklogItemIdUseCase;
	}
}
