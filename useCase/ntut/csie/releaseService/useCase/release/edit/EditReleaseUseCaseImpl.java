package ntut.csie.releaseService.useCase.release.edit;

import java.text.ParseException;
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
		String name = input.getName();
		String startDate = input.getStartDate();
		String endDate = input.getEndDate();
		String description = input.getDescription();
		Release release = releaseRepository.getReleaseById(releaseId);
		if(release == null) {
			output.setEditSuccess(false);
			output.setErrorMessage("Sorry, the release is not exist!");
			return;
		}
		String exceptionMessage = "";
		if(name == null || name.isEmpty()) {
			exceptionMessage += "The name of the release should be required!\n";
		}
		if(startDate == null || startDate.isEmpty()) {
			exceptionMessage += "The start date of the release should be required!\n";
		}
		if(endDate == null || endDate.isEmpty()) {
			exceptionMessage += "The end date of the release should be required!\n";
		}
		if(description == null || description.isEmpty()) {
			exceptionMessage += "The description of the release should be required!\n";
		}
		if(!exceptionMessage.isEmpty()) {
			output.setEditSuccess(false);
			output.setErrorMessage(exceptionMessage);
			return;
		}
		release.setName(input.getName());
		release.setStartDate(input.getStartDate());
		release.setEndDate(input.getEndDate());
		release.setDescription(input.getDescription());
		try {
			if(release.isReleaseStartDateAfterEndDate()) {
				output.setEditSuccess(false);
				output.setErrorMessage("Sorry, the start date must be before the end date!");
				return;
			}
			if(isReleaseOverlap(release)) {
				output.setEditSuccess(false);
				output.setErrorMessage("Sorry, the start date or the end date is overlap with the other release!");
				return;
			}
			releaseRepository.save(release);
		} catch (Exception e) {
			output.setEditSuccess(false);
			output.setErrorMessage(e.getMessage());
			return;
		}
		output.setEditSuccess(true);
	}
	
	private boolean isReleaseOverlap(Release thisRelease) throws ParseException {
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
