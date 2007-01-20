package com.james.main;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.orm.hibernate3.SessionFactoryUtils;
import org.springframework.orm.hibernate3.SessionHolder;
import org.springframework.transaction.support.TransactionSynchronizationManager;

public class HibernateSessionInterceptor implements MethodInterceptor {

	protected Session session;

	protected SessionFactory sessionFactory;

	public Object invoke(MethodInvocation method) throws Throwable {

		// sessionFactory = (SessionFactory) CONTEXT.getBean("sessionFactory");

		session = SessionFactoryUtils.getSession(this.sessionFactory, true);
		TransactionSynchronizationManager.bindResource(this.sessionFactory,
				new SessionHolder(session));

		Object retValue = method.proceed();

		TransactionSynchronizationManager.unbindResource(sessionFactory);
		SessionFactoryUtils.releaseSession(session, sessionFactory);

		return retValue;
	}

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
}
