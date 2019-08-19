package ntut.csie.releaseService.controller.release.scheduledBacklogItem;

import javax.inject.Singleton;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import ntut.csie.releaseService.ApplicationContext;
import ntut.csie.releaseService.useCase.release.scheduledBacklogItem.unschedule.UnscheduleBacklogItemFromReleaseInput;
import ntut.csie.releaseService.useCase.release.scheduledBacklogItem.unschedule.UnscheduleBacklogItemFromReleaseOutput;
import ntut.csie.releaseService.useCase.release.scheduledBacklogItem.unschedule.UnscheduleBacklogItemFromReleaseUseCase;

@Path("/releases/{release_id}/scheduled_backlog_items")
@Singleton
public class UnscheduleBacklogItemFromReleaseRestfulAPI implements UnscheduleBacklogItemFromReleaseOutput {
	private ApplicationContext applicationContext = ApplicationContext.getInstance();
	private UnscheduleBacklogItemFromReleaseUseCase unscheduleBacklogItemFromReleaseUseCase = applicationContext.newUnscheduleBacklogItemFromReleaseUseCase();
	
	private boolean unscheduleSuccess;
	private String errorMessage;
	
	@DELETE
	@Path("/{backlog_item_id}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public synchronized UnscheduleBacklogItemFromReleaseOutput unscheduleBacklogItemFromRelease(
			@PathParam("release_id") String releaseId, 
			@PathParam("backlog_item_id") String backlogItemId) {
		UnscheduleBacklogItemFromReleaseOutput output = this;
		
		UnscheduleBacklogItemFromReleaseInput input = (UnscheduleBacklogItemFromReleaseInput) unscheduleBacklogItemFromReleaseUseCase;
		input.setBacklogItemId(backlogItemId);
		input.setReleaseId(releaseId);
		
		unscheduleBacklogItemFromReleaseUseCase.execute(input, output);
		
		return output;
	}

	@Override
	public boolean isUnscheduleSuccess() {
		return unscheduleSuccess;
	}

	@Override
	public void setUnscheduleSuccess(boolean unscheduleSuccess) {
		this.unscheduleSuccess = unscheduleSuccess;
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
