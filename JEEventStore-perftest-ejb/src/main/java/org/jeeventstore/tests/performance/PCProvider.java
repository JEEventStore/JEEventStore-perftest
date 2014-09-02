package org.jeeventstore.tests.performance;

import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.jeeventstore.persistence.jpa.PersistenceContextProvider;

@Singleton
public class PCProvider implements PersistenceContextProvider {

    @PersistenceContext(unitName="TestPU")
    private EntityManager entityManager;

    @Override
    @Lock(LockType.READ)
    public EntityManager entityManagerForReading(String bucketId) {
        return entityManager;
    }

    @Override
    @Lock(LockType.READ)
    public EntityManager entityManagerForWriting(String bucketId) {
        return entityManager;
    }
    
}
