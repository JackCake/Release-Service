package ntut.csie.releaseService.useCase.release.add;

import ntut.csie.releaseService.useCase.Output;

public interface AddReleaseOutput extends Output{
	public boolean isAddSuccess();
	
	public void setAddSuccess(boolean addSuccess);
	
	public String getErrorMessage();
	
	public void setErrorMessage(String errorMessage);
}
