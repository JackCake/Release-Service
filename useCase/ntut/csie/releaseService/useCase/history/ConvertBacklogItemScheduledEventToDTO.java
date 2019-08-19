package ntut.csie.releaseService.useCase.history;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import ntut.csie.releaseService.model.release.BacklogItemScheduled;
import ntut.csie.releaseService.model.release.ScheduledBacklogItemBehavior;

public class ConvertBacklogItemScheduledEventToDTO {
	public static HistoryModel transform(BacklogItemScheduled backlogItemScheduled) {
		HistoryModel dto = new HistoryModel();
		DateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		String occurredOn = simpleDateFormat.format(backlogItemScheduled.occurredOn());
		dto.setOccurredOn(occurredOn);
		dto.setBehavior(ScheduledBacklogItemBehavior.scheduleBacklogItem);
		dto.setDescription("Schedule Backlog Item To Release " + "\"" + backlogItemScheduled.releaseName() + "\"");
		return dto;
	}
}
