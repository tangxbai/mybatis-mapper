package com.viiyue.plugins.mybatis.utils;

/**
 * Some auxiliary method extensions for {@code StringBuffer}
 *
 * @author tangxbai
 * @since 1.1.0
 */
public class StringAppender {

	private final StringBuffer buffer;

	public StringAppender() {
		this( new StringBuffer() );
	}
	
	public StringAppender( int capacity ) {
		this( new StringBuffer( capacity ) );
	}

	public StringAppender( StringBuffer buffer ) {
		this.buffer = buffer;
	}
	
	public boolean isEmpty() {
		return buffer.length() == 0;
	}

	public boolean hasContent() {
		return buffer.length() > 0;
	}

	public StringAppender addDelimiter( String delimiter ) {
		if ( delimiter != null && hasContent() ) {
			this.buffer.append( delimiter );
		}
		return this;
	}
	
	public final <T> StringAppender insert( int offset, T content ) {
		if ( content != null ) {
			this.buffer.insert( offset, content );
		}
		return this;
	}

	public <T> StringAppender prepend( T content ) {
		if ( content != null ) {
			this.buffer.insert( 0, content );
		}
		return this;
	}
	
	public boolean startsWith( String content ) {
		if ( content != null ) {
			return buffer.indexOf( content ) == 0;
		}
		return false;
	}
	
	public boolean endsWith( String content ) {
		if ( content != null ) {
			return buffer.indexOf( content ) == buffer.length() - 1;
		}
		return false;
	}

	public <T> StringAppender append( StringAppender appender ) {
		if ( appender != null && appender.hasContent() ) {
			this.buffer.append( appender.getBuffer() );
		}
		return this;
	}
	
	public StringAppender append( String content ) {
		if ( content != null ) {
			this.buffer.append( content );
		}
		return this;
	}

	public StringAppender append( Object content ) {
		if ( content != null ) {
			this.buffer.append( content );
		}
		return this;
	}

	public StringAppender delete( int start, int end ) {
		this.buffer.delete( start, end );
		return this;
	}

	public StringAppender reset() {
		this.buffer.setLength( 0 );
		return this;
	}

	public StringBuffer getBuffer() {
		return buffer;
	}

	@Override
	public String toString() {
		return buffer.toString();
	}

}
