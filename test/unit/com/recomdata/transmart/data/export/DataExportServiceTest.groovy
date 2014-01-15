package com.recomdata.transmart.data.export

import grails.test.mixin.*
import org.junit.Test

import java.lang.reflect.Method

@TestFor(DataExportService)
class DataExportServiceTest {

    @Test
    void "it throws an exception when the request 'checkboxList' key contains an empty List"() {
        Map emptyCheckboxList = ['checkboxList':[]]

        def message = shouldFail {
            service.exportData(emptyCheckboxList)
        }
        assert message == "Please select the data to Export."
    }

    @Test
    void "it throws an exception when the request lacks a jobTmpDirectory and the config too"() {
        grailsApplication.config.com.recomdata.transmart.data.export.jobTmpDirectory = null
        Map missingJobTmpDirectory = ['a':1]

        def message = shouldFail {
            service.exportData(missingJobTmpDirectory)
        }
        assert message == "Job temp directory needs to be specified."
    }
}
