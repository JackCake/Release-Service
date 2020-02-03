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
	
	private String productId;

	@Before
	public void setUp() {
		fakeReleaseRepository = new FakeReleaseRepository();
		
		testFactory = new TestFactory(fakeReleaseRepository);
		
		productId = "1";
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
		
		boolean isAddSuccess = output.isAddSuccess();
		assertTrue(isAddSuccess);
	}
	
	@Test
	public void Should_ReturnErrorMessage_When_AddReleaseWithNullParamemters() {
		String name = null;
		String startDate = null;
		String endDate = null;
		String description = null;
		
		AddReleaseOutput output = addNewRelease(name, startDate, endDate, description, null);
		
		boolean isAddSuccess = output.isAddSuccess();
		String errorMessage = output.getErrorMessage();
		String expectedErrorMessage = "The name of the release should be required!\n" + 
				"The start date of the release should be required!\n" + 
				"The end date of the release should be required!\n" + 
				"The description of the release should be required!\n" + 
				"The product id of the release should be required!\n";
		assertFalse(isAddSuccess);
		assertEquals(expectedErrorMessage, errorMessage);
	}
	
	@Test
	public void Should_ReturnErrorMessage_When_AddReleaseWithEmptyParamemters() {
		String name = "";
		String startDate = "";
		String endDate = "";
		String description = "";
		
		AddReleaseOutput output = addNewRelease(name, startDate, endDate, description, "");
		
		boolean isAddSuccess = output.isAddSuccess();
		String errorMessage = output.getErrorMessage();
		String expectedErrorMessage = "The name of the release should be required!\n" + 
				"The start date of the release should be required!\n" + 
				"The end date of the release should be required!\n" + 
				"The description of the release should be required!\n" + 
				"The product id of the release should be required!\n";
		assertFalse(isAddSuccess);
		assertEquals(expectedErrorMessage, errorMessage);
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
		
		GetReleasesByProductIdOutput output = getReleasesByProductId(productId);
		List<ReleaseModel> releaseList = output.getReleaseList();
		
		for(int i = 0; i < numberOfNewReleases; i++) {
			assertEquals(names[i], releaseList.get(i).getName());
			assertEquals(startDates[i], releaseList.get(i).getStartDate());
			assertEquals(endDates[i], releaseList.get(i).getEndDate());
			assertEquals(descriptions[i], releaseList.get(i).getDescription());
		}
		assertEquals(numberOfNewReleases, releaseList.size());
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
	public void Should_ReturnErrorMessage_When_EditNotExistRelease() {
		String editedName = "Release ezScrum2019.";
		String editedStartDate = "2019-03-07";
		String editedEndDate = "2019-09-27";
		String editedDescription = "Implement the product backlog, the release plan, the sprint plan, the sprint backlog, and the retrospectve.";
		
		EditReleaseOutput output = editRelease(null, editedName, editedStartDate, editedEndDate, editedDescription, productId);
		
		boolean isEditSuccess = output.isEditSuccess();
		String errorMessage = output.getErrorMessage();
		String expectedErrorMessage = "Sorry, the release is not exist!";
		assertFalse(isEditSuccess);
		assertEquals(expectedErrorMessage, errorMessage);
	}
	
	@Test
	public void Should_ReturnErrorMessage_When_EditReleaseWithNullParamemters() {
		String name = "ezScrum v1.9";
		String startDate = "2019-01-23";
		String endDate = "2019-06-06";
		String description = "Implement the product backlog, the sprint plan, the sprint backlog, and the retrospectve.";
		
		Release editedRelease = testFactory.newRelease(name, startDate, endDate, description, productId);
		
		String editedName = null;
		String editedStartDate = null;
		String editedEndDate = null;
		String editedDescription = null;
		
		EditReleaseOutput output = editRelease(editedRelease.getReleaseId(), editedName, editedStartDate, editedEndDate, editedDescription, productId);
		
		boolean isEditSuccess = output.isEditSuccess();
		String errorMessage = output.getErrorMessage();
		String expectedErrorMessage = "The name of the release should be required!\n" + 
				"The start date of the release should be required!\n" + 
				"The end date of the release should be required!\n" + 
				"The description of the release should be required!\n";
		assertFalse(isEditSuccess);
		assertEquals(expectedErrorMessage, errorMessage);
	}
	
	@Test
	public void Should_ReturnErrorMessage_When_EditReleaseWithEmptyParamemters() {
		String name = "ezScrum v1.9";
		String startDate = "2019-01-23";
		String endDate = "2019-06-06";
		String description = "Implement the product backlog, the sprint plan, the sprint backlog, and the retrospectve.";
		
		Release editedRelease = testFactory.newRelease(name, startDate, endDate, description, productId);
		
		String editedName = "";
		String editedStartDate = "";
		String editedEndDate = "";
		String editedDescription = "";
		
		EditReleaseOutput output = editRelease(editedRelease.getReleaseId(), editedName, editedStartDate, editedEndDate, editedDescription, productId);
		
		boolean isEditSuccess = output.isEditSuccess();
		String errorMessage = output.getErrorMessage();
		String expectedErrorMessage = "The name of the release should be required!\n" + 
				"The start date of the release should be required!\n" + 
				"The end date of the release should be required!\n" + 
				"The description of the release should be required!\n";
		assertFalse(isEditSuccess);
		assertEquals(expectedErrorMessage, errorMessage);
	}
	
	@Test
	public void Should_Success_When_DeleteRelease() {
		String name = "Release ezScrum2019.";
		String startDate = "2018-09-28";
		String endDate = "2019-01-13";
		String description = "Implement the product backlog, the release plan, the sprint plan, the sprint backlog, and the retrospectve.";
		
		Release deletedRelease = testFactory.newRelease(name, startDate, endDate, description, productId);
		
		String deletedReleaseId = deletedRelease.getReleaseId();
		
		DeleteReleaseOutput output = deleteRelease(deletedReleaseId);
		
		boolean isDeleteSuccess = output.isDeleteSuccess();
		assertTrue(isDeleteSuccess);
	}
	
	@Test
	public void Should_ReturnErrorMessage_When_DeleteNotExistRelease() {
		DeleteReleaseOutput output = deleteRelease(null);
		
		boolean isDeleteSuccess = output.isDeleteSuccess();
		String errorMessage = output.getErrorMessage();
		String expectedErrorMessage = "Sorry, the release is not exist!";
		assertFalse(isDeleteSuccess);
		assertEquals(expectedErrorMessage, errorMessage);
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
		
		GetReleasesByProductIdOutput output = getReleasesByProductId(productId);
		List<ReleaseModel> releaseListAfterDeleted = output.getReleaseList();
		
		for(int i = 0; i < releaseListAfterDeleted.size(); i++) {
			assertEquals(i + 1, releaseListAfterDeleted.get(i).getOrderId());
		}
	}
	
	@Test
	public void Should_ReturnOverlapMessage_When_AddOverlapRelease() {
		String name = "ezScrum v1.9";
		String startDate = "2019-01-23";
		String endDate = "2019-06-06";
		String description = "Implement the product backlog, the sprint plan, the sprint backlog, and the retrospectve.";
		
		testFactory.newRelease(name, startDate, endDate, description, productId);
		
		String overlapName = "Release ezScrum2019.";
		String overlapStartDate = "2019-03-27";
		String overlapEndDate = "2019-09-27";
		String overlapDescription = "Implement the product backlog, the release plan, the sprint plan, the sprint backlog, and the retrospectve.";
		
		AddReleaseOutput output = addNewRelease(overlapName, overlapStartDate, overlapEndDate, overlapDescription, productId);
		
		boolean isAddSuccess = output.isAddSuccess();
		String errorMessage = output.getErrorMessage();
		String expectedErrorMessage = "Sorry, the start date or the end date is overlap with the other release!";
		assertFalse(isAddSuccess);
		assertEquals(expectedErrorMessage, errorMessage);
	}
	
	@Test
	public void Should_Success_When_EditOverlapRelease() {
		String name = "ezScrum v1.9";
		String startDate = "2019-01-23";
		String endDate = "2019-06-06";
		String description = "Implement the product backlog, the sprint plan, the sprint backlog, and the retrospectve.";
		
		testFactory.newRelease(name, startDate, endDate, description, productId);
		
		String overlapName = "Implement ezScrum2019.";
		String overlapStartDate = "2018-03-27";
		String overlapEndDate = "2018-09-27";
		String overlapDescription = "Implement the product backlog, the release plan, the sprint plan, the sprint backlog, and the retrospectve.";
		
		Release editedOverlapRelease = testFactory.newRelease(overlapName, overlapStartDate, overlapEndDate, overlapDescription, productId);
		
		String editedOverlapReleaseId = editedOverlapRelease.getReleaseId();
		String editedOverlapName = "Release ezScrum2019.";
		String editedOverlapStartDate = "2019-03-27";
		String editedOverlapEndDate = "2019-09-27";
		String editedOverlapDescription = "Implement the feature about the tag and the attach file.";
		
		EditReleaseOutput output = editRelease(editedOverlapReleaseId, editedOverlapName, editedOverlapStartDate, editedOverlapEndDate, editedOverlapDescription, productId);
		
		boolean isEditSuccess = output.isEditSuccess();
		String errorMessage = output.getErrorMessage();
		String expectedErrorMessage = "Sorry, the start date or the end date is overlap with the other release!";
		assertFalse(isEditSuccess);
		assertEquals(expectedErrorMessage, errorMessage);
	}
	
	@Test
	public void Should_ReturnErrorMessage_When_AddReleaseWithStartDateAfterEndDate() {
		String name = "Release ezScrum2019.";
		String startDate = "2018-09-28";
		String endDate = "2018-09-27";
		String description = "Implement the product backlog, the release plan, the sprint plan, the sprint backlog, and the retrospectve.";
		
		AddReleaseOutput output = addNewRelease(name, startDate, endDate, description, productId);
		
		boolean isAddSuccess = output.isAddSuccess();
		String errorMessage = output.getErrorMessage();
		String expectedErrorMessage = "Sorry, the start date must be before the end date!";
		assertFalse(isAddSuccess);
		assertEquals(expectedErrorMessage, errorMessage);
	}
	
	@Test
	public void Should_ReturnErrorMessage_When_EditReleaseWithStartDateAfterEndDate() {
		String name = "ezScrum v1.9";
		String startDate = "2019-01-23";
		String endDate = "2019-06-06";
		String description = "Implement the product backlog, the sprint plan, the sprint backlog, and the retrospectve.";
		
		Release editedRelease = testFactory.newRelease(name, startDate, endDate, description, productId);
		
		String editedName = "Release ezScrum2019.";
		String editedStartDate = "2019-03-07";
		String editedEndDate = "2019-03-06";
		String editedDescription = "Implement the product backlog, the release plan, the sprint plan, the sprint backlog, and the retrospectve.";
		
		EditReleaseOutput output = editRelease(editedRelease.getReleaseId(), editedName, editedStartDate, editedEndDate, editedDescription, productId);
		
		boolean isEditSuccess = output.isEditSuccess();
		String errorMessage = output.getErrorMessage();
		String expectedErrorMessage = "Sorry, the start date must be before the end date!";
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
	
	private GetReleasesByProductIdOutput getReleasesByProductId(String productId) {
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
