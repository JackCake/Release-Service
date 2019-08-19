package ntut.csie.releaseService.controller.release;

import javax.inject.Singleton;
import javax.ws.rs.DELETE;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import ntut.csie.releaseService.ApplicationContext;
import ntut.csie.releaseService.useCase.release.delete.DeleteReleaseInput;
import ntut.csie.releaseService.useCase.release.delete.DeleteReleaseOutput;
import ntut.csie.releaseService.useCase.release.delete.DeleteReleaseUseCase;

@Path("/releases")
@Singleton
public class DeleteReleaseRestfulAPI implements DeleteReleaseOutput{
	private ApplicationContext applicationContext = ApplicationContext.getInstance();
	private DeleteReleaseUseCase deleteReleaseUseCase = applicationContext.newDeleteReleaseUseCase();
	
	private boolean deleteSuccess;
	private String errorMessage;

	@DELETE
	@Path("/{release_id}")
	public synchronized DeleteReleaseOutput deletRelease(@PathParam("release_id") String releaseId) {
		DeleteReleaseOutput output = this;
		
		DeleteReleaseInput input = (DeleteReleaseInput) deleteReleaseUseCase;
		input.setReleaseId(releaseId);
		
		deleteReleaseUseCase.execute(input, output);
		
		return output;
	}
	
	@Override
	public boolean isDeleteSuccess() {
		return deleteSuccess;
	}

	@Override
	public void setDeleteSuccess(boolean deleteSuccess) {
		this.deleteSuccess = deleteSuccess;
	}

	@Override
	public String getErrorMessage() {
		return errorMessage;
	}

	@Override
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
}
