package ntut.csie.releaseService.controller.release;

import javax.inject.Singleton;
import javax.ws.rs.Consumes;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.json.JSONException;
import org.json.JSONObject;

import ntut.csie.releaseService.ApplicationContext;
import ntut.csie.releaseService.useCase.release.edit.EditReleaseInput;
import ntut.csie.releaseService.useCase.release.edit.EditReleaseOutput;
import ntut.csie.releaseService.useCase.release.edit.EditReleaseUseCase;

@Path("/releases")
@Singleton
public class EditReleaseRestfulAPI implements EditReleaseOutput{
	private ApplicationContext applicationContext = ApplicationContext.getInstance();
	private EditReleaseUseCase editReleaseUseCase = applicationContext.newEditReleaseUseCase();
	
	private boolean editSuccess;
	private String errorMessage;

	@PUT
	@Path("/{release_id}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public synchronized EditReleaseOutput editRelease(
			@PathParam("release_id") String releaseId, 
			String releaseInfo) {
		String name = "";
		String startDate = "";
		String endDate = "";
		String description = "";
		
		EditReleaseOutput output = this;
		
		try {
			JSONObject sprintJSON = new JSONObject(releaseInfo);
			name = sprintJSON.getString("name");
			startDate = sprintJSON.getString("startDate");
			endDate = sprintJSON.getString("endDate");
			description = sprintJSON.getString("description");
		} catch (JSONException e) {
			e.printStackTrace();
			output.setEditSuccess(false);
			output.setErrorMessage("Sorry, there is the service problem when edit the release. Please contact to the system administrator!");
			return output;
		}
		
		EditReleaseInput input = (EditReleaseInput) editReleaseUseCase;
		input.setReleaseId(releaseId);
		input.setName(name);
		input.setStartDate(startDate);
		input.setEndDate(endDate);
		input.setDescription(description);
		
		editReleaseUseCase.execute(input, output);
		
		return output;
	}

	@Override
	public boolean isEditSuccess() {
		return editSuccess;
	}

	@Override
	public void setEditSuccess(boolean editSuccess) {
		this.editSuccess = editSuccess;
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
