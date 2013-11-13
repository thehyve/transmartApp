package com.recomdata.transmart.data.export

import grails.test.mixin.*
import org.junit.Before
import org.junit.Test

import java.lang.reflect.Method

@TestFor(DataExportService)
class DataExportServiceTest {

    @Test
    void "it throws an exception when the request 'checkboxList' key contains an empty List"() {
        def message = shouldFail {
            service.exportData(['checkboxList':[]])
        }
        assert message == "Please select the data to Export."
    }

    @Test
    void "it throws an exception when the request lacks a jobTmpDirectory and the config too"() {
        grailsApplication.config.com.recomdata.transmart.data.export.jobTmpDirectory = null
        def message = shouldFail {
            service.exportData(['a':1])
        }
        assert message == "Job temp directory needs to be specified."
    }
}
