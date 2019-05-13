/**
 * 
 */
package network;

import java.io.Serializable;

/**
 * @author zirenxiao
 *
 */
public interface Requests extends Serializable {
	
	RequestType getType();
	void setType(RequestType t);
	

}
