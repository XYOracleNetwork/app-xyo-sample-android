package network.xyo.app.xyo.sample.application

import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import kotlinx.coroutines.ExperimentalCoroutinesApi
import network.xyo.client.witness.XyoPanel
import network.xyo.client.node.client.QueryResponseWrapper
import network.xyo.client.settings.XyoSdk
import network.xyo.client.witness.system.info.XyoSystemInfoWitness

const val nodeUrl = "https://beta.api.archivist.xyo.network/Archivist"

class XyoPanelWrapper {
    @ExperimentalCoroutinesApi
    @RequiresApi(Build.VERSION_CODES.M)
    companion object {
        val boundWitnesses = mutableListOf<QueryResponseWrapper>()
        suspend fun onAppLoad(context: Context) {
            val panel: XyoPanel?
            val account = XyoSdk.getInstance(context).getAccount(context.applicationContext)

            panel = XyoPanel(context, account, arrayListOf(Pair(nodeUrl, null)), listOf(XyoSystemInfoWitness()))

            panel.let {
                it.reportAsyncQuery().apiResults?.forEach{ action ->
                    if (action.response !== null) {
                        boundWitnesses.add(action.response!!)
                    }
                    if (action.errors !== null) {
                        action.errors!!.forEach { ex ->
                            Log.e("xyoSampleApp", ex.toString() + ex.stackTraceToString())
                        }
                    }
                }
            }
        }
    }
}