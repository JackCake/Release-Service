package ntut.csie.releaseService.useCase.release.scheduledBacklogItem.unschedule;

import ntut.csie.releaseService.model.release.Release;
import ntut.csie.releaseService.useCase.release.ReleaseRepository;

public class UnscheduleBacklogItemFromReleaseUseCaseImpl implements UnscheduleBacklogItemFromReleaseUseCase, UnscheduleBacklogItemFromReleaseInput {
	private ReleaseRepository releaseRepository;
	
	private String backlogItemId;
	private String releaseId;
	
	public UnscheduleBacklogItemFromReleaseUseCaseImpl(ReleaseRepository releaseRepository) {
		this.releaseRepository = releaseRepository;
	}
	
	@Override
	public void execute(UnscheduleBacklogItemFromReleaseInput input, UnscheduleBacklogItemFromReleaseOutput output) {
		Release release = releaseRepository.getReleaseById(input.getReleaseId());
		if(release == null) {
			output.setUnscheduleSuccess(false);
			output.setErrorMessage("Sorry, the release is not exist.");
			return;
		}
		release.unschedule(input.getBacklogItemId());
		try {
			releaseRepository.save(release);
		} catch (Exception e) {
			output.setUnscheduleSuccess(false);
			output.setErrorMessage(e.getMessage());
			return;
		}
		output.setUnscheduleSuccess(true);
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
