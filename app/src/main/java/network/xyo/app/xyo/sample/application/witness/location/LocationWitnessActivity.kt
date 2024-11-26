package network.xyo.app.xyo.sample.application.witness.location

import android.Manifest
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import network.xyo.app.xyo.sample.application.Constants
import network.xyo.app.xyo.sample.application.nodeUrl
import network.xyo.client.boundwitness.XyoBoundWitnessBodyJson

import network.xyo.client.payload.XyoPayload
import network.xyo.client.witness.location.info.WitnessLocationHandler
import network.xyo.client.witness.types.WitnessResult

class LocationWitnessActivity : LocationActivity() {
    private val hashes = HashesViewModel()

    private val requestMultiplePermissionsLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        // Step 2: Handle the results for each permission
        permissions.entries.forEach { entry ->
            val permission = entry.key
            val isGranted = entry.value
            if (isGranted) {
                Toast.makeText(this, "$permission granted", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "$permission denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun methodRequiresTwoPermission() {
        val permissions = arrayOf(
            ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )

        val grantedPermissions = permissions.all { permission ->
            ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED
        }

        if (grantedPermissions) {
            Log.i("xyoSampleApp", "Permissions already granted")
        } else {
            requestMultiplePermissionsLauncher.launch(
                arrayOf(
                    ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }

    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        methodRequiresTwoPermission()

        setContent {
            WitnessLocationButton(witnessLocation = { handleLocationWitness(this, Constants.witnessDispatcher) })
            HashList(hashes)
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun handleLocationWitness(context: Context, dispatcher: CoroutineDispatcher) {
        CoroutineScope(dispatcher).launch {
            when (val result = WitnessLocationHandler().witness(context, arrayListOf(Pair(nodeUrl, null)))) {
                is WitnessResult.Success<Triple<XyoBoundWitnessBodyJson?, XyoPayload?, XyoPayload?>> -> {
                    handleResults(listOf(result.data.first, result.data.second, result.data.third), context)
                }
                is WitnessResult.Error -> {
                    CoroutineScope(Dispatchers.Main).launch {
                        Toast.makeText(context, "Failed to witness payload - ${result.exception.first().message}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun handleResults(result: List<XyoPayload?>, context: Context) {
        result.forEach {
            if (it !== null) {
                val hash = it.dataHash()
                hashes.addHash(hash)
                CoroutineScope(Dispatchers.Main).launch {
                    Toast.makeText(
                        context.applicationContext,
                        "Witnessed payload - $hash",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }
}