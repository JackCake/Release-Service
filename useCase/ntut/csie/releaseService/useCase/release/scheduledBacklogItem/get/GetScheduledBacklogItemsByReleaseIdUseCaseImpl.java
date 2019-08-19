package ntut.csie.releaseService.useCase.release.scheduledBacklogItem.get;

import java.util.ArrayList;
import java.util.List;

import ntut.csie.releaseService.model.release.Release;
import ntut.csie.releaseService.model.release.ScheduledBacklogItem;
import ntut.csie.releaseService.useCase.release.ReleaseRepository;
import ntut.csie.releaseService.useCase.release.scheduledBacklogItem.ConvertScheduledBacklogItemToDTO;
import ntut.csie.releaseService.useCase.release.scheduledBacklogItem.ScheduledBacklogItemModel;

public class GetScheduledBacklogItemsByReleaseIdUseCaseImpl implements GetScheduledBacklogItemsByReleaseIdUseCase, GetScheduledBacklogItemsByReleaseIdInput {
	private ReleaseRepository releaseRepository;
	
	private String releaseId;
	
	public GetScheduledBacklogItemsByReleaseIdUseCaseImpl(ReleaseRepository releaseRepository) {
		this.releaseRepository = releaseRepository;
	}
	
	@Override
	public void execute(GetScheduledBacklogItemsByReleaseIdInput input,
			GetScheduledBacklogItemsByReleaseIdOutput output) {
		List<ScheduledBacklogItemModel> scheduledBacklogItemList = new ArrayList<>();
		Release release = releaseRepository.getReleaseById(input.getReleaseId());
		for(ScheduledBacklogItem scheduledBacklogItem : release.getScheduledBacklogItems()) {
			scheduledBacklogItemList.add(ConvertScheduledBacklogItemToDTO.transform(scheduledBacklogItem));
		}
		output.setScheduledBacklogItemList(scheduledBacklogItemList);
	}

	@Override
	public String getReleaseId() {
		return releaseId;
	}

	@Override
	public void setReleaseId(String releaseId) {
		this.releaseId = releaseId;
	}
}
