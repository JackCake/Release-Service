package ntut.csie.releaseService.gateways.repository.event;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import ntut.csie.releaseService.gateways.database.EventTable;
import ntut.csie.releaseService.gateways.database.SqlDatabaseHelper;
import ntut.csie.releaseService.model.DomainEvent;
import ntut.csie.releaseService.useCase.EventStore;

public class MySqlEventStoreImpl implements EventStore {
	private SqlDatabaseHelper sqlDatabaseHelper;
	private EventSerializer eventSerializer;
	
	public MySqlEventStoreImpl() {
		sqlDatabaseHelper = new SqlDatabaseHelper();
		this.setEventSerializer(EventSerializer.instance());
	}

	@Override
	public synchronized void save(DomainEvent event) {
		sqlDatabaseHelper.connectToDatabase();
		PreparedStatement preparedStatement = null;
		try {
			StoredEvent storedEvent = new StoredEvent(
					this.getEventSerializer().serialize(event),
					event.getClass().getName());
			String sql = String.format("Insert Into %s (%s, %s) Values (?, ?)", 
					EventTable.tableName, 
					EventTable.eventBody, 
					EventTable.eventType);
			preparedStatement = sqlDatabaseHelper.getPreparedStatement(sql);
			preparedStatement.setString(1, storedEvent.getEventBody());
			preparedStatement.setString(2, storedEvent.getEventType());
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			sqlDatabaseHelper.closePreparedStatement(preparedStatement);
			sqlDatabaseHelper.releaseConnection();
		}
	}
	
	@Override
	public synchronized List<DomainEvent> getByEventType(String eventType) {
		sqlDatabaseHelper.connectToDatabase();
		ResultSet resultSet = null;
		List<DomainEvent> events = new ArrayList<>();
		try {
			String query = String.format("Select * From %s Where %s = '%s' Order By %s",
					EventTable.tableName,
					EventTable.eventType,
					eventType,
					EventTable.eventId);
			resultSet = sqlDatabaseHelper.getResultSet(query);
			while (resultSet.next()) {
				String eventBody = resultSet.getString(EventTable.eventBody);
				
				StoredEvent storedEvent = new StoredEvent(eventBody, eventType);
				
				DomainEvent event = storedEvent.toDomainEvent();
				events.add(event);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			sqlDatabaseHelper.closeResultSet(resultSet);
			sqlDatabaseHelper.releaseConnection();
		}
		return events;
	}

	@Override
	public synchronized List<DomainEvent> getAllEvent() {
		sqlDatabaseHelper.connectToDatabase();
		ResultSet resultSet = null;
		List<DomainEvent> events = new ArrayList<>();
		try {
			String query = String.format("Select * From %s Order By %s",
					EventTable.tableName,
					EventTable.eventId);
			resultSet = sqlDatabaseHelper.getResultSet(query);
			while (resultSet.next()) {
				String eventBody = resultSet.getString(EventTable.eventBody);
				String eventType = resultSet.getString(EventTable.eventType);
				
				StoredEvent storedEvent = new StoredEvent(eventBody, eventType);
				DomainEvent event = storedEvent.toDomainEvent();
				
				events.add(event);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			sqlDatabaseHelper.closeResultSet(resultSet);
			sqlDatabaseHelper.releaseConnection();
		}
		return events;
	}
	
	public EventSerializer getEventSerializer() {
		return eventSerializer;
	}

	public void setEventSerializer(EventSerializer eventSerializer) {
		this.eventSerializer = eventSerializer;
	}
}
