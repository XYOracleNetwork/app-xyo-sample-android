package network.xyo.app.xyo.sample.application

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import com.paulvarry.jsonviewer.JsonViewer
import network.xyo.app.xyo.sample.application.databinding.BoundwitnessDetailBinding
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.ExperimentalCoroutinesApi
import network.xyo.client.node.client.QueryResponseWrapper
import org.json.JSONObject
import org.json.JSONTokener


/**
 * A fragment representing a single Item detail screen.
 * This fragment is either contained in a ItemListFragment
 * in two-pane mode (on larger screen devices) or self-contained
 * on handsets.
 */
@ExperimentalCoroutinesApi
class ItemDetailFragment : Fragment() {

    /**
     * The placeholder content this fragment is presenting.
     */
    private var item: QueryResponseWrapper? = null

    private var jsonViewer: JsonViewer? = null

    private var _binding: BoundwitnessDetailBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    @OptIn(ExperimentalStdlibApi::class)
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            if (it.containsKey(ARG_ITEM_HASH)) {
                val hash = it.getString(ARG_ITEM_HASH)
                item = XyoPanelWrapper.boundWitnesses.find { item -> item.bwHash?.toHexString() == hash }
            }
        }
    }

    @OptIn(ExperimentalStdlibApi::class)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = BoundwitnessDetailBinding.inflate(inflater, container, false)
        val rootView = binding.root

        val hash = item?.bwHash?.toHexString()?.substring(0, 10)
        binding.toolbarLayout?.title = hash

        jsonViewer = binding.jsonViewer
        // Show the placeholder content as text in a TextView.
        item?.let {
            jsonViewer?.setJson(objToJson(it))
        }

        return rootView
    }

    companion object {
        /**
         * The fragment argument representing the item ID that this fragment
         * represents.
         */
        const val ARG_ITEM_HASH = "item_hash"

        fun objToJson(obj: Any): JSONObject {
            val moshi = Moshi.Builder()
                .addLast(KotlinJsonAdapterFactory())
                .build()
            val adapter = moshi.adapter(obj.javaClass)
            val jsonString = adapter.toJson(obj)
            return JSONTokener(jsonString).nextValue() as JSONObject
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}