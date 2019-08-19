package ntut.csie.releaseService;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.http.HttpServlet;

import ntut.csie.releaseService.gateways.database.SqlDatabaseHelper;
import ntut.csie.releaseService.useCase.DomainEventListener;

@SuppressWarnings("serial")
public class ReleaseServiceStart extends HttpServlet implements ServletContextListener {
	@Override
	public void contextDestroyed(ServletContextEvent arg0) {

	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		System.out.println("Release Service Start!");
		SqlDatabaseHelper sqlDatabaseHelper = new SqlDatabaseHelper();
		sqlDatabaseHelper.initialize();
		ApplicationContext context = ApplicationContext.getInstance();
		DomainEventListener.getInstance().init(context.newEventStore());
	}
}