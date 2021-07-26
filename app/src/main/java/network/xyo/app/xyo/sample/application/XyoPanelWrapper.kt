package network.xyo.app.xyo.sample.application

import network.xyo.client.XyoBoundWitnessJson
import network.xyo.client.XyoPanel
import network.xyo.client.XyoSystemInfoWitness

class XyoPanelWrapper {
    companion object {
        private val panel = XyoPanel("test", "https://beta.archivist.xyo.network", listOf(XyoSystemInfoWitness()))
        suspend fun onAppLoad() {
            boundWitnesses.add(panel.report().bw)
        }
        var boundWitnesses = mutableListOf<XyoBoundWitnessJson>()
    }
}