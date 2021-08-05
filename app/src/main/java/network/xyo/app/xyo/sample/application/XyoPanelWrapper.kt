package network.xyo.app.xyo.sample.application

import android.content.Context
import kotlinx.coroutines.runBlocking
import network.xyo.client.XyoBoundWitnessJson
import network.xyo.client.XyoPanel
import network.xyo.client.witness.system.info.XyoSystemInfoWitness

class XyoPanelWrapper {
    companion object {
        private var panel: XyoPanel? = null
        fun onAppLoad(context: Context) {
            panel = XyoPanel(context, "test", "https://beta.archivist.xyo.network", listOf(XyoSystemInfoWitness()))
            panel?.let {
                runBlocking {
                    boundWitnesses.add(it.reportAsync().bw)
                }
            }
        }
        var boundWitnesses = mutableListOf<XyoBoundWitnessJson>()
    }
}