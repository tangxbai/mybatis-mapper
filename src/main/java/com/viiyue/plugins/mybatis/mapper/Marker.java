package com.viiyue.plugins.mybatis.mapper;

import java.io.Serializable;

/**
 * The plugin identifies the interface, only the class that integrates the
 * interface will be processed by the plugin.
 * 
 * @author tangxbai
 * @since 1.1.0
 * 
 * @param <DO> database entity type
 * @param <DTO> query data return entity type
 * @param <PK> primary key type, must be a {@link Serializable} implementation class.
 */
public interface Marker<DO, DTO, PK extends Serializable> {}
