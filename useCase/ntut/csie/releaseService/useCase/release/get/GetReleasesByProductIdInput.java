package ntut.csie.releaseService.useCase.release.get;

import ntut.csie.releaseService.useCase.Input;

public interface GetReleasesByProductIdInput extends Input {
	public String getProductId();
	
	public void setProductId(String productId);
}
