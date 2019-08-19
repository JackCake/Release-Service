package ntut.csie.releaseService.model.release;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import ntut.csie.releaseService.model.DomainEventPublisher;

public class Release {
	private String releaseId;
	private int orderId;
	private String name;
	private String startDate;
	private String endDate;
	private String description;
	private String productId;
	private List<ScheduledBacklogItem> scheduledBacklogItems;

	public Release() {
		scheduledBacklogItems = new ArrayList<>();
	}
	
	public Release(String releaseId, String name, String startDate, String endDate, 
			String description, String productId) {
		this.releaseId = releaseId;
		this.name = name;
		this.startDate = startDate;
		this.endDate = endDate;
		this.productId = productId;
		this.description = description;
		scheduledBacklogItems = new ArrayList<>();
	}
	
	public boolean isReleaseOverlap(String otherStartDate, String otherEndDate) {
		DateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		long thisReleaseStartDate = 0;
		long thisReleaseEndDate = 0;
		long otherReleaseStartDate = 0;
		long otherReleaseEndDate = 0;
		try {
			thisReleaseStartDate = simpleDateFormat.parse(startDate).getTime();
			thisReleaseEndDate = simpleDateFormat.parse(endDate).getTime();
			otherReleaseStartDate = simpleDateFormat.parse(otherStartDate).getTime();
			otherReleaseEndDate = simpleDateFormat.parse(otherEndDate).getTime();
		} catch (ParseException e) {
			System.out.println(e.getMessage());
		}
		if((thisReleaseStartDate >= otherReleaseStartDate && thisReleaseStartDate <= otherReleaseEndDate) ||
			(thisReleaseEndDate >= otherReleaseStartDate	&& thisReleaseEndDate <= otherReleaseEndDate) ||
			(thisReleaseStartDate <= otherReleaseStartDate && thisReleaseEndDate >= otherReleaseEndDate)) {
			return true;
		}
		return false;
	}
	
	public boolean isReleaseOverdue() {
		Calendar calendar = Calendar.getInstance();
		DateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		long today = calendar.getTimeInMillis();
		long thisReleaseEndDate = 0;
		try {
			thisReleaseEndDate = simpleDateFormat.parse(endDate).getTime();
		} catch (ParseException e) {
			System.out.println(e.getMessage());
		}
		return thisReleaseEndDate < today;
	}
	
	public void schedule(String backlogItemId) {
		addScheduledBacklogItem(backlogItemId);
		DomainEventPublisher.getInstance().publish(new BacklogItemScheduled(
				backlogItemId, name));
	}
	
	public void unschedule(String backlogItemId) {
		removeScheduledBacklogItem(backlogItemId);
		DomainEventPublisher.getInstance().publish(new BacklogItemUnscheduled(
				backlogItemId, name));
	}
	
	public void addScheduledBacklogItem(String backlogItemId) {
		ScheduledBacklogItem scheduledBacklogItem = ScheduledBacklogItemBuilder.newInstance()
				.backlogItemId(backlogItemId)
				.releaseId(releaseId)
				.build();
		scheduledBacklogItems.add(scheduledBacklogItem);
	}
	
	public void removeScheduledBacklogItem(String backlogItemId) {
		for(ScheduledBacklogItem scheduledBacklogItem : scheduledBacklogItems) {
			if(scheduledBacklogItem.getBacklogItemId().equals(backlogItemId)) {
				scheduledBacklogItems.remove(scheduledBacklogItem);
				break;
			}
		}
	}

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

	public List<ScheduledBacklogItem> getScheduledBacklogItems() {
		return scheduledBacklogItems;
	}

	public void setScheduledBacklogItems(List<ScheduledBacklogItem> scheduledBacklogItems) {
		this.scheduledBacklogItems = scheduledBacklogItems;
	}
}
