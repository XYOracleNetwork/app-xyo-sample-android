package network.xyo.app.xyo.sample.application.witness

import android.content.Context

interface WitnessHandlerInterface<out T> {
    suspend fun witness(context: Context): WitnessResult<T>
}