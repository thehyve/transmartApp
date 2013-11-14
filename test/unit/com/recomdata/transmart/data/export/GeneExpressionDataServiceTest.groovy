package com.recomdata.transmart.data.export

import grails.test.mixin.TestFor
import org.junit.Before
import org.junit.Test

import java.lang.reflect.Method

@TestFor(GeneExpressionDataService)
class GeneExpressionDataServiceTest {

    def args

    @Before
    void setUp() {
        args = [
                List studyList,
                File studyDir,
                String fileName,
                String jobName,
                String resultInstanceId,
                boolean pivot,
                List gplIds,
                String pathway,
                String timepoint,
                String sampleTypes,
                String tissueTypes,
                Boolean splitAttributeColumn
        ]
    }

    @Test
    void "immediately returns 'false' when resultsInstanceId is null or empty"() {
        assertFalse service.getData(args)
    }

    @Test
    void "turns a comma seperated string into an IN clause"() {
        Method method = GeneExpressionDataService.getDeclaredMethod("constructInClause", String)
        method.setAccessible(true)
        assert method.invoke(service, "a,b") == "('a','b')"
        assert method.invoke(service, "a,b,") == "('a','b')"
        assert method.invoke(service, ",a,b") == "('a','b')"
        assert method.invoke(service, "a,,b") == "('a','b')"
    }
}
