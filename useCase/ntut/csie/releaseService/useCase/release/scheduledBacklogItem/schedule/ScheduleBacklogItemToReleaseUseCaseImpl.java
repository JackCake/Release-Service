package ntut.csie.releaseService.useCase.release.scheduledBacklogItem.schedule;

import ntut.csie.releaseService.model.release.Release;
import ntut.csie.releaseService.useCase.release.ReleaseRepository;

public class ScheduleBacklogItemToReleaseUseCaseImpl implements ScheduleBacklogItemToReleaseUseCase, ScheduleBacklogItemToReleaseInput {
	private ReleaseRepository releaseRepository;
	
	private String backlogItemId;
	private String releaseId;
	
	public ScheduleBacklogItemToReleaseUseCaseImpl(ReleaseRepository releaseRepository) {
		this.releaseRepository = releaseRepository;
	}
	
	@Override
	public void execute(ScheduleBacklogItemToReleaseInput input, ScheduleBacklogItemToReleaseOutput output) {
		Release release = releaseRepository.getReleaseById(input.getReleaseId());
		if(release == null) {
			output.setScheduleSuccess(false);
			output.setErrorMessage("Sorry, the release is not exist!");
			return;
		}
		try {
			release.schedule(input.getBacklogItemId());
			releaseRepository.save(release);
		} catch (Exception e) {
			output.setScheduleSuccess(false);
			output.setErrorMessage(e.getMessage());
			return;
		}
		output.setScheduleSuccess(true);
	}

	@Override
	public String getBacklogItemId() {
		return backlogItemId;
	}

	@Override
	public void setBacklogItemId(String backlogItemId) {
		this.backlogItemId = backlogItemId;
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
