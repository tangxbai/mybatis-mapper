package com.viiyue.plugins.mybatis;

import java.io.InputStream;
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
 * SqlSessionFactory factory = new MyBatisMapperFactoryBuilder().build(Resources.getResourceAsStream("mybatis.xml"));
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
 * System.out.println("the result is: " + result);</pre>
 * 
 * <hr>
 * <p>In the spring environment, you can configure this:
 * <pre>
 * # spring.xml #
 * &lt;beans&gt;
 *     &lt;!-- Sql session factory configuration --&gt;
 *     &lt;bean id=&quot;sqlSessionFactory&quot; class=&quot;org.mybatis.spring.SqlSessionFactoryBean&quot;&gt;
 *          &lt;property name=&quot;dataSource&quot; ref=&quot;your data source&quot;/&gt;
 *          &lt;!-- <b>Database beans are recommended to configure alias packages</b> --&gt;
 *          &lt;property name=&quot;typeAliasesPackage&quot; value=&quot;your.model.bean.aliases.package&quot;/&gt;
 *          &lt;!-- <b>Import mybatis configuration to modify the detailed configuration</b> --&gt;
 *          &lt;property name=&quot;configLocation&quot; value=&quot;classpath:/mybatis.xml&quot;/&gt;
 *          &lt;property name=&quot;mapperLocations&quot; value=&quot;classpath:/*Mapper.xml&quot;/&gt;
 *          &lt;!-- <b>Or modify the language driver directly</b> --&gt;
 *          &lt;property name=&quot;configuration.defaultScriptingLanguage&quot; value=&quot;com.viiyue.plugins.mybatis.MyBatisMapperLanguageDriver&quot;/&gt;
 *          &lt;!-- <b>This must be configured, otherwise the plugin will not take effect</b> --&gt;
 *          &lt;property name=&quot;sqlSessionFactoryBuilder&quot; value=&quot;com.viiyue.plugins.mybatis.MyBatisMapperFactoryBuilder&quot;/&gt;
 *          &lt;!-- <b>Mybatis-mapper uses configuration propertyis for preference configuration</b> --&gt;
 *          &lt;property name=&quot;configurationProperties&quot;&gt;
 *          &lt;props&gt;
 *              &lt;prop key=&quot;enableLogger&quot;&gt;true&lt;/prop&gt;
 *              &lt;prop key=&quot;enableRuntimeLog&quot;&gt;true&lt;/prop&gt;
 *              &lt;prop key=&quot;enableCompilationLog&quot;&gt;true&lt;/prop&gt;
 *              &lt;prop key=&quot;enableKeywordsToUppercase&quot;&gt;true&lt;/prop&gt;
 *              &lt;prop key=&quot;databaseColumnStyle&quot;&gt;#&lt;/prop&gt;
 *              &lt;/props&gt;
 *         &lt;/property&gt;
 *     &lt;/bean&gt;
 *     
 *     &lt;!-- Scan the mapper and inject the spring container --&gt;
 *     &lt;bean class=&quot;org.mybatis.spring.mapper.MapperScannerConfigurer&quot;&gt;
 *         &lt;property name=&quot;basePackage&quot; value=&quot;your.mapper.base.package&quot; /&gt;
 *         &lt;property name=&quot;annotationClass&quot; value=&quot;org.apache.ibatis.annotations.Mapper&quot; /&gt;
 *         &lt;property name=&quot;sqlSessionFactoryBeanName&quot; value=&quot;sqlSessionFactory&quot;/&gt;
 *     &lt;/bean&gt;
 * &lt;/beans&gt;
 * 
 * &#47;&#47; In spring, generally use automatic injection to get an interface instance
 * &#64;Service
 * public YourService {
 * 
 *     &#64;Autowired
 *     private YourMapper mapper;
 *     
 *     public Object getApiResult() {
 *         return mapper.getApi();
 *     }
 *     
 * }</pre>
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
