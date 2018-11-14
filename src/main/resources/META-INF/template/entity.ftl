package ${packageName};

import com.thinkerwolf.hantis.orm.annotation.*;
<#list importList as item>
	import ${item};
</#list>

/**
 * Auto genarated by Hantis 
 */
@Entity(name = "${tableName}")
public class ${className} {
	<#list columnInfos as item>
	/** ${item.comment} */
		<#if item.primaryKey>
	@Id
		</#if>
		<#if item.autoIncrement> 
	@GeneratedValue
		</#if>
	@Column(name = "${item.columnName}")
	private ${item.field} ${item.fieldName};
	
	</#list>
	
	public ${className}() {}
	
	<#list columnInfos as item>
		<#assign prefix = item.fieldName?cap_first>
	public void set${prefix}(${item.field} ${item.fieldName}) {
		this.${item.fieldName} = ${item.fieldName};
	}
		
	public ${item.field} get${prefix}() {
		return this.${item.fieldName};
	}
	
	</#list>
	
	// Self code start
	
	// Self code end
}