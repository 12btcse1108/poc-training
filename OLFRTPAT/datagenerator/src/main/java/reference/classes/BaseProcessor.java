package reference.classes;

import java.io.Serializable;
import java.util.Map;

public abstract class BaseProcessor implements Serializable {

	private static final long serialVersionUID = -2749850662062109384L;

	abstract public void destroy();

	abstract protected void init(Map<String, String> conf);
	/*
	 * abstract public void execute(Object actor, Object data);
	 */
}
