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
		if(name == null) {
			exceptionMessage += "The name of the release should not be null.\n";
		}
		if(startDate == null) {
			exceptionMessage += "The start date of the release should not be null.\n";
		}
		if(endDate == null) {
			exceptionMessage += "The end date of the release should not be null.\n";
		}
		if(description == null) {
			exceptionMessage += "The description of the release should not be null.\n";
		}
		if(!exceptionMessage.equals("")) {
			throw new Exception(exceptionMessage);
		}
		
		releaseId = UUID.randomUUID().toString();
		Release release = new Release(releaseId, name, startDate, endDate, description, productId);
		release.setOrderId(orderId);
		return release;
	}
}
