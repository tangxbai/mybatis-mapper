/**
 * Copyright (C) 2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.viiyue.plugins.mybatis.utils;

import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

import org.apache.commons.lang3.time.DurationFormatUtils;
import org.apache.commons.lang3.time.StopWatch;
import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.LogFactory;

import com.viiyue.plugins.mybatis.Constants;
import com.viiyue.plugins.mybatis.enums.Setting;
import com.viiyue.plugins.mybatis.mapper.Mapper;
import com.viiyue.plugins.mybatis.template.TemplateEngine;

/**
 * Log printing tool class
 *
 * @author tangxbai
 * @since 1.1.0
 */
public final class LoggerUtil {
	
	private LoggerUtil() {}

	public static final Log log = LogFactory.getLog( Mapper.class );
	public static final String dividingLine = "--------------------------------------------------------------------------------";
	
	private static Boolean isEnableLogger;
	private static boolean isEnableRuntimeLog;
	private static boolean isEnableCompilationLog;
	
	private static StopWatch monitor;
	private static Set<Class<?>> registedMappers = new LinkedHashSet<Class<?>>();
	private static String compilationHint = "( Hint: The first compilation may be a bit slow )";
	
	/**
	 * Whether to enable print runtime SQL
	 * 
	 * @return {@code true} will print runtim SQL, {@code false} will not.
	 */
	public static boolean isEnableRuntimeLog() {
		return isEnableLogger() && isEnableRuntimeLog;
	}
	
	/**
	 * Whether to enable printing compiled SQL logs
	 * 
	 * @return {@code true} will print the SQL template compilation log,
	 *         {@code false} will not.
	 */
	public static boolean isEnableCompilationLog() {
		return isEnableLogger() && isEnableCompilationLog;
	}
	
	/**
	 * Whether to enable log printing
	 * 
	 * @return {@code true} will print the relevant log, {@code false} will not.
	 */
	public static boolean isEnableLogger() {
		if ( isEnableLogger == null ) {
			if ( log.isDebugEnabled() ) {
				isEnableLogger = Setting.Logger.isEnable();
				isEnableRuntimeLog = Setting.RuntimeLog.isEnable();
				isEnableCompilationLog = Setting.CompileLog.isEnable();
			} else {
				isEnableLogger = false;
			}
		}
		return isEnableLogger;
	}
	
	/**
	 * Get monitoring time
	 * 
	 * @param monitor the monitor object
	 * @return the time consumed by the program
	 */
	public static String getWatchTime( StopWatch monitor ) {
		monitor.stop();
		return monitor.getTime() + "ms";
	}
	
	/**
	 * Get monitoring time
	 * 
	 * @param monitor the monitor object
	 * @param format time formatting style
	 * @return the time consumed by the program
	 */
	public static String getWatchTime( StopWatch monitor, String format ) {
		monitor.stop();
		return DurationFormatUtils.formatDuration( monitor.getTime(), format );
	}
	
	/**
	 * Record the scanned mapper interface
	 * 
	 * @param mapperInterface the scanned mapper interface 
	 */
	public static void addMapper( Class<?> mapperInterface ) {
		if ( mapperInterface != null ) {
			registedMappers.add( mapperInterface );
		}
	}
	
	public static void printBootstrapLog() {
		if ( log.isDebugEnabled() && monitor == null ) {
			monitor = StopWatch.createStarted();
			log.debug( getDividingLineSeparator( "<START>" ) );
			log.debug( dividingLine );
			log.debug( "Initialize the mybatis mapper metadata information ..." );
			log.debug( dividingLine );
			log.debug( "System platform: " + TemplateEngine.eval( "env.osName + '( ' + env.osVersion + ' )'" ) );
			log.debug( "-- Java version: " + TemplateEngine.eval( "env.javaSpecificationVersion + '( ' + env.javaVersion + ' )'" ) );
			log.debug( "- File encoding: " + TemplateEngine.eval( "env.fileEncoding" ) );
			log.debug( "------ Language: " + TemplateEngine.eval( "env.userLanguage + '-' + env.userCountry" ) );
			log.debug( dividingLine );
			log.debug( Constants.EMPTY );
		}
	}
	
	public static void printLoadedLog() {
		if ( log.isDebugEnabled() && monitor != null ) {
			log.debug( dividingLine );
			log.debug( "Mybatis mapper initialization completed " + getWatchTime( monitor, "s.SSS" ) + "s, scanned " + registedMappers.size() + " mapper interfaces " );
			log.debug( dividingLine );
			for ( Class<?> type : registedMappers ) {
				log.debug( "# " + type.getName() );
			}
			log.debug( dividingLine );
			log.debug( getDividingLineSeparator( "<END>" ) );
			log.debug( Constants.EMPTY );
			monitor = null;
			registedMappers.clear();
		}
	}
	
	public static void printCompilationLog( String namespace, String type, String original, String compiled, String target, StopWatch monitor ) {
		if ( isEnableCompilationLog()  ) {
			log.debug( "Compile " + Objects.toString( type, "the" ) + " SQL ..." );
			log.debug( dividingLine );
			log.debug( "----- Target: " + target );
			log.debug( "-- Namespace: " + namespace );
			log.debug( "Template SQL: " + original );
			log.debug( "Compiled SQL: " + compiled );
			log.debug( "------- Time: " + getWatchTime( monitor ) + StringUtil.defaultString( compilationHint, Constants.EMPTY ) );
			log.debug( dividingLine );
			log.debug( Constants.EMPTY );
			compilationHint = null;
		}
	}
	
	public static void printRuntimeLog( String namespace, String original, String compiled, Object parameter, StopWatch watch ) {
		if ( isEnableRuntimeLog() ) {
			final Log log = LogFactory.getLog( namespace );
			log.debug( dividingLine );
			log.debug( "==> Compile runtime SQL ..." );
			log.debug( dividingLine );
			log.debug( "==> - Template: " + original );
			log.debug( "==> - Compiled: " + compiled );
			log.debug( "==> Parameters: " + ObjectUtil.defaultIfNull( parameter, "null" ) );
			log.debug( "<== ----- Time: " + getWatchTime( watch ) );
			log.debug( dividingLine );
		}
	}

	private static String getDividingLineSeparator( String text ) {
		text = " " + text + " ";
		int textLength = text.length();
		int totalLength = dividingLine.length();
		int startingIndex = ( totalLength - textLength ) / 2;
		return dividingLine.substring( 0, startingIndex ) + text + dividingLine.substring( startingIndex + textLength );
	}
	
}
