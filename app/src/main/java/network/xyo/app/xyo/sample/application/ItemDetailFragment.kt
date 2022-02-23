package network.xyo.app.xyo.sample.application

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import network.xyo.app.xyo.sample.application.databinding.FragmentItemDetailBinding
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import network.xyo.client.boundwitness.XyoBoundWitnessJson


/**
 * A fragment representing a single Item detail screen.
 * This fragment is either contained in a [ItemListFragment]
 * in two-pane mode (on larger screen devices) or self-contained
 * on handsets.
 */
class ItemDetailFragment : Fragment() {

    /**
     * The placeholder content this fragment is presenting.
     */
    private var item: XyoBoundWitnessJson? = null

    lateinit var itemDetailTextView: TextView

    private var _binding: FragmentItemDetailBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            if (it.containsKey(ARG_ITEM_HASH)) {
                val hash = it.getString(ARG_ITEM_HASH)
                item = XyoPanelWrapper.boundWitnesses.find { item -> item._hash == hash}
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentItemDetailBinding.inflate(inflater, container, false)
        val rootView = binding.root

        binding.toolbarLayout?.title = item?._hash?.substring(0, 5)

        itemDetailTextView = binding.itemDetail
        // Show the placeholder content as text in a TextView.
        item?.let {
            itemDetailTextView.text = objToJson(it)
        }

        return rootView
    }

    companion object {
        /**
         * The fragment argument representing the item ID that this fragment
         * represents.
         */
        const val ARG_ITEM_HASH = "item_hash"

        fun objToJson(obj: Any): String {
            val moshi = Moshi.Builder()
                .addLast(KotlinJsonAdapterFactory())
                .build()
            val adapter = moshi.adapter(obj.javaClass)
            return adapter.toJson(obj)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}