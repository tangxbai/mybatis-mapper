package com.viiyue.plugins.mybatis.mapper.base;

import java.io.Serializable;

import org.apache.ibatis.annotations.InsertProvider;

import com.viiyue.plugins.mybatis.annotation.member.Column;
import com.viiyue.plugins.mybatis.mapper.Marker;
import com.viiyue.plugins.mybatis.provider.DynamicProvider;
import com.viiyue.plugins.mybatis.provider.base.BaseInsertProvider;

/**
 * Basic insert api method interface definition
 * 
 * @author tangxbai
 * @since 1.0.0
 * 
 * @param <DO> database entity type
 * @param <DTO> query data return entity type
 * @param <PK> primary key type, must be a {@link Serializable} implementation class.
 */
public interface BaseInsertMapper<DO, DTO, PK extends Serializable> extends Marker<DO, DTO, PK> {

	/**
	 * Insert new data, including all fields by default, unless you set the
	 * {@link Column @Column} inserttable to false to specify which fields can
	 * not be inserted.
	 * 
	 * @param param database entity object
	 * @return generated primary key value
	 */
	@InsertProvider( type = BaseInsertProvider.class, method = DynamicProvider.dynamicSQL )
	int insert( DO param );

	/**
	 * Insert new data, this method will filter the non-null attribute values in
	 * the object as insert columns, and will not use all columns unless each
	 * field value in your object is not null.
	 * 
	 * @param param database entity object
	 * @return generated primary key value
	 */
	@InsertProvider( type = BaseInsertProvider.class, method = DynamicProvider.dynamicSQL )
	int insertBySelective( DO param );

}
