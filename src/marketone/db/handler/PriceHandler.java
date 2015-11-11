/**
 * 
 */
package marketone.db.handler;

import java.util.Date;
import java.util.List;

import marketone.db.entity.Index;
import marketone.db.entity.Price;

/**
 * @author LIU Xiaofan
 * 
 */
public interface PriceHandler {

	/**
	 * @param index
	 * @param sd
	 * @param ed
	 * @return
	 */
	Object[] getClosePrice(Index index, Date sd, Date ed);

	/**
	 * @param s
	 * @param sd
	 * @param ed
	 * @return
	 */
	List<Price> getPeriodData(Index s, Date sd, Date ed);

	/**
	 * @param priceList
	 */
	void savePrices(List<Price> priceList);

	/**
	 * @param index
	 * @param start
	 * @param end
	 * @return
	 */
	int getClosePriceSize(Index index, Date start, Date end);

}
