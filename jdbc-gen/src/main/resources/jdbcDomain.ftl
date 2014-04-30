[#ftl]
[#if package?? && package != ""]package ${package}.[/#if]domain;

[#list imports as imp]
import ${imp};
[/#list]
import java.io.Serializable;

/**
  *
  */
public class ${className} implements Serializable {

	private static final long serialVersionUID = 1L;

	[#list props as p]
	protected ${p[0]} ${p[1]};
	[/#list]

	/** Default constructor */
	public ${className}() {
	}

	[#list props as p]
	/**
	  * Get the current value of ${p[1]}.
	  * @return the current value
	  */
	public ${p[0]} get${p[2]}() {
		return this.${p[1]};
	}

	/**
	  * Set a new value for ${p[1]}.
	  * @param ${p[1]} the new value
	  */
	public void set${p[2]}(final ${p[0]} ${p[1]}) {
		this.${p[1]} = ${p[1]};
	}

	[/#list]
}
