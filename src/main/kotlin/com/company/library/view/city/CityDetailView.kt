package com.company.library.view.city

import com.company.library.entity.City
import com.company.library.view.main.MainView
import com.vaadin.flow.router.Route
import io.jmix.flowui.view.*

@Route(value = "cities/:id", layout = MainView::class)
@ViewController(id = "City.detail")
@ViewDescriptor(path = "city-detail-view.xml")
@EditedEntityContainer("cityDc")
class CityDetailView : StandardDetailView<City>() {
}