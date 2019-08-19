package ntut.csie.releaseService.useCase.release.get;

import java.util.ArrayList;
import java.util.List;

import ntut.csie.releaseService.model.release.Release;
import ntut.csie.releaseService.useCase.release.ConvertReleaseToDTO;
import ntut.csie.releaseService.useCase.release.ReleaseModel;
import ntut.csie.releaseService.useCase.release.ReleaseRepository;

public class GetReleasesByProductIdUseCaseImpl implements GetReleasesByProductIdUseCase, GetReleasesByProductIdInput {
	private ReleaseRepository releaseRepository;
	
	private String productId;
	
	public GetReleasesByProductIdUseCaseImpl(ReleaseRepository releaseRepository) {
		this.releaseRepository = releaseRepository;
	}

	@Override
	public void execute(GetReleasesByProductIdInput input, GetReleasesByProductIdOutput output) {
		List<ReleaseModel> releaseList = new ArrayList<>();
		for(Release release : releaseRepository.getReleasesByProductId(input.getProductId())) {
			releaseList.add(ConvertReleaseToDTO.transform(release));
		}
		output.setReleaseList(releaseList);
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
