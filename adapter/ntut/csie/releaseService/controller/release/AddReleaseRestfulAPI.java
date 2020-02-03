package ntut.csie.releaseService.controller.release;

import javax.inject.Singleton;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.json.JSONException;
import org.json.JSONObject;

import ntut.csie.releaseService.ApplicationContext;
import ntut.csie.releaseService.useCase.release.add.AddReleaseInput;
import ntut.csie.releaseService.useCase.release.add.AddReleaseOutput;
import ntut.csie.releaseService.useCase.release.add.AddReleaseUseCase;

@Path("/products/{product_id}/releases")
@Singleton
public class AddReleaseRestfulAPI implements AddReleaseOutput {
	private ApplicationContext applicationContext = ApplicationContext.getInstance();
	private AddReleaseUseCase addReleaseUseCase = applicationContext.newAddReleaseUseCase();
	
	private boolean addSuccess;
	private String errorMessage;
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public synchronized AddReleaseOutput addRelease(
			@PathParam("product_id") String productId, 
			String releaseInfo) {
		String name = "";
		String startDate = "";
		String endDate = "";
		String description = "";
		
		AddReleaseOutput output = this;
		
		try {
			JSONObject sprintJSON = new JSONObject(releaseInfo);
			name = sprintJSON.getString("name");
			startDate = sprintJSON.getString("startDate");
			endDate = sprintJSON.getString("endDate");
			description = sprintJSON.getString("description");
		} catch (JSONException e) {
			e.printStackTrace();
			output.setAddSuccess(false);
			output.setErrorMessage("Sorry, there is the service problem when add the release. Please contact to the system administrator!");
			return output;
		}
		
		AddReleaseInput input = (AddReleaseInput) addReleaseUseCase;
		input.setName(name);
		input.setStartDate(startDate);
		input.setEndDate(endDate);
		input.setDescription(description);
		input.setProductId(productId);
		
		addReleaseUseCase.execute(input, output);

		return output;
	}
	
	@Override
	public boolean isAddSuccess() {
		return addSuccess;
	}

	@Override
	public void setAddSuccess(boolean addSuccess) {
		this.addSuccess = addSuccess;
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
