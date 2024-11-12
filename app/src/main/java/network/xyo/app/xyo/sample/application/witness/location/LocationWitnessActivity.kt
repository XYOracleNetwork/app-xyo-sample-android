package network.xyo.app.xyo.sample.application.witness.location

import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.widget.Button
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.vmadalin.easypermissions.EasyPermissions
import com.vmadalin.easypermissions.annotations.AfterPermissionGranted
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import network.xyo.app.xyo.sample.application.R
import network.xyo.client.payload.XyoPayload
import network.xyo.client.witness.location.info.LocationActivity

const val REQUEST_CODE_LOCATION = 1000

class LocationWitnessActivity : LocationActivity() {
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        // EasyPermissions handles the request result.
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    @AfterPermissionGranted(REQUEST_CODE_LOCATION)
    private fun methodRequiresTwoPermission() {
        if (EasyPermissions.hasPermissions(this, ACCESS_FINE_LOCATION)) {
            // Already have permission, do the thing
            // ...
        } else {
            // Do not have permissions, request them now
            EasyPermissions.requestPermissions(
                this,
                getString(R.string.permission_location_and_contacts_rationale_message),
                REQUEST_CODE_LOCATION,
                ACCESS_FINE_LOCATION
            )
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        methodRequiresTwoPermission()

        setContentView(R.layout.location_witness_activity)

        val button: Button = findViewById(R.id.witness_button)

        val context = this

        var isRunning = false
        button.setOnClickListener {
            if (isRunning) return@setOnClickListener
            isRunning = true

            // Extract Dispatchers.IO to helper
            CoroutineScope(Dispatchers.IO).launch {
                when (val result = WitnessLocationHandler().witness(context)) {
                    is WitnessResult.Success<XyoPayload?> -> {
                        Looper.prepare()
                        Toast.makeText(context, "Location saved to archivist! - ${result.data?.schema}", Toast.LENGTH_SHORT).show()
                    }
                    is WitnessResult.Error -> {
                        Looper.prepare()
                        Toast.makeText(context, "Location was NOT Saved to archivist! - ${result.exception.first().message}", Toast.LENGTH_SHORT).show()
                    }
                }
                isRunning = false
            }
        }
    }
}