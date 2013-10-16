package transmart



import grails.test.mixin.*
import org.junit.*

/**
 * See the API for {@link grails.test.mixin.web.GroovyPageUnitTestMixin} for usage instructions
 */
@TestFor(FormatTagLib)
class FormatTagLibSpec {

    void testLeaveAloneNonNumbers() {
        assert applyTemplate('<g:typeIndifferentNumberRound number="bla" />') == "bla"
    }

    void testRoundingOfNumbers() {
        assert applyTemplate('<g:typeIndifferentNumberRound number="0.12345" />') == "0.123"
    }
}
