package com.company.library.app

import com.company.library.entity.*
import io.jmix.core.DataManager
import io.jmix.core.Resources
import io.jmix.core.security.Authenticated
import io.jmix.reports.ReportImportExport
import jakarta.persistence.LockModeType
import org.apache.commons.io.IOUtils
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.boot.context.event.ApplicationStartedEvent
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component
import java.io.IOException

const val REPORT_LOCATION: String = "com/company/library/reports/"

@Component
open class DemoDataInitializer(private val dataManager: DataManager, private val resources: Resources,
                               private val reportImportExport: ReportImportExport) {
    private val log: Logger = LoggerFactory.getLogger(DemoDataInitializer::class.java)

    @EventListener
    @Authenticated
    open fun onApplicationStarted(event: ApplicationStartedEvent) {
        if (dataManager.load(City::class.java).all().maxResults(1).list().size > 0) {
            return
        }
        val cities: List<City> = initCities()
        val publishers: List<Publisher> = initPublishers()
        val types: List<LiteratureType> = initLiteratureTypes()
        val departments: List<LibraryDepartment> = initLibraryDepartments()
        val authors: List<Author> = initAuthors()
        val books: List<Book> = initBooks(types)
        val publications: List<BookPublication> = initBookPublications(books, publishers, cities)
        val bookInstances: List<BookInstance> = initBookInstances(publications, departments)
        importReport()
    }

    private fun initCities(): List<City> {
        val list = ArrayList<City>()

        var city = dataManager.create(City::class.java)
        city.name = "Riverside"
        list.add(dataManager.save(city))

        city = dataManager.create(City::class.java)
        city.name = "New York"
        list.add(dataManager.save(city))

        city = dataManager.create(City::class.java)
        city.name = "Los Angeles"
        list.add(dataManager.save(city))

        return list
    }

    private fun initPublishers(): List<Publisher> {
        val list = java.util.ArrayList<Publisher>()

        var publisher = dataManager.create(Publisher::class.java)
        publisher.name = "Corner Street Publisher"
        list.add(dataManager.save(publisher))

        publisher = dataManager.create(Publisher::class.java)
        publisher.name = "Phaidon Press"
        list.add(dataManager.save(publisher))

        return list
    }

    private fun initLiteratureTypes(): List<LiteratureType> {
        val list = java.util.ArrayList<LiteratureType>()

        var literatureType = dataManager.create(LiteratureType::class.java)
        literatureType.name = "Economics"
        list.add(dataManager.save(literatureType))

        literatureType = dataManager.create(LiteratureType::class.java)
        literatureType.name = "Art"
        list.add(dataManager.save(literatureType))

        literatureType = dataManager.create(LiteratureType::class.java)
        literatureType.name = "Management Skills"
        list.add(dataManager.save(literatureType))

        return list
    }

    private fun initLibraryDepartments(): List<LibraryDepartment> {
        var libraryDepartment: LibraryDepartment
        val list = java.util.ArrayList<LibraryDepartment>()

        libraryDepartment = dataManager.create(LibraryDepartment::class.java)
        libraryDepartment.name = "Social Science"
        list.add(dataManager.save(libraryDepartment))

        libraryDepartment = dataManager.create(LibraryDepartment::class.java)
        libraryDepartment.name = "Economics"
        list.add(dataManager.save(libraryDepartment))

        libraryDepartment = dataManager.create(LibraryDepartment::class.java)
        libraryDepartment.name = "Language and Literature"
        list.add(dataManager.save(libraryDepartment))

        libraryDepartment = dataManager.create(LibraryDepartment::class.java)
        libraryDepartment.name = "Liberal Arts"
        list.add(dataManager.save(libraryDepartment))

        libraryDepartment = dataManager.create(LibraryDepartment::class.java)
        libraryDepartment.name = "Natural Science"
        list.add(dataManager.save(libraryDepartment))

        return list
    }

    private fun initAuthors(): List<Author> {
        val list = java.util.ArrayList<Author>()

        var author = dataManager.create(Author::class.java)
        author.firstName = "Peter"
        author.lastName = "Block"
        list.add(dataManager.save(author))

        author = dataManager.create(Author::class.java)
        author.firstName = "Curtis"
        author.lastName = "Ray"
        list.add(dataManager.save(author))

        author = dataManager.create(Author::class.java)
        author.firstName = "Ursula"
        author.lastName = "Runde"
        list.add(dataManager.save(author))

        return list
    }

    private fun initBooks(types: List<LiteratureType>): List<Book> {
        val list = java.util.ArrayList<Book>()

        var book = dataManager.create(Book::class.java)
        book.name = "The Lost Science of Compound Interest"
        book.literatureType = types[0]
        book.summary =
            "In a new and compelling story about money, science, art, evolution, discovery, creation, struggle, " +
                    "and ultimately, triumph, Curtis will take you on a life-changing journey through some of the most complicated " +
                    "mathematic money concepts by transforming them into an easily implemented path to unlimited wealth and prosperity"
        list.add(dataManager.save(book))

        book = dataManager.create(Book::class.java)
        book.name = "The 20th Century Art Book"
        book.literatureType = types[1]
        book.summary =
            "The 20th Century Art Book was hailed upon its release as an exciting celebration of the myriad forms " +
                    "assumed by art over the last century"
        list.add(dataManager.save(book))

        book = dataManager.create(Book::class.java)
        book.name = "Stewardship Choosing Service Over Self-Interest"
        book.literatureType = types[2]
        book.summary =
            "Stewardship was provocative, even revolutionary, when it was first published in 1993, and it remains " +
                    "every bit as relevant and radical today"
        list.add(dataManager.save(book))

        return list
    }

    private fun initBookPublications(
        books: List<Book>,
        publishers: List<Publisher>,
        cities: List<City>
    ): List<BookPublication> {
        val list = java.util.ArrayList<BookPublication>()

        var bookPublication = dataManager.create(BookPublication::class.java)
        bookPublication.year = 1999
        bookPublication.book = books[1]
        bookPublication.publisher = publishers[1]
        bookPublication.city = cities[1]

        list.add(dataManager.save(bookPublication))

        bookPublication = dataManager.create(BookPublication::class.java)
        bookPublication.year = 2013
        bookPublication.book = books[2]
        bookPublication.publisher = publishers[0]
        bookPublication.city = cities[1]

        list.add(dataManager.save(bookPublication))

        bookPublication = dataManager.create(BookPublication::class.java)
        bookPublication.year = 2015
        bookPublication.book = books[2]
        bookPublication.publisher = publishers[1]
        bookPublication.city = cities[0]

        list.add(dataManager.save(bookPublication))

        bookPublication = dataManager.create(BookPublication::class.java)
        bookPublication.year = 2020
        bookPublication.book = books[0]
        bookPublication.publisher = publishers[1]
        bookPublication.city = cities[2]

        list.add(dataManager.save(bookPublication))

        return list
    }

    private fun initBookInstances(
        bookPublications: List<BookPublication>,
        departments: List<LibraryDepartment>
    ): List<BookInstance> {
        val list = java.util.ArrayList<BookInstance>()

        var bookInstance = dataManager.create(BookInstance::class.java)
        bookInstance.isReference = true
        bookInstance.inventoryNumber = java.lang.Long.getLong("12584572132")
        bookInstance.bookCount = 20
        bookInstance.bookPublication = bookPublications[3]
        bookInstance.libraryDepartment = departments[1]

        list.add(dataManager.save(bookInstance))

        bookInstance = dataManager.create(BookInstance::class.java)
        bookInstance.isReference = false
        bookInstance.inventoryNumber = java.lang.Long.getLong("234526243562")
        bookInstance.bookCount = 100
        bookInstance.bookPublication = bookPublications[2]
        bookInstance.libraryDepartment = departments[0]

        list.add(dataManager.save(bookInstance))

        bookInstance = dataManager.create(BookInstance::class.java)
        bookInstance.isReference = false
        bookInstance.inventoryNumber = java.lang.Long.getLong("32541435134")
        bookInstance.bookCount = 85
        bookInstance.bookPublication = bookPublications[1]
        bookInstance.libraryDepartment = departments[0]

        list.add(dataManager.save(bookInstance))

        return list
    }

    private fun importReport() {
        val initFlags = dataManager.load(InitFlags::class.java)
            .id(1)
            .lockMode(LockModeType.PESSIMISTIC_WRITE)
            .optional()
            .orElseGet {
                val entity = dataManager.create(InitFlags::class.java)
                entity.id = 1
                entity
            }

        if (java.lang.Boolean.TRUE != initFlags.reportsInitialized) {
            importReport("Authors info.zip")
            importReport("Book Items location.zip")
            importReport("Book Record.zip")
            importReport("Neighbourhoods.zip")
            importReport("Publication details.zip")
            importReport("Publications grouped by types and books.zip")
            importReport("Publications by year.zip")
            importReport("Recently added book items.zip")

            initFlags.reportsInitialized = true
            dataManager.save(initFlags)
        }
    }

    private fun importReport(reportFileName: String) {
        val location: String = REPORT_LOCATION + reportFileName
        log.info("Initializing report from $location")
        try {
            resources.getResourceAsStream(location).use { stream ->
                if (stream != null) {
                    reportImportExport.importReports(IOUtils.toByteArray(stream))
                } else {
                    log.info("Not found: $location")
                }
            }
        } catch (e: IOException) {
            log.error("Unable to initialize reports", e)
        }
    }

}