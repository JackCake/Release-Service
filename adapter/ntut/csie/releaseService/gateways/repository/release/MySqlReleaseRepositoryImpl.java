package ntut.csie.releaseService.gateways.repository.release;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import ntut.csie.releaseService.gateways.database.ReleaseTable;
import ntut.csie.releaseService.gateways.database.ScheduledBacklogItemTable;
import ntut.csie.releaseService.gateways.database.SqlDatabaseHelper;
import ntut.csie.releaseService.gateways.repository.release.scheduledBacklogItem.ScheduledBacklogItemData;
import ntut.csie.releaseService.gateways.repository.release.scheduledBacklogItem.ScheduledBacklogItemMapper;
import ntut.csie.releaseService.model.release.Release;
import ntut.csie.releaseService.model.release.ScheduledBacklogItem;
import ntut.csie.releaseService.useCase.release.ReleaseRepository;

public class MySqlReleaseRepositoryImpl implements ReleaseRepository {
	private SqlDatabaseHelper sqlDatabaseHelper;
	private ReleaseMapper sprintMapper;
	private ScheduledBacklogItemMapper scheduledBacklogItemMapper;
	
	public MySqlReleaseRepositoryImpl() {
		sqlDatabaseHelper = new SqlDatabaseHelper();
		sprintMapper = new ReleaseMapper();
		scheduledBacklogItemMapper = new ScheduledBacklogItemMapper();
	}

	@Override
	public synchronized void save(Release release) throws Exception {
		sqlDatabaseHelper.connectToDatabase();
		PreparedStatement preparedStatement = null;
		try {
			sqlDatabaseHelper.transactionStart();
			//當記憶體中的release下的scheduledBacklogItem被移除時，那麼資料庫也必須被同步刪除
			Release releaseInDatabase = getReleaseById(release.getReleaseId());
			if(releaseInDatabase != null) {
				Set<String> backlogItemIds = new HashSet<>();
				for(ScheduledBacklogItem scheduledBacklogItem : release.getScheduledBacklogItems()) {
					backlogItemIds.add(scheduledBacklogItem.getBacklogItemId());
				}
				for(ScheduledBacklogItem scheduledBacklogItem : releaseInDatabase.getScheduledBacklogItems()) {
					if(!backlogItemIds.contains(scheduledBacklogItem.getBacklogItemId())) {
						removeScheduledBacklogItem(scheduledBacklogItem);
					}
				}
				
				//開始儲存
				for(ScheduledBacklogItem scheduledBacklogItem : release.getScheduledBacklogItems()) {
					addScheduledBacklogItem(scheduledBacklogItem);
				}
			}
			
			ReleaseData data = sprintMapper.transformToSprintData(release);
			String sql = String.format("Insert Into %s Values (?, ?, ?, ?, ?, ?, ?) "
					+ "On Duplicate Key Update %s=?, %s=?, %s=?, %s=?, %s=?",
					ReleaseTable.tableName, ReleaseTable.orderId, ReleaseTable.name, 
					ReleaseTable.startDate, ReleaseTable.endDate, ReleaseTable.description);
			preparedStatement = sqlDatabaseHelper.getPreparedStatement(sql);
			preparedStatement.setString(1, data.getReleaseId());
			preparedStatement.setInt(2, data.getOrderId());
			preparedStatement.setString(3, data.getName());
			preparedStatement.setString(4, data.getStartDate());
			preparedStatement.setString(5, data.getEndDate());
			preparedStatement.setString(6, data.getDescription());
			preparedStatement.setString(7, data.getProductId());
			preparedStatement.setInt(8, data.getOrderId());
			preparedStatement.setString(9, data.getName());
			preparedStatement.setString(10, data.getStartDate());
			preparedStatement.setString(11, data.getEndDate());
			preparedStatement.setString(12, data.getDescription());
			preparedStatement.executeUpdate();
			sqlDatabaseHelper.transactionEnd();
		} catch(SQLException e) {
			sqlDatabaseHelper.transactionError();
			e.printStackTrace();
			throw new Exception("Sorry, there is the database problem when save the release. Please contact to the system administrator!");
		} finally {
			sqlDatabaseHelper.closePreparedStatement(preparedStatement);
			sqlDatabaseHelper.releaseConnection();
		}
	}

	@Override
	public synchronized void remove(Release release) throws Exception {
		sqlDatabaseHelper.connectToDatabase();
		PreparedStatement preparedStatement = null;
		try {
			sqlDatabaseHelper.transactionStart();
			for(ScheduledBacklogItem scheduledBacklogItem : release.getScheduledBacklogItems()) {
				removeScheduledBacklogItem(scheduledBacklogItem);
			}
			
			ReleaseData data = sprintMapper.transformToSprintData(release);
			String sql = String.format("Delete From %s Where %s = ?",
					ReleaseTable.tableName,
					ReleaseTable.releaseId);
			preparedStatement = sqlDatabaseHelper.getPreparedStatement(sql);
			preparedStatement.setString(1, data.getReleaseId());
			preparedStatement.executeUpdate();
			sqlDatabaseHelper.transactionEnd();
		} catch(SQLException e) {
			sqlDatabaseHelper.transactionError();
			e.printStackTrace();
			throw new Exception("Sorry, there is the database problem when remove the release. Please contact to the system administrator!");
		} finally {
			sqlDatabaseHelper.closePreparedStatement(preparedStatement);
			sqlDatabaseHelper.releaseConnection();
		}
	}

	@Override
	public synchronized Release getReleaseById(String releaseId) {
		if(!sqlDatabaseHelper.isTransacting()) {
			sqlDatabaseHelper.connectToDatabase();
		}
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		Release release = null;
		try {
			String sql = String.format("Select * From %s Where %s = ?",
					ReleaseTable.tableName,
					ReleaseTable.releaseId);
			preparedStatement = sqlDatabaseHelper.getPreparedStatement(sql);
			preparedStatement.setString(1, releaseId);
			resultSet = preparedStatement.executeQuery();
			if (resultSet.first()) {
				int orderId = resultSet.getInt(ReleaseTable.orderId);
				String name = resultSet.getString(ReleaseTable.name);
				String startDate = resultSet.getString(ReleaseTable.startDate);
				String endDate = resultSet.getString(ReleaseTable.endDate);
				String description = resultSet.getString(ReleaseTable.description);
				String productId = resultSet.getString(ReleaseTable.productId);
				
				ReleaseData data = new ReleaseData();
				data.setReleaseId(releaseId);
				data.setOrderId(orderId);
				data.setName(name);
				data.setStartDate(startDate);
				data.setEndDate(endDate);
				data.setDescription(description);
				data.setProductId(productId);
				data.setScheduledBacklogItemDatas(getScheduledBacklogItemDatasByReleaseId(releaseId));

				release = sprintMapper.transformToRelease(data);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			sqlDatabaseHelper.closeResultSet(resultSet);
			sqlDatabaseHelper.closePreparedStatement(preparedStatement);
			if(!sqlDatabaseHelper.isTransacting()) {
				sqlDatabaseHelper.releaseConnection();
			}
		}
		return release;
	}
	
	@Override
	public synchronized Collection<Release> getReleasesByProductId(String productId){
		if(!sqlDatabaseHelper.isTransacting()) {
			sqlDatabaseHelper.connectToDatabase();
		}
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		Collection<Release> sprints = new ArrayList<>();
		try {
			String sql = String.format("Select * From %s Where %s = ? Order By %s",
					ReleaseTable.tableName, 
					ReleaseTable.productId, 
					ReleaseTable.orderId);
			preparedStatement = sqlDatabaseHelper.getPreparedStatement(sql);
			preparedStatement.setString(1, productId);
			resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				String releaseId = resultSet.getString(ReleaseTable.releaseId);
				int orderId = resultSet.getInt(ReleaseTable.orderId);
				String name = resultSet.getString(ReleaseTable.name);
				String startDate = resultSet.getString(ReleaseTable.startDate);
				String endDate = resultSet.getString(ReleaseTable.endDate);
				String description = resultSet.getString(ReleaseTable.description);
				
				ReleaseData data = new ReleaseData();
				data.setReleaseId(releaseId);
				data.setOrderId(orderId);
				data.setName(name);
				data.setStartDate(startDate);
				data.setEndDate(endDate);
				data.setDescription(description);
				data.setProductId(productId);
				data.setScheduledBacklogItemDatas(getScheduledBacklogItemDatasByReleaseId(releaseId));

				Release release = sprintMapper.transformToRelease(data);
				sprints.add(release);
			}
			resultSet.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			sqlDatabaseHelper.closeResultSet(resultSet);
			sqlDatabaseHelper.closePreparedStatement(preparedStatement);
			if(!sqlDatabaseHelper.isTransacting()) {
				sqlDatabaseHelper.releaseConnection();
			}
		}
		return sprints;
	}
	
	private void addScheduledBacklogItem(ScheduledBacklogItem scheduledBacklogItem) throws SQLException {
		ScheduledBacklogItemData data = scheduledBacklogItemMapper.transformToScheduledBacklogItemData(scheduledBacklogItem);
		String sql = String.format("Insert Into %s Values (?, ?) On Duplicate Key Update %s=?",
				ScheduledBacklogItemTable.tableName, ScheduledBacklogItemTable.releaseId);
		PreparedStatement preparedStatement = sqlDatabaseHelper.getPreparedStatement(sql);
		preparedStatement.setString(1, data.getBacklogItemId());
		preparedStatement.setString(2, data.getReleaseId());
		preparedStatement.setString(3, data.getReleaseId());
		preparedStatement.executeUpdate();
		sqlDatabaseHelper.closePreparedStatement(preparedStatement);
	}

	private void removeScheduledBacklogItem(ScheduledBacklogItem scheduledBacklogItem) throws SQLException {
		ScheduledBacklogItemData data = scheduledBacklogItemMapper.transformToScheduledBacklogItemData(scheduledBacklogItem);
		String sql = String.format("Delete From %s Where %s = ?",
				ScheduledBacklogItemTable.tableName,
				ScheduledBacklogItemTable.backlogItemId);
		PreparedStatement preparedStatement = sqlDatabaseHelper.getPreparedStatement(sql);
		preparedStatement.setString(1, data.getBacklogItemId());
		preparedStatement.executeUpdate();
		sqlDatabaseHelper.closePreparedStatement(preparedStatement);
	}
	
	private Collection<ScheduledBacklogItemData> getScheduledBacklogItemDatasByReleaseId(String releaseId){
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		Collection<ScheduledBacklogItemData> scheduledBacklogItemDatas = new ArrayList<>();
		try {
			String sql = String.format("Select * From %s Where %s = ?",
					ScheduledBacklogItemTable.tableName, 
					ScheduledBacklogItemTable.releaseId);
			preparedStatement = sqlDatabaseHelper.getPreparedStatement(sql);
			preparedStatement.setString(1, releaseId);
			resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				String backlogItemId = resultSet.getString(ScheduledBacklogItemTable.backlogItemId);
				
				ScheduledBacklogItemData data = new ScheduledBacklogItemData();
				data.setBacklogItemId(backlogItemId);
				data.setReleaseId(releaseId);

				scheduledBacklogItemDatas.add(data);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			sqlDatabaseHelper.closeResultSet(resultSet);
			sqlDatabaseHelper.closePreparedStatement(preparedStatement);
		}
		return scheduledBacklogItemDatas;
	}
}
