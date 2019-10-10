package com.viiyue.plugins.mybatis.enums;

import java.util.Locale;

import com.viiyue.plugins.mybatis.utils.CaseUtil;

/**
 * Conversion style between model bean property name and database field name
 *
 * <ul>
 * <li>{@link NameStyle#DEFAULT DEFAULT} - the original attribute name
 * <li>{@link NameStyle#UPPERCASE UPPERCASE} - convert attribute names to all uppercase characters
 * <li>{@link NameStyle#LOWERCASE LOWERCASE} - convert attribute names to all lowercase characters
 * <li>{@link NameStyle#UNDERLINE UNDERLINE} - attribute names are converted to underscores to separate words
 * <li>{@link NameStyle#UNDERLINE_UPPERCASE UNDERLINE_UPPERCASE} - convert attribute names to uppercase words separated by underscores
 * </ul>
 *
 * @author tangxbai
 * @since 1.0.0
 */
public enum NameStyle {

	/**
	 * The default style,
	 * If it is on the field, the original field name is used by default. 
	 * If it appears in the Table, the name style marked on the class is used by default.
	 */
	DEFAULT,

	/**
	 * Convert to all uppercase text
	 */
	UPPERCASE {
		@Override
		public String convert( String property ) {
			return property.toUpperCase( Locale.ENGLISH );
		}
	},

	/**
	 * Convert to all lowercase text
	 */
	LOWERCASE {
		@Override
		public String convert( String property ) {
			return property.toLowerCase( Locale.ENGLISH );
		}
	},

	/**
	 * Convert to underlined text
	 */
	UNDERLINE {
		@Override
		public String convert( String property ) {
			return CaseUtil.toUnderscore( property );
		}
	},

	/**
	 * Convert all uppercase text separated by underscores
	 */
	UNDERLINE_UPPERCASE {
		@Override
		public String convert( String property ) {
			return CaseUtil.toUnderscore( property ).toUpperCase();
		}
	};

	/**
	 * Value convert with different style
	 * 
	 * @param property target propery name
	 * @return converted property name
	 */
	public String convert( String property ) {
		return property;
	}

}
