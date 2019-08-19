package ntut.csie.releaseService.controller.release.scheduledBacklogItem;

import java.util.List;

import javax.inject.Singleton;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import ntut.csie.releaseService.ApplicationContext;
import ntut.csie.releaseService.useCase.release.scheduledBacklogItem.ScheduledBacklogItemModel;
import ntut.csie.releaseService.useCase.release.scheduledBacklogItem.get.GetScheduledBacklogItemsByReleaseIdInput;
import ntut.csie.releaseService.useCase.release.scheduledBacklogItem.get.GetScheduledBacklogItemsByReleaseIdOutput;
import ntut.csie.releaseService.useCase.release.scheduledBacklogItem.get.GetScheduledBacklogItemsByReleaseIdUseCase;

@Path("/releases/{release_id}/scheduled_backlog_items")
@Singleton
public class GetScheduledBacklogItemsByReleaseIdRestfulAPI implements GetScheduledBacklogItemsByReleaseIdOutput {
	private ApplicationContext applicationContext = ApplicationContext.getInstance();
	private GetScheduledBacklogItemsByReleaseIdUseCase getScheduledBacklogItemsByReleaseIdUseCase = applicationContext.newGetScheduledBacklogItemsByReleaseIdUseCase();
	
	private List<ScheduledBacklogItemModel> scheduledBacklogItemList;
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public synchronized GetScheduledBacklogItemsByReleaseIdOutput getScheduledBacklogItemsByReleaseId(@PathParam("release_id") String releaseId) {
		GetScheduledBacklogItemsByReleaseIdOutput output = this;
		
		GetScheduledBacklogItemsByReleaseIdInput input = (GetScheduledBacklogItemsByReleaseIdInput) getScheduledBacklogItemsByReleaseIdUseCase;
		
		input.setReleaseId(releaseId);
		
		getScheduledBacklogItemsByReleaseIdUseCase.execute(input, output);
		
		return output;
	}

	@Override
	public List<ScheduledBacklogItemModel> getScheduledBacklogItemList() {
		return scheduledBacklogItemList;
	}

	@Override
	public void setScheduledBacklogItemList(List<ScheduledBacklogItemModel> scheduledBacklogItemList) {
		this.scheduledBacklogItemList = scheduledBacklogItemList;
	}
}
