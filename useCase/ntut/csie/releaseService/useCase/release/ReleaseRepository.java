package ntut.csie.releaseService.useCase.release;

import java.util.Collection;

import ntut.csie.releaseService.model.release.Release;

public interface ReleaseRepository {
	public void save(Release release) throws Exception;
	
	public void remove(Release release) throws Exception;
	
	public Release getReleaseById(String releaseId);
	
	public Collection<Release> getReleasesByProductId(String productId);
}
