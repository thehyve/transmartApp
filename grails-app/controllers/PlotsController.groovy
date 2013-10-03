/**
 *
 */

class PlotsController {

    def grailsApplication
    def plotsDir

    def getPlotsDir() {
        if(!plotsDir) {
            plotsDir = new File(grailsApplication.config.org.transmart.plotsDir ?: 'plots')
            plotsDir.mkdirs()
        }
        plotsDir
    }

    def mplot = {
        plotsDir = null
        def imageFile = new File(getPlotsDir(), "mplot_${params.id}.png")
        if(imageFile.exists()) {
            response.contentType = 'image/png'
            response.outputStream << imageFile.newInputStream()
        } else {
            response.status = 404
        }
    }
}
