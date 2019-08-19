package ntut.csie.releaseService.useCase.release.edit;

import java.util.ArrayList;
import java.util.List;

import ntut.csie.releaseService.model.release.Release;
import ntut.csie.releaseService.useCase.release.ReleaseRepository;

public class EditReleaseUseCaseImpl implements EditReleaseUseCase, EditReleaseInput {
	private ReleaseRepository releaseRepository;
	
	private String releaseId;
	private String name;
	private String startDate;
	private String endDate;
	private String description;
	private String productId;
	
	public EditReleaseUseCaseImpl(ReleaseRepository releaseRepository) {
		this.releaseRepository = releaseRepository;
	}
	
	@Override
	public void execute(EditReleaseInput input, EditReleaseOutput output) {
		String releaseId = input.getReleaseId();
		Release release = releaseRepository.getReleaseById(releaseId);
		if(release == null) {
			output.setEditSuccess(false);
			output.setOverlap(false);
			output.setErrorMessage("Sorry, the sprint is not exist.");
			return;
		}
		release.setName(input.getName());
		release.setStartDate(input.getStartDate());
		release.setEndDate(input.getEndDate());
		release.setDescription(input.getDescription());
		if(isReleaseOverlap(release)) {
			output.setEditSuccess(false);
			output.setOverlap(true);
			output.setErrorMessage("Sorry, the start date or the end date is overlap with the other release.");
			return;
		}
		try {
			releaseRepository.save(release);
		} catch (Exception e) {
			output.setEditSuccess(false);
			output.setErrorMessage(e.getMessage());
			return;
		}
		output.setEditSuccess(true);
		output.setOverlap(false);
	}
	
	private boolean isReleaseOverlap(Release thisRelease) {
		List<Release> releaseList = new ArrayList<>(releaseRepository.getReleasesByProductId(thisRelease.getProductId()));
		for(Release otherRelease : releaseList) {
			if(!thisRelease.getReleaseId().equals(otherRelease.getReleaseId())) {
				String otherStartDate = otherRelease.getStartDate();
				String otherEndDate = otherRelease.getEndDate();
				if(thisRelease.isReleaseOverlap(otherStartDate, otherEndDate)) {
					return true;
				}
			}
		}
		return false;
	}
	
	@Override
	public String getReleaseId() {
		return releaseId;
	}

	@Override
	public void setReleaseId(String releaseId) {
		this.releaseId = releaseId;
	}
	
	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public String getStartDate() {
		return startDate;
	}
	
	@Override
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	
	@Override
	public String getEndDate() {
		return endDate;
	}
	
	@Override
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	
	@Override
	public String getDescription() {
		return description;
	}

	@Override
	public void setDescription(String description) {
		this.description = description;
	}
	
	@Override
	public String getProductId() {
		return productId;
	}
	
	@Override
	public void setProductId(String productId) {
		this.productId = productId;
	}
}
