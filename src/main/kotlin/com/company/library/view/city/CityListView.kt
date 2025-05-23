package com.company.library.view.city

import com.company.library.entity.City
import com.company.library.view.main.MainView
import com.vaadin.flow.router.Route
import io.jmix.flowui.view.*


@Route(value = "cities", layout = MainView::class)
@ViewController(id = "City.list")
@ViewDescriptor(path = "city-list-view.xml")
@LookupComponent("citiesDataGrid")
@DialogMode(width = "64em")
class CityListView : StandardListView<City>() {
}