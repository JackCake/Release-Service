package ntut.csie.releaseService.useCase.release.edit;

import ntut.csie.releaseService.useCase.Output;

public interface EditReleaseOutput extends Output{
	public boolean isEditSuccess();
	
	public void setEditSuccess(boolean editSuccess);
	
	public String getErrorMessage();
	
	public void setErrorMessage(String errorMessage);
}
