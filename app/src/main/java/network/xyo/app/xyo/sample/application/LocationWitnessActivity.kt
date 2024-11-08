package network.xyo.app.xyo.sample.application

import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.Manifest.permission.READ_CONTACTS
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.vmadalin.easypermissions.EasyPermissions
import com.vmadalin.easypermissions.annotations.AfterPermissionGranted
import network.xyo.client.witness.location.info.LocationActivity

const val REQUEST_CODE_LOCATION_AND_CONTACTS_PERMISSION = 12345

class LocationWitnessActivity : LocationActivity() {
//    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//
//        // EasyPermissions handles the request result.
//        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
//    }
//
//    @AfterPermissionGranted(REQUEST_CODE_LOCATION_AND_CONTACTS_PERMISSION)
//    private fun methodRequiresTwoPermission() {
//        if (EasyPermissions.hasPermissions(this, ACCESS_FINE_LOCATION, READ_CONTACTS)) {
//            // Already have permission, do the thing
//            // ...
//        } else {
//            // Do not have permissions, request them now
//            EasyPermissions.requestPermissions(
//                this,
//                getString(R.string.permission_location_and_contacts_rationale_message),
//                REQUEST_CODE_LOCATION_AND_CONTACTS_PERMISSION,
//                ACCESS_FINE_LOCATION, READ_CONTACTS
//            )
//        }
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        methodRequiresTwoPermission()

        setContentView(R.layout.location_witness_activity)

        val button: Button = findViewById(R.id.witness_button)


        button.setOnClickListener {
            // Display a Toast message when the button is clicked
            Toast.makeText(this, "Button Clicked!", Toast.LENGTH_SHORT).show()
        }
    }
}