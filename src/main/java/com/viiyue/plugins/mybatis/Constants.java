/*-
 * ApacheLICENSE-2.0
 * #
 * Copyright (C) 2017 - 2019 mybatis-mapper
 * #
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ------------------------------------------------------------------------
 */
package com.viiyue.plugins.mybatis;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import com.viiyue.plugins.mybatis.annotation.member.Conditional;
import com.viiyue.plugins.mybatis.enums.ExpressionStyle;
import com.viiyue.plugins.mybatis.enums.NameStyle;
import com.viiyue.plugins.mybatis.enums.Template;
import com.viiyue.plugins.mybatis.enums.ValueStyle;

/**
 * Some constants that will be used in the mybatis-mapper plugin
 *
 * @author tangxbai
 * @since 1.1.0
 */
public final class Constants {
	
	// Do not let anyone instantiate this constant class
	private Constants() {}
	
	/** Template engine default root parameter name : {@value} */
	public static final String ROOT_PARAMETER_NAME = "$";
	/** Template engine default root scope : {@value} */
	public static final String DEFUALT_SCOPE = "this";
	/** SQL separator : {@value} */
	public static final String SEPARATOR = ", ";
	
	// Order by
	
	/** Order by : {@value} */
	public static final String ORDER_BY_ASCENDING = "asc";
	/** Order by : {@value} */
	public static final String ORDER_BY_ASCENDING_WRAP = "[asc]";
	/** Order by : {@value} */
	public static final String ORDER_BY_DESCENDING = "desc";
	/** Order by : {@value} */
	public static final String ORDER_BY_DESCENDING_WRAP = "[desc]";
	/** Order by : [ <sub>0</sub>"<b>asc</b>", <sub>1</sub>"<b>desc</b>", <sub>2</sub>"<b>[asc]</b>", <sub>3</sub>"<b>[desc]</b>" ] */
	public static final String [] ORDERS = { ORDER_BY_ASCENDING, ORDER_BY_DESCENDING, ORDER_BY_ASCENDING_WRAP, ORDER_BY_DESCENDING_WRAP };

	// Primary key
	 
	/** Primary key placeholder : {@value} */
	public static final String PRIMARY_KEY = "#pk";
	/** Primary keys placeholder : {@value} */
	public static final String PRIMARY_KEYS = "#pks";
	
	// Result map
	
	/** Default base result map reference name : {@value} */
	public static final String DEFAULT_RESULT_MAP = "BaseResultMap";
	
	// Conditional
	
	/** Default conditional expression : {@value} */
	public static final String DEFAULT_CONDITIONAL = "isNotBlank( this )";
	/** Default conditional expression operator holder : {@link Template#Equal Equal} */
	public static final Conditional.Holder DEFAULT_CONDITIONAL_HOLDER = Conditional.Holder.Equal;
	
	// Default styles
	
	/** Default name conversion style : {@link NameStyle#UNDERLINE} */
	public static final NameStyle DEFAULT_NAME_STYLE = NameStyle.UNDERLINE;
	/** Default value conversion style : {@link ValueStyle#SHORT} */
	public static final ValueStyle DEFAULT_VALUE_STYLE = ValueStyle.SHORT;
	/** Default expression conversion style : {@link ExpressionStyle#SHORT} */
	public static final ExpressionStyle DEFAULT_EXPRESSION_STYLE = ExpressionStyle.SHORT;
	
	// Regular expression
	
	/** 
	 * <p>Mybatis parameter regular expression matching pattern
	 * <p><code>#{property, javaType=, jdbcType=}</code>
	 */
	public static final Pattern MYBATIS_PARAMETER_PATTERN = Pattern.compile( "([#|$])\\{([^\\}].*?)\\}" );
	
	/** 
	 * <p>Text regular expression matching pattern 
	 * 
	 * <ul>
	 * <li>-- single line comment
	 * <li>&#47;&#47; single line comment
	 * <li>&#47;* inline text comment | multi-line text comment *&#47;
	 * </ul>
	 */
	public static final Pattern TEXT_COMMENTS_PATTERN = Pattern.compile( "(?ms)(('(?:''|[^'])*')|--.*?$|//.*?$|/\\*.*?\\*/)" );
	
	// Environment
	
	/** 
	 * Template engine default environment variable names
	 * 
	 * <table border="1">
	 * 	<tr>
	 * 		<th>Type name</th>
	 * 		<th>variable name</th>
	 * 		<th>Example</th>
	 * 	</tr>
	 * 	<tr>
	 * 		<th align="right" rowspan="4">Operating system</th>
	 * 	</tr>
	 * 	<tr><td>osName</td><td>(e.g., Windos 7)</td></tr>
	 * 	<tr><td>osVersion</td><td>(e.g., 6.1)</td></tr>
	 * 	<tr><td>osArch</td><td>(e.g., amd64)</td></tr>
	 * 	<tr>
	 * 		<th align="right" rowspan="4">Region</th>
	 * 	</tr>
	 * 	<tr><td>userTimezone</td><td>(e.g., Windos)</td></tr>
	 * 	<tr><td>userCountry</td><td>(e.g., CN)</td></tr>
	 * 	<tr><td>userLanguage</td><td>(e.g., zh)</td></tr>
	 * 	<tr>
	 * 		<th align="right" rowspan="3">Java informations</th>
	 * 	</tr>
	 * 	<tr><td>javaVersion</td><td>(e.g., 1.8.0_66)</td></tr>
	 * 	<tr><td>javaSpecificationVersion</td><td>(e.g., 1.8)</td></tr>
	 * 	<tr>
	 * 		<th align="right" rowspan="5">Fiel informations</th>
	 * 	</tr>
	 * 	<tr><td>fileEncoding</td><td>(e.g., UTF-8)</td></tr>
	 * 	<tr><td>fileSeparator</td><td>(e.g., \)</td></tr>
	 * 	<tr><td>lineSeparator</td><td>(e.g., \n)</td></tr>
	 * 	<tr><td>pathSeparator</td><td>(e.g., ;)</td></tr>
	 * </table>
	 */
	public static final List<String> ENV_PROP_NAMES = Arrays.asList( 
		"os.name", "os.version", "os.arch", // osName, osVersion, osArch
		"user.timezone", "user.country", "user.language", // userTimezone, userCountry, userLanguage
		"java.version", "java.specification.version", // javaVersion, javaSpecificationVersion
		"file.encoding", "file.separator", "line.separator", "path.separator" // fieldEncoding, fildSeparator, lineSeparator, pathSeparator
	);
	
}
