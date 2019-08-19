package ntut.csie.releaseService.useCase.history;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import ntut.csie.releaseService.model.release.BacklogItemUnscheduled;
import ntut.csie.releaseService.model.release.ScheduledBacklogItemBehavior;

public class ConvertBacklogItemUnscheduledEventToDTO {
	public static HistoryModel transform(BacklogItemUnscheduled BacklogItemUnscheduled) {
		HistoryModel dto = new HistoryModel();
		DateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		String occurredOn = simpleDateFormat.format(BacklogItemUnscheduled.occurredOn());
		dto.setOccurredOn(occurredOn);
		dto.setBehavior(ScheduledBacklogItemBehavior.unscheduleBacklogItem);
		dto.setDescription("Unschedule Backlog Item From Release " + "\"" + BacklogItemUnscheduled.releaseName() + "\"");
		return dto;
	}
}
