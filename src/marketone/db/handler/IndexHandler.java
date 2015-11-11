package marketone.db.handler;

import marketone.db.entity.Index;

/**
 * @author LIU Xiaofan
 * 
 */
public interface IndexHandler {

	/**
	 * @param index
	 * @return
	 */
	Index getFullIndex(Index index);

	/**
	 * @return
	 */
	Object[] getIndexList();

	/**
	 * @param index
	 */
	void saveIndex(Index index);

	/**
	 * @return
	 */
	Index getWorldIndex();

}
