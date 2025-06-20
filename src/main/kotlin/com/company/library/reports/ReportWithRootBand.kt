package com.company.library.reports

import io.jmix.reports.annotation.BandDef
import io.jmix.reports.entity.Orientation

@BandDef(
    name = "Root",
    root = true,
    orientation = Orientation.HORIZONTAL
)
interface ReportWithRootBand {
}