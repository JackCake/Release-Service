package ntut.csie.releaseService.unitTest.useCase;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import ntut.csie.releaseService.controller.history.GetHistoriesByBacklogItemIdRestfulAPI;
import ntut.csie.releaseService.controller.release.scheduledBacklogItem.GetScheduledBacklogItemsByReleaseIdRestfulAPI;
import ntut.csie.releaseService.controller.release.scheduledBacklogItem.ScheduleBacklogItemToReleaseRestfulAPI;
import ntut.csie.releaseService.controller.release.scheduledBacklogItem.UnscheduleBacklogItemFromReleaseRestfulAPI;
import ntut.csie.releaseService.model.release.Release;
import ntut.csie.releaseService.unitTest.factory.TestFactory;
import ntut.csie.releaseService.unitTest.repository.FakeEventStore;
import ntut.csie.releaseService.unitTest.repository.FakeReleaseRepository;
import ntut.csie.releaseService.useCase.DomainEventListener;
import ntut.csie.releaseService.useCase.history.HistoryModel;
import ntut.csie.releaseService.useCase.history.get.GetHistoriesByBacklogItemIdInput;
import ntut.csie.releaseService.useCase.history.get.GetHistoriesByBacklogItemIdOutput;
import ntut.csie.releaseService.useCase.history.get.GetHistoriesByBacklogItemIdUseCase;
import ntut.csie.releaseService.useCase.history.get.GetHistoriesByBacklogItemIdUseCaseImpl;
import ntut.csie.releaseService.useCase.release.scheduledBacklogItem.ScheduledBacklogItemModel;
import ntut.csie.releaseService.useCase.release.scheduledBacklogItem.get.GetScheduledBacklogItemsByReleaseIdInput;
import ntut.csie.releaseService.useCase.release.scheduledBacklogItem.get.GetScheduledBacklogItemsByReleaseIdOutput;
import ntut.csie.releaseService.useCase.release.scheduledBacklogItem.get.GetScheduledBacklogItemsByReleaseIdUseCase;
import ntut.csie.releaseService.useCase.release.scheduledBacklogItem.get.GetScheduledBacklogItemsByReleaseIdUseCaseImpl;
import ntut.csie.releaseService.useCase.release.scheduledBacklogItem.schedule.ScheduleBacklogItemToReleaseInput;
import ntut.csie.releaseService.useCase.release.scheduledBacklogItem.schedule.ScheduleBacklogItemToReleaseOutput;
import ntut.csie.releaseService.useCase.release.scheduledBacklogItem.schedule.ScheduleBacklogItemToReleaseUseCase;
import ntut.csie.releaseService.useCase.release.scheduledBacklogItem.schedule.ScheduleBacklogItemToReleaseUseCaseImpl;
import ntut.csie.releaseService.useCase.release.scheduledBacklogItem.unschedule.UnscheduleBacklogItemFromReleaseInput;
import ntut.csie.releaseService.useCase.release.scheduledBacklogItem.unschedule.UnscheduleBacklogItemFromReleaseOutput;
import ntut.csie.releaseService.useCase.release.scheduledBacklogItem.unschedule.UnscheduleBacklogItemFromReleaseUseCase;
import ntut.csie.releaseService.useCase.release.scheduledBacklogItem.unschedule.UnscheduleBacklogItemFromReleaseUseCaseImpl;

public class ScheduledBacklogItemUseCaseTest {
	private FakeReleaseRepository fakeReleaseRepository;
	private FakeEventStore fakeEventStore;
	
	private TestFactory testFactory;
	
	private String backlogItemId;
	private Release release;
	private String releaseId;
	
	@Before
	public void setUp() {
		fakeReleaseRepository = new FakeReleaseRepository();
		fakeEventStore = new FakeEventStore();
		DomainEventListener.getInstance().init(fakeEventStore);
		
		testFactory = new TestFactory(fakeReleaseRepository);
		
		String productId = "1";
		String name = "Release ezKanban.";
		String startDate = "2018-03-27";
		String endDate = "2018-09-27";
		String description = "1. Visualize Workflow 2. WIP Limit 3. Workflow Management";
		backlogItemId = "1";
		release = testFactory.newRelease(name, startDate, endDate, description, productId);
		releaseId = release.getReleaseId();
	}
	
	@After
	public void tearDown() {
		fakeReleaseRepository.clearAll();
		fakeEventStore.clearAll();
	}
	
	@Test
	public void Should_Success_When_ScheduleBacklogItemToRelease() {
		int originalNumberOfScheduledBacklogItems = release.getScheduledBacklogItems().size();
		
		ScheduleBacklogItemToReleaseOutput output = scheduleBacklogItemToRelease(backlogItemId, releaseId);
		
		int newNumberOfScheduledBacklogItems = release.getScheduledBacklogItems().size();
		
		assertEquals(1, newNumberOfScheduledBacklogItems - originalNumberOfScheduledBacklogItems);
		
		boolean isScheduleSuccess = output.isScheduleSuccess();
		assertTrue(isScheduleSuccess);
	}
	
	@Test
	public void Should_ReturnErrorMessage_When_ScheduleBacklogItemToNotExistRelease() {
		ScheduleBacklogItemToReleaseOutput output = scheduleBacklogItemToRelease(backlogItemId, null);
		
		boolean isScheduleSuccess = output.isScheduleSuccess();
		String errorMessage = output.getErrorMessage();
		String expectedErrorMessage = "Sorry, the release is not exist!";
		assertFalse(isScheduleSuccess);
		assertEquals(expectedErrorMessage, errorMessage);
	}
	
	@Test
	public void Should_ReturnErrorMessage_When_ScheduleBacklogItemToReleaseWithNullBacklogItemId() {
		ScheduleBacklogItemToReleaseOutput output = scheduleBacklogItemToRelease(null, releaseId);
		
		boolean isScheduleSuccess = output.isScheduleSuccess();
		String errorMessage = output.getErrorMessage();
		String expectedErrorMessage = "The backlog item id of the scheduled backlog item should be required!\n";
		assertFalse(isScheduleSuccess);
		assertEquals(expectedErrorMessage, errorMessage);
	}
	
	@Test
	public void Should_ReturnErrorMessage_When_ScheduleBacklogItemToReleaseWithEmptyBacklogItemId() {
		ScheduleBacklogItemToReleaseOutput output = scheduleBacklogItemToRelease("", releaseId);
		
		boolean isScheduleSuccess = output.isScheduleSuccess();
		String errorMessage = output.getErrorMessage();
		String expectedErrorMessage = "The backlog item id of the scheduled backlog item should be required!\n";
		assertFalse(isScheduleSuccess);
		assertEquals(expectedErrorMessage, errorMessage);
	}
	
	@Test
	public void Should_ReturnScheduledBacklogItemList_When_GetScheduledBacklogItemsOfRelease() {
		String[] backlogItemIds = {backlogItemId, "2"};
		
		int numberOfScheduledBacklogItems = backlogItemIds.length;
		
		for(int i = 0; i < numberOfScheduledBacklogItems; i++) {
			scheduleBacklogItemToRelease(backlogItemIds[i], releaseId);
		}
		
		GetScheduledBacklogItemsByReleaseIdOutput output = getScheduledBacklogItemsByReleaseId(releaseId);
		List<ScheduledBacklogItemModel> scheduledBacklogItemList = output.getScheduledBacklogItemList();
		
		for(int i = 0; i < numberOfScheduledBacklogItems; i++) {
			assertEquals(backlogItemIds[i], scheduledBacklogItemList.get(i).getBacklogItemId());
			assertEquals(releaseId, scheduledBacklogItemList.get(i).getReleaseId());
		}
		assertEquals(numberOfScheduledBacklogItems, scheduledBacklogItemList.size());
	}
	
	@Test
	public void Should_BacklogItemNotBelongToAnyRelease_When_UnscheduleBacklogItemFromRelease() {
		scheduleBacklogItemToRelease(backlogItemId, releaseId);
		int numberOfScheduledBacklogItemsBeforeUnscheduled = release.getScheduledBacklogItems().size();
		
		UnscheduleBacklogItemFromReleaseOutput output = unscheduleBacklogItemFromRelease(backlogItemId, releaseId);
		int numberOfScheduledBacklogItemsAfterUnscheduled = release.getScheduledBacklogItems().size();
		
		assertEquals(1, numberOfScheduledBacklogItemsBeforeUnscheduled - numberOfScheduledBacklogItemsAfterUnscheduled);
		
		boolean isUnscheduleSuccess = output.isUnscheduleSuccess();
		assertTrue(isUnscheduleSuccess);
	}
	
	@Test
	public void Should_ReturnErrorMessage_When_UnscheduleBacklogItemFromNotExistRelease() {
		UnscheduleBacklogItemFromReleaseOutput output = unscheduleBacklogItemFromRelease(backlogItemId, null);
		
		boolean isUnscheduleSuccess = output.isUnscheduleSuccess();
		String errorMessage = output.getErrorMessage();
		String expectedErrorMessage = "Sorry, the release is not exist!";
		assertFalse(isUnscheduleSuccess);
		assertEquals(expectedErrorMessage, errorMessage);
	}
	
	@Test
	public void Should_ReturnHistoryList_When_GetHistoriesOfBacklogItem() {
		scheduleBacklogItemToRelease(backlogItemId, releaseId);
		unscheduleBacklogItemFromRelease(backlogItemId, releaseId);
		
		GetHistoriesByBacklogItemIdOutput output = getHistoriesByBacklogItemId(backlogItemId);
		List<HistoryModel> historyList = output.getHistoryList();
		
		int numberOfHistories = 2;
		assertEquals(numberOfHistories, historyList.size());
	}
	
	private ScheduleBacklogItemToReleaseOutput scheduleBacklogItemToRelease(String backlogItemId, String releaseId) {
		ScheduleBacklogItemToReleaseUseCase scheduleBacklogItemToReleaseUseCase = new ScheduleBacklogItemToReleaseUseCaseImpl(fakeReleaseRepository);
		ScheduleBacklogItemToReleaseInput input = (ScheduleBacklogItemToReleaseInput) scheduleBacklogItemToReleaseUseCase;
		input.setBacklogItemId(backlogItemId);
		input.setReleaseId(releaseId);
		ScheduleBacklogItemToReleaseOutput output = new ScheduleBacklogItemToReleaseRestfulAPI();
		scheduleBacklogItemToReleaseUseCase.execute(input, output);
		return output;
	}
	
	private GetScheduledBacklogItemsByReleaseIdOutput getScheduledBacklogItemsByReleaseId(String releaseId) {
		GetScheduledBacklogItemsByReleaseIdUseCase getScheduledBacklogItemsByReleaseIdUseCase = new GetScheduledBacklogItemsByReleaseIdUseCaseImpl(fakeReleaseRepository);
		GetScheduledBacklogItemsByReleaseIdInput input = (GetScheduledBacklogItemsByReleaseIdInput) getScheduledBacklogItemsByReleaseIdUseCase;
		input.setReleaseId(releaseId);
		GetScheduledBacklogItemsByReleaseIdOutput output = new GetScheduledBacklogItemsByReleaseIdRestfulAPI();
		getScheduledBacklogItemsByReleaseIdUseCase.execute(input, output);
		return output;
	}
	
	private UnscheduleBacklogItemFromReleaseOutput unscheduleBacklogItemFromRelease(String backlogItemId, String releaseId) {
		UnscheduleBacklogItemFromReleaseUseCase unscheduleBacklogItemFromReleaseUseCase = new UnscheduleBacklogItemFromReleaseUseCaseImpl(fakeReleaseRepository);
		UnscheduleBacklogItemFromReleaseInput input = (UnscheduleBacklogItemFromReleaseInput) unscheduleBacklogItemFromReleaseUseCase;
		input.setBacklogItemId(backlogItemId);
		input.setReleaseId(releaseId);
		UnscheduleBacklogItemFromReleaseOutput output = new UnscheduleBacklogItemFromReleaseRestfulAPI();
		unscheduleBacklogItemFromReleaseUseCase.execute(input, output);
		return output;
	}
	
	private GetHistoriesByBacklogItemIdOutput getHistoriesByBacklogItemId(String backlogItemId) {
		GetHistoriesByBacklogItemIdUseCase getHistoriesByBacklogItemIdUseCase = new GetHistoriesByBacklogItemIdUseCaseImpl(fakeEventStore);
		GetHistoriesByBacklogItemIdInput input = (GetHistoriesByBacklogItemIdInput) getHistoriesByBacklogItemIdUseCase;
		input.setBacklogItemId(backlogItemId);
		GetHistoriesByBacklogItemIdOutput output = new GetHistoriesByBacklogItemIdRestfulAPI();
		getHistoriesByBacklogItemIdUseCase.execute(input, output);
		return output;
	}
}
