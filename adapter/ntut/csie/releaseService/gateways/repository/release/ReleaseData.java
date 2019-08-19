package ntut.csie.releaseService.gateways.repository.release;

import java.util.Collection;

import ntut.csie.releaseService.gateways.repository.release.scheduledBacklogItem.ScheduledBacklogItemData;

public class ReleaseData {
	private String releaseId;
	private int orderId;
	private String name;
	private String startDate;
	private String endDate;
	private String description;
	private String productId;
	private Collection<ScheduledBacklogItemData> scheduledBacklogItemDatas;
	
	public String getReleaseId() {
		return releaseId;
	}
	
	public void setReleaseId(String releaseId) {
		this.releaseId = releaseId;
	}
	
	public int getOrderId() {
		return orderId;
	}
	
	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getStartDate() {
		return startDate;
	}
	
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	
	public String getEndDate() {
		return endDate;
	}
	
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getProductId() {
		return productId;
	}
	
	public void setProductId(String productId) {
		this.productId = productId;
	}

	public Collection<ScheduledBacklogItemData> getScheduledBacklogItemDatas() {
		return scheduledBacklogItemDatas;
	}

	public void setScheduledBacklogItemDatas(Collection<ScheduledBacklogItemData> scheduledBacklogItemDatas) {
		this.scheduledBacklogItemDatas = scheduledBacklogItemDatas;
	}
}
