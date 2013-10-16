package transmart

import java.util.regex.Matcher
import java.util.regex.Pattern

class FormatTagLib {
    def typeIndifferentNumberRound = { attrs ->
        if (!attrs.number) { raise }
        if (attrs.number ==~ /\d+\.\d+/) {
            attrs['maxFractionDigits'] = 3
            out << g.formatNumber(attrs)
        } else {
            out << attrs.number
        }
        //g.formatNumber(attrs)
    }
}
