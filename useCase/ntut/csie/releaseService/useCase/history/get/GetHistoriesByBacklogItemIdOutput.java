package ntut.csie.releaseService.useCase.history.get;

import java.util.List;

import ntut.csie.releaseService.useCase.Output;
import ntut.csie.releaseService.useCase.history.HistoryModel;

public interface GetHistoriesByBacklogItemIdOutput extends Output{
	public List<HistoryModel> getHistoryList();
	
	public void setHistoryList(List<HistoryModel> historyList);
}
