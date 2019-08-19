package ntut.csie.releaseService.useCase.history.get;

import java.util.ArrayList;
import java.util.List;

import ntut.csie.releaseService.model.DomainEvent;
import ntut.csie.releaseService.model.release.BacklogItemScheduled;
import ntut.csie.releaseService.model.release.BacklogItemUnscheduled;
import ntut.csie.releaseService.useCase.EventStore;
import ntut.csie.releaseService.useCase.history.ConvertBacklogItemScheduledEventToDTO;
import ntut.csie.releaseService.useCase.history.ConvertBacklogItemUnscheduledEventToDTO;
import ntut.csie.releaseService.useCase.history.HistoryModel;

public class GetHistoriesByBacklogItemIdUseCaseImpl implements GetHistoriesByBacklogItemIdUseCase, GetHistoriesByBacklogItemIdInput {
	private EventStore eventStore;
	
	private String backlogItemId;
	
	public GetHistoriesByBacklogItemIdUseCaseImpl(EventStore eventStore) {
		this.eventStore = eventStore;
	}
	
	@Override
	public void execute(GetHistoriesByBacklogItemIdInput input, GetHistoriesByBacklogItemIdOutput output) {
		String backlogItemId = input.getBacklogItemId();
		List<HistoryModel> historyList = new ArrayList<>();
		for(DomainEvent domainEvent : eventStore.getAllEvent()) {
			if(domainEvent instanceof BacklogItemScheduled) {
				BacklogItemScheduled backlogItemScheduled = (BacklogItemScheduled) domainEvent;
				if(backlogItemScheduled.backlogItemId().equals(backlogItemId)) {
					historyList.add(ConvertBacklogItemScheduledEventToDTO.transform(backlogItemScheduled));
				}
			} else if(domainEvent instanceof BacklogItemUnscheduled) {
				BacklogItemUnscheduled backlogItemUnscheduled = (BacklogItemUnscheduled) domainEvent;
				if(backlogItemUnscheduled.backlogItemId().equals(backlogItemId)) {
					historyList.add(ConvertBacklogItemUnscheduledEventToDTO.transform(backlogItemUnscheduled));
				}
			}
		}
		output.setHistoryList(historyList);
	}

	@Override
	public String getBacklogItemId() {
		return backlogItemId;
	}

	@Override
	public void setBacklogItemId(String backlogItemId) {
		this.backlogItemId = backlogItemId;
	}
}
