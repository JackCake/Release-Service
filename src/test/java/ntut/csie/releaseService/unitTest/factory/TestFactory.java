package ntut.csie.releaseService.unitTest.factory;

import ntut.csie.releaseService.model.release.Release;
import ntut.csie.releaseService.model.release.ReleaseBuilder;
import ntut.csie.releaseService.unitTest.repository.FakeReleaseRepository;

public class TestFactory {
	private FakeReleaseRepository fakeReleaseRepository;
	
	public TestFactory(FakeReleaseRepository fakeReleaseRepository) {
		this.fakeReleaseRepository = fakeReleaseRepository;
	}
	
	public Release newRelease(String name, String startDate, String endDate, String description, String productId) {
		int orderId = fakeReleaseRepository.getReleasesByProductId(productId).size() + 1;
		Release release = null;
		try {
			release = ReleaseBuilder.newInstance().
					orderId(orderId).
					name(name).
					startDate(startDate).
					endDate(endDate).
					description(description).
					productId(productId).
					build();
		} catch(Exception e) {
			System.out.println(e.getMessage());
		}
		fakeReleaseRepository.save(release);
		return release;
	}
}
