package ntut.csie.releaseService.controller.release;

import java.util.List;

import javax.inject.Singleton;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import ntut.csie.releaseService.ApplicationContext;
import ntut.csie.releaseService.useCase.release.ReleaseModel;
import ntut.csie.releaseService.useCase.release.get.GetReleasesByProductIdInput;
import ntut.csie.releaseService.useCase.release.get.GetReleasesByProductIdOutput;
import ntut.csie.releaseService.useCase.release.get.GetReleasesByProductIdUseCase;

@Path("/products/{product_id}/releases")
@Singleton
public class GetReleasesByProductIdRestfulAPI implements GetReleasesByProductIdOutput{
	private ApplicationContext applicationContext = ApplicationContext.getInstance();
	private GetReleasesByProductIdUseCase getReleasesByProductIdUseCase = applicationContext.newGetReleasesByProductIdUseCase();
	
	private List<ReleaseModel> releaseList;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public synchronized GetReleasesByProductIdOutput getReleasesByProductId(@PathParam("product_id") String productId) {
		GetReleasesByProductIdOutput output = this;
		
		GetReleasesByProductIdInput input = (GetReleasesByProductIdInput) getReleasesByProductIdUseCase;
		input.setProductId(productId);
		
		getReleasesByProductIdUseCase.execute(input, output);
		
		return output;
	}
	
	@Override
	public List<ReleaseModel> getReleaseList() {
		return releaseList;
	}

	@Override
	public void setReleaseList(List<ReleaseModel> releaseList) {
		this.releaseList = releaseList;
	}
}
