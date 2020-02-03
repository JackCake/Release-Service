package ntut.csie.releaseService.model.release;

import java.util.UUID;

public class ReleaseBuilder {
	private String releaseId;
	private int orderId;
	private String name;
	private String startDate;
	private String endDate;
	private String description;
	private String productId;
	
	public static ReleaseBuilder newInstance() {
		return new ReleaseBuilder();
	}
	
	public ReleaseBuilder orderId(int orderId) {
		this.orderId = orderId;
		return this;
	}
	
	public ReleaseBuilder name(String name) {
		this.name = name;
		return this;
	}
	
	public ReleaseBuilder startDate(String startDate) {
		this.startDate = startDate;
		return this;
	}
	
	public ReleaseBuilder endDate(String endDate) {
		this.endDate = endDate;
		return this;
	}
	
	public ReleaseBuilder description(String description) {
		this.description = description;
		return this;
	}
	
	public ReleaseBuilder productId(String productId) {
		this.productId = productId;
		return this;
	}
	
	public Release build() throws Exception{
		String exceptionMessage = "";
		if(name == null || name.isEmpty()) {
			exceptionMessage += "The name of the release should be required!\n";
		}
		if(startDate == null || startDate.isEmpty()) {
			exceptionMessage += "The start date of the release should be required!\n";
		}
		if(endDate == null || endDate.isEmpty()) {
			exceptionMessage += "The end date of the release should be required!\n";
		}
		if(description == null || description.isEmpty()) {
			exceptionMessage += "The description of the release should be required!\n";
		}
		if(productId == null || productId.isEmpty()) {
			exceptionMessage += "The product id of the release should be required!\n";
		}
		if(!exceptionMessage.isEmpty()) {
			throw new Exception(exceptionMessage);
		}
		
		releaseId = UUID.randomUUID().toString();
		Release release = new Release(releaseId, name, startDate, endDate, description, productId);
		release.setOrderId(orderId);
		return release;
	}
}
