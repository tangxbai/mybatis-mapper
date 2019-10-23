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

import java.io.InputStream;
import java.io.Reader;
import java.util.Properties;

import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import com.viiyue.plugins.mybatis.enums.Setting;
import com.viiyue.plugins.mybatis.scripting.MyBatisMapperBuilder;
import com.viiyue.plugins.mybatis.utils.LoggerUtil;

/**
 * <p>
 * Inherited from mybatis SqlSessionFactoryBuilder, use some of the functions to
 * achieve the default parsing, and refactor the myoptis parsed MappedStatement
 * to generate the default SQL source to achieve the purpose of the generic
 * mapper.
 * 
 * <hr>
 * <p>In a pure mybatis environment, you can configure it like this:
 * <pre>
 * # mybatis.xml #
 * &lt;configuration&gt;
 *     &lt;!-- <b>Mybatis-mapper preference configuration</b> --&gt;
 *     &lt;properties resource=&quot;jdbc.properties&quot;&gt;
 *         &lt;property name=&quot;enableLogger&quot; value=&quot;true&quot;/&gt;
 *         &lt;property name=&quot;enableRuntimeLog&quot; value=&quot;true&quot;/&gt;
 *         &lt;property name=&quot;enableCompilationLog&quot; value=&quot;false&quot;/&gt;
 *         &lt;property name=&quot;enableKeywordsToUppercase&quot; value=&quot;true&quot;/&gt;
 *         &lt;property name=&quot;databaseColumnStyle&quot; value=&quot;#&quot;/&gt;
 *     &lt;/properties&gt;
 *     
 *     &lt;!-- It is generally recommended to configure the bean alias --&gt;
 *     &lt;!-- so that it is easier to write and the plugin works better. --&gt;
 *     &lt;typeAliases&gt;
 *         &lt;package name=&quot;your.model.classpath&quot;/&gt;
 *         &lt;package name=&quot;or.more&quot;/&gt;
 *     &lt;/typeAliases&gt;
 * &lt;/configuration&gt;
 * 
 * &#47;&#47; You can initialize the configuration in the 
 * &#47;&#47; myabtis.xml configuration file and instantiate it.
 * SqlSessionFactory factory = new MyBatisMapperFactoryBuilder().build(Resources.getResourceAsStream("your-mybatis.xml"));
 * 
 * &#47;&#47; Or you can manually add configuration properties and initialize the factory
 * Properties properties = new Properties();
 * properties.put("enableLogger", true);
 * properties.put("enableRuntimeLog", true);
 * properties.put("enableCompilationLog", true);
 * properties.put("enableKeywordsToUppercase", true);
 * properties.put("databaseColumnStyle", "#");
 * SqlSessionFactory factory = new MyBatisMapperFactoryBuilder().build(Resources.getResourceAsStream("mybatis.xml"), properties);
 * 
 * &#47;&#47; How to use:
 * SqlSession session = factory.openSession();
 * YourMapper mapper = session.getMapper(YourMapper.class);
 * Object result = mapper.api();
 * 
 * &#47;&#47; Your other logic code 
 * ...</pre>
 *
 * @author tangxbai
 * @since 1.1.0
 * @since mybatis 3.2.4+
 * @see MyBatisMapperBuilder
 * @see SqlSessionFactoryBuilder
 */
public final class MyBatisMapperFactoryBuilder extends SqlSessionFactoryBuilder {

	private final MyBatisMapperBuilder builder = new MyBatisMapperBuilder();
	
	@Override
	public SqlSessionFactory build( Reader reader, String environment, Properties properties ) {
		LoggerUtil.printBootstrapLog();
		return super.build( reader, environment, properties );
	}

	@Override
	public SqlSessionFactory build( InputStream inputStream, String environment, Properties properties ) {
		LoggerUtil.printBootstrapLog();
		return super.build( inputStream, environment, properties );
	}

	@Override
	public SqlSessionFactory build( Configuration configuration ) {
		Setting.copyPropertiesFromConfiguration( configuration );
		SqlSessionFactory factory = super.build( builder.refactoring( configuration ) );
		LoggerUtil.printLoadedLog();
		return factory;
	}

}
