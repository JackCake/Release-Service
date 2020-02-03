package ntut.csie.releaseService.useCase.release.add;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import ntut.csie.releaseService.model.release.Release;
import ntut.csie.releaseService.model.release.ReleaseBuilder;
import ntut.csie.releaseService.useCase.release.ReleaseRepository;

public class AddReleaseUseCaseImpl implements AddReleaseUseCase, AddReleaseInput {
	private ReleaseRepository releaseRepository;
	
	private String name;
	private String startDate;
	private String endDate;
	private String description;
	private String productId;
	
	public AddReleaseUseCaseImpl(ReleaseRepository releaseRepository) {
		this.releaseRepository = releaseRepository;
	}
	
	@Override
	public void execute(AddReleaseInput input, AddReleaseOutput output) {
		try {
			int orderId = releaseRepository.getReleasesByProductId(input.getProductId()).size() + 1;
			Release release = ReleaseBuilder.newInstance().
					orderId(orderId).
					name(input.getName()).
					startDate(input.getStartDate()).
					endDate(input.getEndDate()).
					description(input.getDescription()).
					productId(input.getProductId()).
					build();
			if(release.isReleaseStartDateAfterEndDate()) {
				output.setAddSuccess(false);
				output.setErrorMessage("Sorry, the start date must be before the end date!");
				return;
			}
			if(isReleaseOverlap(release)) {
				output.setAddSuccess(false);
				output.setErrorMessage("Sorry, the start date or the end date is overlap with the other release!");
				return;
			}
			releaseRepository.save(release);
		} catch (Exception e) {
			output.setAddSuccess(false);
			output.setErrorMessage(e.getMessage());
			return;
		}
		output.setAddSuccess(true);
	}
	
	private boolean isReleaseOverlap(Release thisRelease) throws ParseException {
		List<Release> releaseList = new ArrayList<>(releaseRepository.getReleasesByProductId(thisRelease.getProductId()));
		for(Release otherRelease : releaseList) {
			String otherStartDate = otherRelease.getStartDate();
			String otherEndDate = otherRelease.getEndDate();
			if(thisRelease.isReleaseOverlap(otherStartDate, otherEndDate)) {
				return true;
			}
		}
		return false;
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
