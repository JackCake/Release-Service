package ntut.csie.releaseService.useCase.release.delete;

import ntut.csie.releaseService.useCase.Input;

public interface DeleteReleaseInput extends Input{
	public String getReleaseId();
	
	public void setReleaseId(String releaseId);
}
