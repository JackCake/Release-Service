package ntut.csie.releaseService.useCase.release.get;

import java.util.List;

import ntut.csie.releaseService.useCase.Output;
import ntut.csie.releaseService.useCase.release.ReleaseModel;

public interface GetReleasesByProductIdOutput extends Output {
	public List<ReleaseModel> getReleaseList();
	
	public void setReleaseList(List<ReleaseModel> releaseList);
}
