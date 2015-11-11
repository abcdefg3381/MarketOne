package marketone.db.handlerbean;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import marketone.db.entity.Index;
import marketone.db.handler.IndexHandler;

/**
 * @author LIU Xiaofan
 * 
 */
@Stateless
public class IndexHandlerBean implements IndexHandler {

	@PersistenceContext
	EntityManager em;

	public IndexHandlerBean() {
		super();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * marketone.db.handler.IndexHandler#getIndex(marketone.db.entity.Index)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Index getFullIndex(Index index) {
		index.setPriceList(em.createQuery("from Price p where p.index.id=" + index.getID())
				.getResultList());
		return index;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see marketone.db.handler.IndexHandler#getIndexs()
	 */
	@Override
	public Object[] getIndexList() {
		return em.createQuery("from Index index").getResultList().toArray();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * marketone.db.handler.IndexHandler#saveIndex(marketone.db.entity.Index)
	 */
	@Override
	public void saveIndex(Index index) {
		em.persist(index);
	}

	/* (non-Javadoc)
	 * @see marketone.db.handler.IndexHandler#getWorldIndex()
	 */
	@Override
	public Index getWorldIndex() {
		return (Index) em.createQuery("from Index index where index.name='THE WORLD INDEX'").getSingleResult();
	}
}
