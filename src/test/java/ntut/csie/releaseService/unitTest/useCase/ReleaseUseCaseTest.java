package ntut.csie.releaseService.unitTest.useCase;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import ntut.csie.releaseService.controller.release.AddReleaseRestfulAPI;
import ntut.csie.releaseService.controller.release.DeleteReleaseRestfulAPI;
import ntut.csie.releaseService.controller.release.EditReleaseRestfulAPI;
import ntut.csie.releaseService.controller.release.GetReleasesByProductIdRestfulAPI;
import ntut.csie.releaseService.model.release.Release;
import ntut.csie.releaseService.unitTest.factory.TestFactory;
import ntut.csie.releaseService.unitTest.repository.FakeReleaseRepository;
import ntut.csie.releaseService.useCase.release.ReleaseModel;
import ntut.csie.releaseService.useCase.release.add.AddReleaseInput;
import ntut.csie.releaseService.useCase.release.add.AddReleaseOutput;
import ntut.csie.releaseService.useCase.release.add.AddReleaseUseCase;
import ntut.csie.releaseService.useCase.release.add.AddReleaseUseCaseImpl;
import ntut.csie.releaseService.useCase.release.delete.DeleteReleaseInput;
import ntut.csie.releaseService.useCase.release.delete.DeleteReleaseOutput;
import ntut.csie.releaseService.useCase.release.delete.DeleteReleaseUseCase;
import ntut.csie.releaseService.useCase.release.delete.DeleteReleaseUseCaseImpl;
import ntut.csie.releaseService.useCase.release.edit.EditReleaseInput;
import ntut.csie.releaseService.useCase.release.edit.EditReleaseOutput;
import ntut.csie.releaseService.useCase.release.edit.EditReleaseUseCase;
import ntut.csie.releaseService.useCase.release.edit.EditReleaseUseCaseImpl;
import ntut.csie.releaseService.useCase.release.get.GetReleasesByProductIdInput;
import ntut.csie.releaseService.useCase.release.get.GetReleasesByProductIdOutput;
import ntut.csie.releaseService.useCase.release.get.GetReleasesByProductIdUseCase;
import ntut.csie.releaseService.useCase.release.get.GetReleasesByProductIdUseCaseImpl;

public class ReleaseUseCaseTest {
	private FakeReleaseRepository fakeReleaseRepository;
	
	private TestFactory testFactory;
	
	private Release release;
	private String productId;

	@Before
	public void setUp() {
		fakeReleaseRepository = new FakeReleaseRepository();
		
		testFactory = new TestFactory(fakeReleaseRepository);
		
		productId = "1";
		String name = "Release ezKanban.";
		String startDate = "2018-03-27";
		String endDate = "2018-09-27";
		String description = "1. Visualize Workflow 2. WIP Limit 3. Workflow Management";
		release = testFactory.newRelease(name, startDate, endDate, description, productId);
	}
	
	@After
	public void tearDown() {
		fakeReleaseRepository.clearAll();
	}
	
	@Test
	public void Should_Success_When_AddRelease() {
		String name = "Release ezScrum2019.";
		String startDate = "2018-09-28";
		String endDate = "2019-01-13";
		String description = "Implement the product backlog, the release plan, the sprint plan, the sprint backlog, and the retrospectve.";
		
		AddReleaseOutput output = addNewRelease(name, startDate, endDate, description, productId);
		
		assertTrue(output.isAddSuccess());
	}
	
	@Test
	public void Should_ReturnErrorMessage_When_AddReleaseWithEmptyParamemter() {
		String expectedErrorMessage = "The name of the release should not be null.\n" +
				"The start date of the release should not be null.\n" +
				"The end date of the release should not be null.\n" +
				"The description of the release should not be null.\n";
		
		AddReleaseOutput output = addNewRelease(null, null, null, null, productId);
		
		assertFalse(output.isAddSuccess());
		assertEquals(expectedErrorMessage, output.getErrorMessage());
	}
	
	@Test
	public void Should_ReturnReleaseList_When_GetReleasesOfProduct() {
		String[] names = {
				"Release#1",
				"Release#2",
				"Release#3"
		};
		String[] startDates = {"2019-01-12", "2019-03-29", "2019-06-15"};
		String[] endDates = {"2019-03-01", "2019-05-14", "2019-08-28"};
		String[] descriptions = {
			"This is the description of the release#1.", 
			"This is the description of the release#2.", 
			"This is the description of the release#3."
		};
		
		int numberOfNewReleases = names.length;
		
		for(int i = 0; i < numberOfNewReleases; i++) {
			testFactory.newRelease(names[i], startDates[i], endDates[i], descriptions[i], productId);
		}
		
		GetReleasesByProductIdOutput output = getReleasesByProductId();
		List<ReleaseModel> releaseList = output.getReleaseList();
		
		assertEquals(release.getName(), releaseList.get(0).getName());
		assertEquals(release.getStartDate(), releaseList.get(0).getStartDate());
		assertEquals(release.getEndDate(), releaseList.get(0).getEndDate());
		assertEquals(release.getDescription(), releaseList.get(0).getDescription());
		for(int i = 0; i < numberOfNewReleases; i++) {
			assertEquals(names[i], releaseList.get(i + 1).getName());
			assertEquals(startDates[i], releaseList.get(i + 1).getStartDate());
			assertEquals(endDates[i], releaseList.get(i + 1).getEndDate());
			assertEquals(descriptions[i], releaseList.get(i + 1).getDescription());
		}
		int expectedNumberOfReleases = 1 + numberOfNewReleases;
		assertEquals(expectedNumberOfReleases, releaseList.size());
	}
	
	@Test
	public void Should_Success_When_EditRelease() {
		String name = "ezScrum v1.9";
		String startDate = "2019-01-23";
		String endDate = "2019-06-06";
		String description = "Implement the product backlog, the sprint plan, the sprint backlog, and the retrospectve.";
		
		Release editedRelease = testFactory.newRelease(name, startDate, endDate, description, productId);
		
		String editedName = "Release ezScrum2019.";
		String editedStartDate = "2019-03-07";
		String editedEndDate = "2019-09-27";
		String editedDescription = "Implement the product backlog, the release plan, the sprint plan, the sprint backlog, and the retrospectve.";
		
		EditReleaseOutput output = editRelease(editedRelease.getReleaseId(), editedName, editedStartDate, editedEndDate, editedDescription, productId);
		
		boolean isEditSuccess = output.isEditSuccess();
		assertTrue(isEditSuccess);
	}
	
	@Test
	public void Should_ReturnErrorMessage_When_EditRelease() {
		String editedName = "Release ezScrum2019.";
		String editedStartDate = "2019-03-07";
		String editedEndDate = "2019-09-27";
		String editedDescription = "Implement the product backlog, the release plan, the sprint plan, the sprint backlog, and the retrospectve.";
		
		EditReleaseOutput output = editRelease(null, editedName, editedStartDate, editedEndDate, editedDescription, productId);
		
		
		boolean isEditSuccess = output.isEditSuccess();
		String expectedErrorMessage = "Sorry, the sprint is not exist.";
		assertFalse(isEditSuccess);
		assertEquals(expectedErrorMessage, output.getErrorMessage());
	}
	
	@Test
	public void Should_Success_When_DeleteRelease() {
		DeleteReleaseOutput output = deleteRelease(release.getReleaseId());
		
		boolean isDeleteSuccess = output.isDeleteSuccess();
		assertTrue(isDeleteSuccess);
	}
	
	@Test
	public void Should_ReturnErrorMessage_When_DeleteRelease() {
		DeleteReleaseOutput output = deleteRelease(null);
		
		boolean isDeleteSuccess = output.isDeleteSuccess();
		String expectedErrorMessage = "Sorry, the release is not exist.";
		assertFalse(isDeleteSuccess);
		assertEquals(expectedErrorMessage, output.getErrorMessage());
	}
	
	@Test
	public void Should_OrderIdUpdated_When_DeleteRelease() {
		String[] names = {
				"Release#1",
				"Release#2",
				"Release#3"
		};
		String[] startDates = {"2019-01-12", "2019-03-29", "2019-06-15"};
		String[] endDates = {"2019-03-01", "2019-05-14", "2019-08-28"};
		String[] descriptions = {
			"This is the description of the release#1.", 
			"This is the description of the release#2.", 
			"This is the description of the release#3."
		};
		
		int numberOfNewReleases = names.length;
		
		for(int i = 0; i < numberOfNewReleases; i++) {
			testFactory.newRelease(names[i], startDates[i], endDates[i], descriptions[i], productId);
		}
		
		List<Release> releaseList = new ArrayList<>(fakeReleaseRepository.getReleasesByProductId(productId));
		String deletedReleaseId = releaseList.get(1).getReleaseId();
		deleteRelease(deletedReleaseId);
		
		GetReleasesByProductIdOutput output = getReleasesByProductId();
		List<ReleaseModel> releaseListAfterDeleted = output.getReleaseList();
		
		for(int i = 0; i < releaseListAfterDeleted.size(); i++) {
			assertEquals(i + 1, releaseListAfterDeleted.get(i).getOrderId());
		}
	}
	
	@Test
	public void Should_ReturnOverlapMessage_When_AddOverlapRelease() {
		String overlapName = "Release ezScrum2019.";
		String overlapStartDate = "2018-03-27";
		String overlapEndDate = "2018-09-27";
		String overlapDescription = "Implement the product backlog, the release plan, the sprint plan, the sprint backlog, and the retrospectve.";
		
		
		AddReleaseOutput output = addNewRelease(overlapName, overlapStartDate, overlapEndDate, overlapDescription, productId);
		boolean isAddSuccess = output.isAddSuccess();
		String errorMessage = output.getErrorMessage();
		
		String expectedErrorMessage = "Sorry, the start date or the end date is overlap with the other release.";
		assertFalse(isAddSuccess);
		assertEquals(expectedErrorMessage, errorMessage);
	}
	
	@Test
	public void Should_Success_When_EditOverlapRelease() {
		String name = "ezScrum v1.9";
		String startDate = "2019-01-23";
		String endDate = "2019-06-06";
		String description = "Implement the product backlog, the sprint plan, the sprint backlog, and the retrospectve.";
		
		Release editedRelease = testFactory.newRelease(name, startDate, endDate, description, productId);
		
		String overlapName = "Release ezScrum2019.";
		String overlapStartDate = "2018-03-27";
		String overlapEndDate = "2018-09-27";
		String overlapDescription = "Implement the product backlog, the release plan, the sprint plan, the sprint backlog, and the retrospectve.";
		
		EditReleaseOutput output = editRelease(editedRelease.getReleaseId(), overlapName, overlapStartDate, overlapEndDate, overlapDescription, productId);
		boolean isEditSuccess = output.isEditSuccess();
		String errorMessage = output.getErrorMessage();
		
		String expectedErrorMessage = "Sorry, the start date or the end date is overlap with the other release.";
		assertFalse(isEditSuccess);
		assertEquals(expectedErrorMessage, errorMessage);
	}
	
	private AddReleaseOutput addNewRelease(String name, String startDate, String endDate, String description, String productId) {
		AddReleaseUseCase addReleaseUseCase = new AddReleaseUseCaseImpl(fakeReleaseRepository);
		AddReleaseInput input = (AddReleaseInput) addReleaseUseCase;
		input.setName(name);
		input.setStartDate(startDate);
		input.setEndDate(endDate);
		input.setDescription(description);
		input.setProductId(productId);
		AddReleaseOutput output = new AddReleaseRestfulAPI();
		addReleaseUseCase.execute(input, output);
		return output;
		
	}
	
	private GetReleasesByProductIdOutput getReleasesByProductId() {
		GetReleasesByProductIdUseCase getReleasesByProductIdUseCase = new GetReleasesByProductIdUseCaseImpl(fakeReleaseRepository);
		GetReleasesByProductIdInput input = (GetReleasesByProductIdInput) getReleasesByProductIdUseCase;
		input.setProductId(productId);
		GetReleasesByProductIdOutput output = new GetReleasesByProductIdRestfulAPI();
		getReleasesByProductIdUseCase.execute(input, output);
		return output;
	}
	
	private EditReleaseOutput editRelease(String releaseId, String editedName, String editedStartDate, 
			String editedEndDate, String editedDescription, String productId) {
		EditReleaseUseCase editReleaseUseCase = new EditReleaseUseCaseImpl(fakeReleaseRepository);
		EditReleaseInput input = (EditReleaseInput) editReleaseUseCase;
		input.setReleaseId(releaseId);
		input.setName(editedName);
		input.setStartDate(editedStartDate);
		input.setEndDate(editedEndDate);
		input.setDescription(editedDescription);
		input.setProductId(productId);
		EditReleaseOutput output = new EditReleaseRestfulAPI();
		editReleaseUseCase.execute(input, output);
		return output;
	}
	
	private DeleteReleaseOutput deleteRelease(String releaseId) {
		DeleteReleaseUseCase deleteReleaseUseCase = new DeleteReleaseUseCaseImpl(fakeReleaseRepository);
		DeleteReleaseInput input = (DeleteReleaseInput) deleteReleaseUseCase;
		input.setReleaseId(releaseId);
		DeleteReleaseOutput output = new DeleteReleaseRestfulAPI();
		deleteReleaseUseCase.execute(input, output);
		return output;
	}
}
