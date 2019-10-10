package com.viiyue.plugins.mybatis.provider.special;

import org.apache.ibatis.mapping.MappedStatement;

import com.viiyue.plugins.mybatis.condition.WhereExample;
import com.viiyue.plugins.mybatis.mapper.special.RecycleBinMapper;
import com.viiyue.plugins.mybatis.provider.DynamicProvider;
import com.viiyue.plugins.mybatis.utils.StringUtil;

/**
 * {@link RecycleBinMapper} interface sql provider implementation class
 *
 * @author tangxbai
 * @since 1.1.0
 */
public class RecycleBinProvider extends DynamicProvider {
	
	/**
	 * Refer to the {@link RecycleBinMapper#selectAllDeleted()
	 * selectAllDeleted()} method of {@link RecycleBinMapper}
	 * 
	 * @param ms the MappedStatement object
	 * @return the sql template text
	 */
	public String selectAllDeleted( MappedStatement ms ) {
		return StringUtil.join( 
			"[select] @{this.columns} ", 
			"[from] @{this.table} ", 
			"@{this.tryLogicallyDelete.useWhereQuery.useDeletedValue} ",
			"@{this.defaultOrderBy}"
		);
	}
	
	/**
	 * Refer to the {@link RecycleBinMapper#restore(Object) restore(Object)}
	 * method of {@link RecycleBinMapper}
	 * 
	 * @param ms the MappedStatement object
	 * @return the sql template text
	 */
	public String restore( MappedStatement ms ) {
		return baseStatement( "%{this.where($).tryLogicallyDeleteQuery.useDeletedValue}" );
	}
	
	/**
	 * Refer to the {@link RecycleBinMapper#restoreAllDeleted() restoreAllDeleted()}
	 * method of {@link RecycleBinMapper}
	 * 
	 * @param ms the MappedStatement object
	 * @return the sql template text
	 */
	public String restoreAllDeleted( MappedStatement ms ) {
		return baseStatement( "@{this.tryLogicallyDelete.useWhereQuery.useDeletedValue}" );
	}
	
	/**
	 * Refer to the
	 * {@link RecycleBinMapper#restoreByPrimaryKey(java.io.Serializable)
	 * restoreByPrimaryKey(PK)} method of {@link RecycleBinMapper}
	 * 
	 * @param ms the MappedStatement object
	 * @return the sql template text
	 */
	public String restoreByPrimaryKey( MappedStatement ms ) {
		return baseStatement( "[where] @{this.pk} = #{pk} @{this.tryLogicallyDelete.useAndQuery.useDeletedValue}" );
	}
	
	/**
	 * Refer to the
	 * {@link RecycleBinMapper#restoreByPrimaryKeyIndex(Integer, java.io.Serializable)
	 * restoreByPrimaryKeyIndex(Integer, PK)} method of {@link RecycleBinMapper}
	 * 
	 * @param ms the MappedStatement object
	 * @return the sql template text
	 */
	public String restoreByPrimaryKeyIndex( MappedStatement ms ) {
		return baseStatement( "[where] %{this.pk($.index)} = #{pk} @{this.tryLogicallyDelete.useAndQuery.useDeletedValue}" );
	}
	
	/**
	 * Refer to the
	 * {@link RecycleBinMapper#restoreByPrimaryKeyGroup(java.io.Serializable...)
	 * restoreByPrimaryKeyIndex(PK...)} method of {@link RecycleBinMapper}
	 * 
	 * @param ms the MappedStatement object
	 * @return the sql template text
	 */
	public String restoreByPrimaryKeyGroup( MappedStatement ms ) {
		return baseStatement( "[where] @{this.pk} [in] ( {{$.inArguments}} ) @{this.tryLogicallyDelete.useAndQuery.useDeletedValue}" );
	}
	
	/**
	 * Refer to the
	 * {@link RecycleBinMapper#restoreByPrimaryKeyIndexGroup(Integer, java.io.Serializable...)
	 * restoreByPrimaryKeyIndexGroup(Integer, PK...)} method of
	 * {@link RecycleBinMapper}
	 * 
	 * @param ms the MappedStatement object
	 * @return the sql template text
	 */
	public String restoreByPrimaryKeyIndexGroup( MappedStatement ms ) {
		return baseStatement( "[where] %{this.pk($.index)} [in] ( {{$.inArguments}} ) @{this.tryLogicallyDelete.useAndQuery.useDeletedValue}" );
	}
	
	/**
	 * Refer to the
	 * {@link RecycleBinMapper#restoreByExample(com.viiyue.plugins.mybatis.condition.WhereExample)
	 * restoreByExample(WhereExample)} method of {@link RecycleBinMapper}
	 * 
	 * @param ms the MappedStatement object
	 * @return the sql template text
	 * @see WhereExample
	 */
	public String restoreByExample( MappedStatement ms ) {
		return baseStatement( "{{$.example.where(true)}}" );
	}
	
	/**
	 * Basic statement prefix
	 * 
	 * @param fragments SQL statement fragments
	 * @return the complete sql statement 
	 */
	private String baseStatement( String ... fragments ) {
		return "[update] @{this.table} [set] @{this.tryLogicallyDelete.useQueryValue} " + (
			fragments.length == 1 ? fragments[ 0 ] : StringUtil.join( fragments ) 
		);
	}
	
}
