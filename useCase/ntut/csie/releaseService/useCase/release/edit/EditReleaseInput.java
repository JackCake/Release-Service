package ntut.csie.releaseService.useCase.release.edit;

import ntut.csie.releaseService.useCase.Input;

public interface EditReleaseInput extends Input{
	public String getReleaseId();
	
	public void setReleaseId(String releaseId);
	
	public String getName();
	
	public void setName(String name);
	
	public String getStartDate();
	
	public void setStartDate(String startDate);
	
	public String getEndDate();
	
	public void setEndDate(String endDate);
	
	public String getDescription();
	
	public void setDescription(String description);
	
	public String getProductId();
	
	public void setProductId(String productId);
}
