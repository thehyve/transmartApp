package com.recomdata.transmart.data.export

import grails.test.mixin.*
import org.junit.Before
import org.junit.Test

import java.lang.reflect.Method

@TestFor(DataExportService)
class DataExportServiceTest {

    @Test
    void "it throws an exception when the request 'checkboxList' key contains an empty List"() {
        Method method = DataExportService.getDeclaredMethod("checkIfDataIsSelected", Map);
        method.setAccessible(true);
        shouldFail {
            method.invoke(service, ['checkboxList':[]]);
        }
    }

    @Test
    void "it throws an exception when the request lacks a jobTmpDirectory and the config too"() {
        grailsApplication.config.com.recomdata.transmart.data.export.jobTmpDirectory = null
        Method method = DataExportService.getDeclaredMethod("checkForJobTmpDirectory", Map);
        method.setAccessible(true);
        //NOTE: we should be able to catch the message but that does not happen for some reason
        def message = shouldFail {
            method.invoke(service, ['a':1]);
        }
    }
}
