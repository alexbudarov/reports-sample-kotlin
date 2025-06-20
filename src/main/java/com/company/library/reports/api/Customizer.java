package com.company.library.reports.api;

public interface Customizer<T> {
    /**
     * Customize attributes of a constructed report sub-element.
     * @param element just constructed input parameter, band, data set, template or value format
     */
    void customize(T element);
}
