package ntut.csie.releaseService.controller.release.scheduledBacklogItem;

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
import ntut.csie.releaseService.useCase.release.scheduledBacklogItem.schedule.ScheduleBacklogItemToReleaseInput;
import ntut.csie.releaseService.useCase.release.scheduledBacklogItem.schedule.ScheduleBacklogItemToReleaseOutput;
import ntut.csie.releaseService.useCase.release.scheduledBacklogItem.schedule.ScheduleBacklogItemToReleaseUseCase;

@Path("/releases/{release_id}/scheduled_backlog_items")
@Singleton
public class ScheduleBacklogItemToReleaseRestfulAPI implements ScheduleBacklogItemToReleaseOutput {
	private ApplicationContext applicationContext = ApplicationContext.getInstance();
	private ScheduleBacklogItemToReleaseUseCase scheduleBacklogItemToReleaseUseCase = applicationContext.newScheduleBacklogItemToReleaseUseCase();
	
	private boolean scheduleSuccess;
	private String errorMessage;
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public synchronized ScheduleBacklogItemToReleaseOutput scheduleBacklogItemToRelease(
			@PathParam("release_id") String releaseId, 
			String scheduledBacklogItemInfo) {
		String backlogItemId = "";
		
		ScheduleBacklogItemToReleaseOutput output = this;
		
		try {
			JSONObject scheduledBacklogItemJSON = new JSONObject(scheduledBacklogItemInfo);
			backlogItemId = scheduledBacklogItemJSON.getString("backlogItemId");
		} catch (JSONException e) {
			e.printStackTrace();
			output.setScheduleSuccess(false);
			output.setErrorMessage("Sorry, there is the service problem when schedule the backlog item to the release. Please contact to the system administrator!");
			return output;
		}
		
		ScheduleBacklogItemToReleaseInput input = (ScheduleBacklogItemToReleaseInput) scheduleBacklogItemToReleaseUseCase;
		input.setBacklogItemId(backlogItemId);
		input.setReleaseId(releaseId);
		
		scheduleBacklogItemToReleaseUseCase.execute(input, output);
		
		return output;
	}

	@Override
	public boolean isScheduleSuccess() {
		return scheduleSuccess;
	}

	@Override
	public void setScheduleSuccess(boolean scheduleSuccess) {
		this.scheduleSuccess = scheduleSuccess;
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
