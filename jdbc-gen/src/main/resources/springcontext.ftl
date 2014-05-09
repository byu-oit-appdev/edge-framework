[#ftl]
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
		xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
		xmlns:aop="http://www.springframework.org/schema/aop"
		xmlns:context="http://www.springframework.org/schema/context"
		xmlns:tx="http://www.springframework.org/schema/tx"
		xmlns:c="http://www.springframework.org/schema/c"
		xmlns:p="http://www.springframework.org/schema/p"
		xsi:schemaLocation="
		http://www.springframework.org/schema/beans    http://www.springframework.org/schema/beans/spring-beans-3.1.xsd
		http://www.springframework.org/schema/aop      http://www.springframework.org/schema/aop/spring-aop-3.1.xsd
		http://www.springframework.org/schema/context  http://www.springframework.org/schema/context/spring-context-3.1.xsd
		http://www.springframework.org/schema/tx       http://www.springframework.org/schema/tx/spring-tx-3.1.xsd">

[#list daos as d]
	<bean id="${d[0]}Dao" class="[#if package?? && package != ""]${package}.[/#if]da.jdbc.${d[1]}DaoImpl"
			c:jdbc-ref="${jdbcName}"
			c:mapper-ref="${d[0]}Mapper" />

[/#list]

[#list daos as d]
	<bean id="${d[0]}Mapper" class="[#if package?? && package != ""]${package}.[/#if]da.mapper.${d[1]}Mapper" />

[/#list]
	<tx:advice id="${txAdvice}" transaction-manager="${txManager}">
		<tx:attributes>
			<tx:method name="delete*" read-only="false" isolation="DEFAULT" propagation="REQUIRED" />
			<tx:method name="save*"   read-only="false" isolation="DEFAULT" propagation="REQUIRED" />
			<tx:method name="update*" read-only="false" isolation="DEFAULT" propagation="REQUIRED" />
			<tx:method name="find*"   read-only="true"  isolation="DEFAULT" propagation="REQUIRES_NEW" />
			<tx:method name="get*"    read-only="true"  isolation="DEFAULT" propagation="REQUIRES_NEW" />
		</tx:attributes>
	</tx:advice>

	<aop:config>
		<aop:advisor advice-ref="${txAdvice}" pointcut="execution(* [#if package?? && package != ""]${package}.[/#if]da.*.*(..))" />
	</aop:config>

	<bean id="${datasource}" class="com.jolbox.bonecp.BoneCPDataSource"
		  p:driverClass="" p:jdbcUrl="" p:externalAuth="true"
		  p:idleConnectionTestPeriodInSeconds="0" p:idleMaxAgeInSeconds="30"
		  p:closeConnectionWatch="false"
		  p:connectionTestStatement="select 1 from dual"
		  p:connectionTimeoutInMs="7500"
		  p:poolName="${poolName}"
		  p:serviceOrder="LIFO"
		  p:partitionCount="4"
		  p:minConnectionsPerPartition="1"
		  p:maxConnectionsPerPartition="16"
		  p:acquireIncrement="2" p:acquireRetryDelayInMs="2500" />

	<bean id="${jdbcName}" class="org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate"
		  c:dataSource-ref="${datasource}" />

	<bean id="${txManager}" class="org.springframework.jdbc.datasource.DataSourceTransactionManager"
		  p:dataSource-ref="${datasource}" />
</beans>
