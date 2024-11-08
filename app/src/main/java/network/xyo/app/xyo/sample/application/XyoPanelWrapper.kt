package network.xyo.app.xyo.sample.application

import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import network.xyo.client.XyoPanel
import network.xyo.client.address.XyoAccount
import network.xyo.client.node.client.QueryResponseWrapper
import network.xyo.client.witness.location.info.XyoLocationWitness
import network.xyo.client.witness.system.info.XyoSystemInfoWitness

class XyoPanelWrapper {
    @ExperimentalCoroutinesApi
    @RequiresApi(Build.VERSION_CODES.M)
    companion object {
        fun onAppLoad(context: Context) {
            val panel: XyoPanel?
            val account = XyoAccount()

            panel = XyoPanel(context, arrayListOf(Pair("https://beta.api.archivist.xyo.network/Archivist", account)), listOf(XyoSystemInfoWitness(), XyoLocationWitness()))
            panel.let {
                runBlocking {
                    it.reportAsyncQuery().apiResults?.forEach{ action ->
                        if (action.response !== null) {
                            boundWitnesses.add(action.response!!)
                        }
                        if (action.errors !== null) {
                            action.errors!!.forEach {
                                Log.e("xyoSampleApp", it.message ?: it.toString())
                            }
                        }
                    }
                }
            }
        }
        val boundWitnesses = mutableListOf<QueryResponseWrapper>()
    }
}