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
package com.viiyue.plugins.mybatis.utils;

import java.util.Random;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * <p> Twitter Snowflake Id
 * 
 * <p>
 * SnowFlake的结构如下(每部分用-分开):<br>
 * 0 - 0000000000 0000000000 0000000000 0000000000 0 - 00000 - 00000 -
 * 000000000000 <br>
 * 1位标识，由于long基本类型在Java中是带符号的，最高位是符号位，正数是0，负数是1，所以id一般是正数，最高位是0<br>
 * 41位时间截(毫秒级)，注意，41位时间截不是存储当前时间的时间截，而是存储时间截的差值（当前时间截 - 开始时间截)
 * 得到的值），这里的的开始时间截，一般是我们的id生成器开始使用的时间，由我们程序来指定的（如下下面程序类的startTimestamp属性）。
 * 41位的时间截，可以使用69年，年T = (1L << 41) / (1000L * 60 * 60 * 24 * 365) = 69<br>
 * 10位的数据机器位，可以部署在1024个节点，包括5位dataCenterId和5位workerId<br>
 * 12位序列，毫秒内的计数，12位的计数顺序号支持每个节点每毫秒(同一机器，同一时间截)产生4096个ID序号<br>
 * 加起来刚好64位，为一个Long型。<br>
 * SnowFlake的优点是，整体上按照时间自增排序，并且整个分布式系统内不会产生ID碰撞(由数据中心ID和机器ID作区分)，并且效率较高，经测试，SnowFlake每秒能够产生26万ID左右。
 * </p>
 * 
 * @author Twitter
 * @author tangxbai
 * @since 1.1.0
 */
public class IdGenerator {

	private static IdGenerator generator = null;
	private static final String workerIdErrorMessage = "worker Id can't be greater than %d or less than 0";
	private static final String dataCenterIdErrorMessage = "datacenter Id can't be greater than %d or less than 0";
	private static final String clockBackwardErrorMessage = "Clock moved backwards. Refusing to generate id for %d milliseconds";

	/** Worker Id [ 0-31 ] */
	private long workerId;
	/** Data center Id [ 0-31 ] */
	private long dataCenterId;
	
	/** Sync object for non-fair locks */
	private final Lock lock = new ReentrantLock();
	/** Start timestamp ( 2019-01-01 ) */
	private final long startTimestamp = 1548950400000L;

	/** The number of bits in the machine Id */
	private final int workerIdBits = 5;
	/** The number of digits in the data identifier Id */
	private final int dataCenterIdBits = 5;
	/** The number of bits in the sequence in the Id */
	private final int sequenceBits = 12;

	/** The maximum machine Id supported, the result is 31 */
	private final int maxWorkerId = ~ ( -1 << workerIdBits );
	/** The maximum supported data identifier Id, the result is 31 */
	private final int maxDataCenterId = ~ ( -1 << dataCenterIdBits );
	/** Generate a mask for the sequence, here 4095 ( 0b111111111111 = 0xfff = 4095 ) */
	private final int maxSequenceMask = ~ ( -1 << sequenceBits );
	
	/** Machine Id shifts 12 bits to the left */
	private final long workerIdShift = sequenceBits;
	/** Data ID Id is shifted to the left by 17 digits ( 12 + 5 ) */
	private final int dataCenterIdShift = sequenceBits + workerIdBits;
	/** Time is shifted to the left by 22 bits ( 5 + 5 + 12 ) */
	private final int timestampShift = sequenceBits + workerIdBits + dataCenterIdBits;
	
	/** Sequence within milliseconds [ 0-4095 ] */
	private volatile long sequence = 0L;
	/** The time when the last Id was generated */
	private volatile long lastTimestamp = 0L;
	
	/**
	 * Constructor initializes machine Id and data center Id
	 * 
	 * @param workerId worker id [ 0-31 ]
	 * @param dataCenterId data center id [ 0-31 ]
	 */
	private IdGenerator( int workerId, int dataCenterId ) {
		if ( workerId > maxWorkerId || workerId < 0 ) {
			throw new IllegalArgumentException( String.format( workerIdErrorMessage, maxWorkerId ) );
		}
		if ( dataCenterId > maxDataCenterId || dataCenterId < 0 ) {
			throw new IllegalArgumentException( String.format( dataCenterIdErrorMessage, maxDataCenterId ) );
		}
		this.workerId = workerId;
		this.dataCenterId = dataCenterId;
	}

	/**
	 * Block until the next millisecond until a new timestamp is obtained
	 * 
	 * @param lastTimestamp last generated timestamp
	 * @return fixed timestamp
	 */
	private long blockNextMillis( long lastTimestamp ) {
		long current = timeGen();
		while ( current <= lastTimestamp ) {
			current = timeGen();
		}
		return current;
	}
	
	/**
	 * Get the current system timestamp
	 * 
	 * @return current system time millis
	 */
	private long timeGen() {
		return System.currentTimeMillis();
	}
	
	/**
	 * Get the next serialization Id
	 * 
	 * @return next generated sequence id
	 */
	private long createId() {
		try {
			lock.lock();
			long currentTimestamp = timeGen();
			if ( currentTimestamp < lastTimestamp ) {
				throw new RuntimeException( String.format( clockBackwardErrorMessage, lastTimestamp - currentTimestamp ) );
			}
			if ( lastTimestamp == currentTimestamp ) {
				sequence = ( sequence + 1 ) & maxSequenceMask;
				if ( sequence == 0 ) {
					currentTimestamp = blockNextMillis( lastTimestamp );
				}
			} else {
				sequence = 0;
			}
			lastTimestamp = currentTimestamp;
			return ( ( currentTimestamp - startTimestamp ) << timestampShift ) | 
				( dataCenterId << dataCenterIdShift ) | 
				( workerId << workerIdShift ) | 
				sequence;
		} finally {
			lock.unlock();
		}
	}
	
	/**
	 * Get the next long {@code Id}
	 * 
	 * @return unique {@code Id} of the long type
	 */
	public static final long nextId() {
		return init().createId();
	}
	
	/**
	 * Get the next string {@code Id}
	 * 
	 * @return unique {@code Id} of the string type
	 */
	public static final String nextIdString() {
		return String.valueOf( init().createId() );
	}
	
	/**
	 * Initialize the default generator, 
	 * data center id and worker id randomly generated.
	 * 
	 * @return default Id generator
	 */
	public static final IdGenerator init() {
		if ( generator == null ) {
			Random random = new Random();
			return init( random.nextInt( 31 ), random.nextInt( 31 ) );
		}
		return generator;
	}
	
	/**
	 * Initialize the custom generator, 
	 * you can specify the worker id and data center id.
	 * 
	 * @param workerId worker id, the range is 0-31
	 * @param dataCenterId data center id, the range is 0-31
	 * @return custom generator
	 */
	public static final IdGenerator init( int workerId, int dataCenterId ) {
		if ( generator == null ) {
			synchronized ( IdGenerator.class ) {
				if ( generator == null ) {
					generator = new IdGenerator( workerId, dataCenterId );
				}
			}
		}
		return generator;
	}
	
}
