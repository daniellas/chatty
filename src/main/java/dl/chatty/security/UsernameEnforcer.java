package dl.chatty.security;

import java.util.function.Function;

/**
 * Enforce username to support access to user owned object before object query
 * is performed
 * 
 * @author Daniel Łaś
 *
 */
public interface UsernameEnforcer extends Function<String, String> {

}
