package net.pingfang.common.config;

import java.lang.reflect.ParameterizedType;

import org.apache.ibatis.binding.MapperProxyFactory;
import org.apache.ibatis.builder.xml.XMLMapperBuilder;
import org.apache.ibatis.datasource.unpooled.UnpooledDataSource;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.TransactionIsolationLevel;
import org.apache.ibatis.session.defaults.DefaultSqlSession;
import org.apache.ibatis.transaction.Transaction;
import org.apache.ibatis.transaction.jdbc.JdbcTransaction;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePropertySource;

/**
 * @author 王超
 * @description TODO
 * @date 2022-06-21 16:09
 */
public class BaseMapperTest<T> {
	/**
	 * mapper接口类（持久层接口）
	 */
	private T mapper;
	/**
	 * 数据库连接
	 */
	private SqlSession sqlSession;
	/**
	 * 执行
	 */
	private static Executor executor;
	/**
	 * 配置
	 */
	private static Configuration configuration;

	static {
		try {
			// 定义一个配置
			configuration = new Configuration();
			configuration.setCacheEnabled(false);
			configuration.setLazyLoadingEnabled(false);
			configuration.setAggressiveLazyLoading(true);
			configuration.setDefaultStatementTimeout(20);
			// 读取测试环境数据库配置
			PropertySource propertySource = new ResourcePropertySource(new ClassPathResource("testdb.properties"));
			// 设置数据库链接
			UnpooledDataSource dataSource = new UnpooledDataSource();
			dataSource.setDriver(propertySource.getProperty("driverClassName").toString());
			dataSource.setUrl(propertySource.getProperty("url").toString());
			dataSource.setUsername(propertySource.getProperty("username").toString());
			dataSource.setPassword(propertySource.getProperty("password").toString());
			// 设置事务(测试设置事务不提交false)
			Transaction transaction = new JdbcTransaction(dataSource, TransactionIsolationLevel.READ_UNCOMMITTED,
					false);
			// 设置执行
			executor = configuration.newExecutor(transaction);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public BaseMapperTest(String mapperName) {
		try {
			// 解析mapper文件
			Resource mapperResource = new ClassPathResource(mapperName);
			XMLMapperBuilder xmlMapperBuilder = new XMLMapperBuilder(mapperResource.getInputStream(), configuration,
					mapperResource.toString(), configuration.getSqlFragments());
			xmlMapperBuilder.parse();
			// 直接实例化一个默认的sqlSession
			// 是做单元测试，那么没必要通过SqlSessionFactoryBuilder构造SqlSessionFactory,再来获取SqlSession
			sqlSession = new DefaultSqlSession(configuration, executor, false);
			// 将接口实例化成对象
			ParameterizedType pt = (ParameterizedType) this.getClass().getGenericSuperclass();
			MapperProxyFactory<T> mapperProxyFactory = new MapperProxyFactory<>(
					(Class<T>) pt.getActualTypeArguments()[0]);
			mapper = mapperProxyFactory.newInstance(sqlSession);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 返回mapper实例对象
	 */
	public T getMapper() {
		return mapper;
	}
}
