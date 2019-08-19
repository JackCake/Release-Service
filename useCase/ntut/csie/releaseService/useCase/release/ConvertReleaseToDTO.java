package ntut.csie.releaseService.useCase.release;

import ntut.csie.releaseService.model.release.Release;

public class ConvertReleaseToDTO {
	public static ReleaseModel transform(Release release) {
		ReleaseModel dto = new ReleaseModel();
		dto.setReleaseId(release.getReleaseId());
		dto.setOrderId(release.getOrderId());
		dto.setName(release.getName());
		dto.setStartDate(release.getStartDate());
		dto.setEndDate(release.getEndDate());
		dto.setDescription(release.getDescription());
		dto.setProductId(release.getProductId());
		dto.setReleaseOverdue(release.isReleaseOverdue());
		return dto;
	}
}
