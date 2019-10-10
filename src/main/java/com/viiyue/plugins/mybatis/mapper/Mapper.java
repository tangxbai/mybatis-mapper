package com.viiyue.plugins.mybatis.mapper;

import java.io.Serializable;

import com.viiyue.plugins.mybatis.mapper.special.AggregateFunctionMapper;
import com.viiyue.plugins.mybatis.mapper.special.ForUpdateMapper;
import com.viiyue.plugins.mybatis.mapper.special.LogicallyDeleteMapper;
import com.viiyue.plugins.mybatis.mapper.special.RecycleBinMapper;

/**
 * <p>
 * Basic mapper interface definition with special pessimistic locking operations
 * and logically deletion interfaces.
 * 
 * <p>
 * Added {@link ForUpdateMapper}, {@link LogicallyDeleteMapper}, and
 * {@link RecycleBinMapper} interfaces since 1.1.0
 * 
 * <ul>
 * <li><code>BASE</code> {@link BaseMapper} - Basic mapper interface
 * <li><code>U</code> {@link ForUpdateMapper} - Pessimistic lock interface
 * <li><code>U</code> {@link LogicallyDeleteMapper} - Logical delete interface
 * <li><code>U</code> {@link RecycleBinMapper} - Recycle Bin interface for recovering logically deleted data
 * </ul>
 * 
 * <p>
 * <b>Note</b>: Each api interface can be inherited independently
 * 
 * @author tangxbai
 * @since 1.1.0
 * 
 * @param <DO> database entity type
 * @param <DTO> query data return entity type
 * @param <PK> primary key type, must be a {@link Serializable} implementation class.
 */
public interface Mapper<DO, DTO, PK extends Serializable> 
extends Marker<DO, DTO, PK>,
		BaseMapper<DO, DTO, PK>,
		ForUpdateMapper<DO, DTO, PK>,
		LogicallyDeleteMapper<DO, DTO, PK>,
		RecycleBinMapper<DO, DTO, PK>,
		AggregateFunctionMapper<DO, DTO, PK> {
}
