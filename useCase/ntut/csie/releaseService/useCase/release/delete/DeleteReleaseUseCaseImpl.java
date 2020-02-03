package ntut.csie.releaseService.useCase.release.delete;

import java.util.ArrayList;
import java.util.List;

import ntut.csie.releaseService.model.release.Release;
import ntut.csie.releaseService.useCase.release.ReleaseRepository;

public class DeleteReleaseUseCaseImpl implements DeleteReleaseUseCase, DeleteReleaseInput{
	private ReleaseRepository releaseRepository;
	
	private String releaseId;

	public DeleteReleaseUseCaseImpl(ReleaseRepository releaseRepository) {
		this.releaseRepository = releaseRepository;
	}
	
	@Override
	public void execute(DeleteReleaseInput input, DeleteReleaseOutput output) {
		Release release = releaseRepository.getReleaseById(input.getReleaseId());
		if(release == null) {
			output.setDeleteSuccess(false);
			output.setErrorMessage("Sorry, the release is not exist!");
			return;
		}
		int orderId = release.getOrderId();
		String productId = release.getProductId();
		List<Release> releaseList = new ArrayList<>(releaseRepository.getReleasesByProductId(productId));
		try {
			for(int i = orderId; i < releaseList.size(); i++) {
				releaseList.get(i).setOrderId(i);
				releaseRepository.save(releaseList.get(i));
			}
			releaseRepository.remove(release);
		} catch (Exception e) {
			output.setDeleteSuccess(false);
			output.setErrorMessage(e.getMessage());
			return;
		}
		output.setDeleteSuccess(true);
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
