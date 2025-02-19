package com.example.hrr_android.challengelist

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.hrr_android.R
import androidx.annotation.LayoutRes

class SpinnerFilterAdapter(
    context: Context,
    @LayoutRes private val selectedItemLayout: Int,
    @LayoutRes private val dropdownItemLayout: Int,
    private val items: List<String>
) : ArrayAdapter<String>(context, selectedItemLayout, items) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(selectedItemLayout, parent, false)

        val textView = view.findViewById<TextView>(R.id.tv_spinner_selected)
        textView.text = items[position]

        return view
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(dropdownItemLayout, parent, false)

        val textView = view.findViewById<TextView>(android.R.id.text1)
        textView.text = items[position]

        return view
    }
}
