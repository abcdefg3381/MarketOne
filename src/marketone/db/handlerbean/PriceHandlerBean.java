package marketone.db.handlerbean;

import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import marketone.db.entity.Index;
import marketone.db.entity.Price;
import marketone.db.handler.PriceHandler;

/**
 * @author LIU Xiaofan
 * 
 */
@Stateless
public class PriceHandlerBean implements PriceHandler {
	@PersistenceContext
	EntityManager em;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * marketone.db.handler.PriceHandler#getClosePrice(marketone.db.entity.Index
	 * , java.util.Date, java.util.Date)
	 */
	@Override
	public Object[] getClosePrice(Index index, Date sd, Date ed) {
		Query q =
				em
						.createQuery("from Price p where p.index.id=:id and p.date >= :sd and p.date <= :ed");
		q.setParameter("id", index.getID());
		q.setParameter("sd", sd);
		q.setParameter("ed", ed);
		Object[] objs = q.getResultList().toArray();
		for (int i = 0; i < objs.length; i++) {
			if (objs[i] instanceof Price) {
				Price p = (Price) objs[i];
				objs[i] = p.getAdjClose();
			}
		}
		return objs;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see marketone.db.handler.PriceHandler#getPeriodData(java.util.Date,
	 * java.util.Date)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Price> getPeriodData(Index s, Date sd, Date ed) {
		Query q =
				em
						.createQuery("from Price p where p.index.id=:id and p.date > :sd and p.date <= :ed");
		q.setParameter("id", s.getID());
		q.setParameter("sd", sd);
		q.setParameter("ed", ed);
		return q.getResultList();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see marketone.db.handler.PriceHandler#savePrices(java.util.List)
	 */
	@Override
	public void savePrices(List<Price> priceList) {
		for (Price price : priceList) {
			em.persist(price);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * marketone.db.handler.PriceHandler#getClosePriceSize(marketone.db.entity
	 * .Index, java.util.Date, java.util.Date)
	 */
	@Override
	public int getClosePriceSize(Index index, Date start, Date end) {
		Query q =
				em
						.createQuery("from Price p where p.index.id=:id and p.date >= :sd and p.date <= :ed");
		q.setParameter("id", index.getID());
		q.setParameter("sd", start);
		q.setParameter("ed", end);
		return q.getResultList().size();
	}

}
