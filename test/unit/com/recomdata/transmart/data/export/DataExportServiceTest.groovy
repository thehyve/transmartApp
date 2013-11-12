package com.recomdata.transmart.data.export

import grails.test.mixin.*
import org.junit.Before
import org.junit.Test

import java.lang.reflect.Method

@TestFor(DataExportService)
class DataExportServiceTest {

    @Before
    void setup() {
        println("time for lunch!")
    }

    @Test
    void "it throws an exception when the request 'checkboxList' key contains an empty List"() {
        Method method = DataExportService.getDeclaredMethod("checkRequestValidity", Map);
        method.setAccessible(true);
        shouldFail {
            method.invoke(service, ['checkboxList':[]]);
        }
    }
}
