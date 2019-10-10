package com.viiyue.plugins.mybatis.metadata.info;

import java.util.Objects;

import com.viiyue.plugins.mybatis.annotation.member.Version;
import com.viiyue.plugins.mybatis.api.NextVersionProvider;
import com.viiyue.plugins.mybatis.metadata.Column;
import com.viiyue.plugins.mybatis.metadata.Property;
import com.viiyue.plugins.mybatis.utils.StringUtil;

/**
 * Optimistic lock description bean
 *
 * @author tangxbai
 * @since 1.1.0
 */
public final class VersionInfo {

	private final Property property;
	private final Class<? extends NextVersionProvider> versionProviderType;

	public VersionInfo( Property property, Version version ) {
		this.property = property;
		this.versionProviderType = version.nextVersion();
	}

	public Property getProperty() {
		return property;
	}
	
	public Column getColumn() {
		return property.getColumn();
	}
	
	public String getPropertyName() {
		return property.getName();
	}
	
	public String getParameterName() {
		return "next" + StringUtil.capitalize( getPropertyName() ) + "Value"; // nextVersionValue
	}

	public Class<? extends NextVersionProvider> getVersionProviderType() {
		return versionProviderType;
	}
	
	public boolean isVersionProperty( Property property ) {
		return Objects.equals( this.property.getName(), property.getName() );
	}

}
