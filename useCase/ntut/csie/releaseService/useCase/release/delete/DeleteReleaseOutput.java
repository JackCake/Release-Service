package ntut.csie.releaseService.useCase.release.delete;

import ntut.csie.releaseService.useCase.Output;

public interface DeleteReleaseOutput extends Output{
	public boolean isDeleteSuccess();
	
	public void setDeleteSuccess(boolean deleteSuccess);
	
	public String getErrorMessage();
	
	public void setErrorMessage(String errorMessage);
}
