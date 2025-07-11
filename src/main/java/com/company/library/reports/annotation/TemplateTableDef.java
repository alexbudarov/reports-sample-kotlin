package com.company.library.reports.annotation;

import io.jmix.reports.entity.table.TemplateTableDescription;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @see TemplateTableDescription
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface TemplateTableDef {

    TableBandDef[] bands();

}
