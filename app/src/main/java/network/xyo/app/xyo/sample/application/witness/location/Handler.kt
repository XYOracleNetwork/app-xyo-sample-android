package network.xyo.app.xyo.sample.application.witness.location

import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.withContext
import network.xyo.app.xyo.sample.application.nodeUrl
import network.xyo.app.xyo.sample.application.witness.WitnessHandlerInterface
import network.xyo.app.xyo.sample.application.witness.WitnessResult
import network.xyo.client.XyoPanel
import network.xyo.client.address.XyoAccount
import network.xyo.client.payload.XyoPayload
import network.xyo.client.witness.location.info.XyoLocationWitness

class WitnessLocationHandler : WitnessHandlerInterface<XyoPayload?> {
    @RequiresApi(Build.VERSION_CODES.M)
    override suspend fun witness(context: Context): WitnessResult<XyoPayload?> {
        val panel = XyoPanel(context, arrayListOf(Pair(nodeUrl, XyoAccount())), listOf(
            XyoLocationWitness()
        ))
        return getLocation(panel)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @RequiresApi(Build.VERSION_CODES.M)
    private suspend fun getLocation(panel: XyoPanel): WitnessResult<XyoPayload?> {
        return withContext(Dispatchers.IO) {
            var locationPayload: XyoPayload? = null
            val errors: MutableList<Error> = mutableListOf()
            panel.let {
                it.reportAsyncQuery().apiResults?.forEach { action ->
                    if (action.response !== null) {
                        val payloads = action.response!!.payloads
                        if (payloads?.get(1)?.schema.equals("network.xyo.location.android")) {
                            locationPayload = payloads?.get(1)
                        }
                    }
                    if (action.errors !== null) {
                        action.errors!!.forEach { error ->
                            Log.e("xyoSampleApp", error.message ?: error.toString())
                            errors.add(error)
                        }
                    }
                }
            }
            if (errors.size > 0) return@withContext WitnessResult.Error(errors)
            return@withContext WitnessResult.Success(locationPayload)
        }
    }
}