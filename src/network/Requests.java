/**
 * 
 */
package network;

/** Interface of the request.
 * @author zirenxiao
 *
 */
public interface Requests {
	
	/** Get request type
	 * @return
	 */
	RequestType getType();
	
	
	/** Set request type
	 * @param t
	 */
	void setType(RequestType t);
	
	
	/** Convert a request to a JSON string
	 * @return
	 */
	String toJSONString();
	
	
	/** Convert a JSON string to a request
	 * @param s
	 */
	void fromJSONString(String s);

}
