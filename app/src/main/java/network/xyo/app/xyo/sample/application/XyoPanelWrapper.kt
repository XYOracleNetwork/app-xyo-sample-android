package network.xyo.app.xyo.sample.application

import android.content.Context
import network.xyo.client.XyoBoundWitnessJson
import network.xyo.client.XyoPanel
import network.xyo.client.witness.system.info.XyoSystemInfoWitness

class XyoPanelWrapper {
    companion object {
        private var panel: XyoPanel? = null
        suspend fun onAppLoad(context: Context) {
            panel = XyoPanel(context, "test", "https://beta.archivist.xyo.network", listOf(XyoSystemInfoWitness()))
            panel?.let {
                boundWitnesses.add(it.report().bw)
            }
        }
        var boundWitnesses = mutableListOf<XyoBoundWitnessJson>()
    }
}