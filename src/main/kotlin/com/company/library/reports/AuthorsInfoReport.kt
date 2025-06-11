package com.company.library.reports

import io.jmix.reports.annotation.*
import io.jmix.reports.entity.DataSetType
import io.jmix.reports.entity.Orientation
import io.jmix.reports.entity.ReportOutputType

@ReportDef(
    name = "Authors Info",
    code = "authors-info",
    description = "Example report with Table template"
)
@BandDef(
    name = "Root",
    root = true,
    orientation = Orientation.HORIZONTAL
)
@BandDef(
    name = "Authors",
    parent = "Root",
    orientation = Orientation.HORIZONTAL,
    dataSets = [DataSetDef(
        type = DataSetType.JPQL,
        query = """
                    select
                    e.createdDate as "createdDate",
                    e.createdBy as "createdBy",
                    e.firstName as "firstName",
                    e.lastName as "lastName"
                    from Author e
                    """
    )]
)
@TemplateDef(
    code = "DEFAULT",
    outputType = ReportOutputType.TABLE,
    isDefault = true,
    table = TemplateTableDef(
        bands = [TableBandDef(
            bandName = "Authors",
            columns = [TableColumnDef(
                key = "createdDate",
                caption = "msg://com.company.library.entity/Author.createdDate"
            ), TableColumnDef(
                key = "createdBy",
                caption = "msg://com.company.library.entity/Author.createdBy"
            ), TableColumnDef(
                key = "firstName",
                caption = "msg://com.company.library.entity/Author.firstName"
            ), TableColumnDef(
                key = "lastName",
                caption = "msg://com.company.library.entity/Author.lastName"
            )]
        )]
    )
)
@ValueFormatDef(
    band = "Authors",
    field = "createdDate",
    format = "dd.MM.yyyy HH:mm:ss"
)
class AuthorsInfoReport