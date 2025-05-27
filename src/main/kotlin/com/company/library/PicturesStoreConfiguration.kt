package com.company.library

import io.jmix.autoconfigure.data.JmixLiquibaseCreator
import liquibase.integration.spring.SpringLiquibase
import org.springframework.boot.autoconfigure.liquibase.LiquibaseProperties
import io.jmix.core.JmixModules
import io.jmix.core.Resources
import io.jmix.data.impl.JmixEntityManagerFactoryBean
import io.jmix.data.impl.JmixTransactionManager
import io.jmix.data.persistence.DbmsSpecifics
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.orm.jpa.JpaTransactionManager
import org.springframework.orm.jpa.JpaVendorAdapter
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean

import jakarta.persistence.EntityManagerFactory
import javax.sql.DataSource

@Configuration
open class PicturesStoreConfiguration {

    @Bean
    @ConfigurationProperties("pictures.datasource")
    open fun picturesDataSourceProperties(): DataSourceProperties = DataSourceProperties()

    @Bean
    @ConfigurationProperties(prefix = "pictures.datasource.hikari")
    open fun picturesDataSource(@Qualifier("picturesDataSourceProperties") properties: DataSourceProperties): DataSource =
        properties.initializeDataSourceBuilder().build()

    @Bean
    open fun picturesEntityManagerFactory(
        @Qualifier("picturesDataSource") dataSource: DataSource,
        jpaVendorAdapter: JpaVendorAdapter,
        dbmsSpecifics: DbmsSpecifics,
        jmixModules: JmixModules,
        resources: Resources
    ): LocalContainerEntityManagerFactoryBean =
        JmixEntityManagerFactoryBean("pictures", dataSource, jpaVendorAdapter, dbmsSpecifics, jmixModules, resources)

    @Bean
    open fun picturesTransactionManager(
        @Qualifier("picturesEntityManagerFactory") entityManagerFactory: EntityManagerFactory
    ): JpaTransactionManager =
        JmixTransactionManager("pictures", entityManagerFactory)

    @Bean("picturesLiquibaseProperties")
    @ConfigurationProperties(prefix = "pictures.liquibase")
    open fun picturesLiquibaseProperties(): LiquibaseProperties = LiquibaseProperties()

    @Bean("picturesLiquibase")
    open fun picturesLiquibase(
        @Qualifier("picturesDataSource") dataSource: DataSource,
        @Qualifier("picturesLiquibaseProperties") liquibaseProperties: LiquibaseProperties
    ): SpringLiquibase =
        JmixLiquibaseCreator.create(dataSource, liquibaseProperties)
}
