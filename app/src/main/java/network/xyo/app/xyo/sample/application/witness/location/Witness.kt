package network.xyo.app.xyo.sample.application.witness.location

import android.content.Context
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import network.xyo.app.xyo.sample.application.nodeUrl
import network.xyo.client.XyoPanel
import network.xyo.client.address.XyoAccount
import network.xyo.client.witness.location.info.XyoLocationWitness

class WitnessLocation {
    companion object {
        @OptIn(ExperimentalCoroutinesApi::class)
        @RequiresApi(Build.VERSION_CODES.M)
        fun witness(context: Context) {
            val panel = XyoPanel(context, arrayListOf(Pair(nodeUrl, XyoAccount())), listOf(
                XyoLocationWitness()
            ))

            CoroutineScope(Dispatchers.Main).launch {
                panel.let {
                    it.reportAsyncQuery().apiResults?.forEach { action ->
                        if (action.response !== null) {
                            val payloads = action.response!!.payloads
                            if (payloads?.get(1)?.schema.equals("network.xyo.location.android")) {
                                Toast.makeText(context, "Location saved to archivist!", Toast.LENGTH_SHORT).show()
                            }
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
    }
}