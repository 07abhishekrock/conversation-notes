package com.ajrocks.conversationnotes.utils

import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.ajrocks.conversationnotes.R
import com.google.android.material.textview.MaterialTextView

/*
    you can have an enum of items.
    then based on enum you render different types of components
    your viewholder and adapter both have to be aware of those enums
*/

sealed class CustomViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    class DateView(view: View) : CustomViewHolder(view) {
        var dateText: MaterialTextView

        init {
            dateText = view.findViewById(R.id.date_row_view_text)
        }
    }

    class SingleConversation(view: View) : CustomViewHolder(view) {
        var avatar: ImageView
        var personName: MaterialTextView
        var latestMessageClipped: MaterialTextView

        init {
            avatar = view.findViewById(R.id.single_conversation_row_image)
            personName = view.findViewById(R.id.single_conversation_row_title)
            latestMessageClipped = view.findViewById(R.id.single_conversation_row_subTitle)
        }
    }

}

enum class CustomViewTypes {
    DATE_VIEW,
    SINGLE_CONVERSATION_VIEW
}

sealed class CustomView(val type: CustomViewTypes) {
    class DateView(
        var date: String
    ) : CustomView(type = CustomViewTypes.DATE_VIEW)

    class SingleConversation(
        var avatarImg: Drawable,
        var personName: String,
        var latestMessageClipped: String
    ) : CustomView(type = CustomViewTypes.SINGLE_CONVERSATION_VIEW)
}

class CustomRVAdapter(private var items: MutableList<CustomView>) : RecyclerView.Adapter<CustomViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        when (viewType) {
            CustomViewTypes.DATE_VIEW.ordinal -> {
                val view = LayoutInflater.from(parent.context).inflate(
                    R.layout.date_row_view,
                    parent,
                    false
                )
                return CustomViewHolder.DateView(view)
            }

            CustomViewTypes.SINGLE_CONVERSATION_VIEW.ordinal -> {
                val view = LayoutInflater.from(parent.context).inflate(
                    R.layout.single_conversation_row,
                    parent,
                    false
                )
                return CustomViewHolder.SingleConversation(view)
            }

            else -> {
                val view = LayoutInflater.from(parent.context).inflate(
                    R.layout.date_row_view,
                    parent,
                    false
                )
                return CustomViewHolder.DateView(view)
            }
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun getItemViewType(position: Int): Int {
        val itemAtIndex = items[position]
        return itemAtIndex.type.ordinal
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        val itemAtIndex = items[position]
        if (itemAtIndex.type == CustomViewTypes.DATE_VIEW) {
            (holder as CustomViewHolder.DateView).dateText.text = (itemAtIndex as CustomView
            .DateView).date
        } else if (itemAtIndex.type == CustomViewTypes.SINGLE_CONVERSATION_VIEW) {
            val castedItemAtIndex = itemAtIndex as CustomView.SingleConversation
            (holder as CustomViewHolder.SingleConversation).avatar.setImageDrawable(
                castedItemAtIndex
                    .avatarImg
            )
            holder.personName.text = castedItemAtIndex.personName
            holder.latestMessageClipped.text = castedItemAtIndex.latestMessageClipped
        }
    }

    fun addItemsAtIndex(indexToAdd: Int, vararg itemsToAdd: CustomView){
        items.addAll(indexToAdd, itemsToAdd.toMutableList())
        notifyItemRangeInserted(indexToAdd, itemsToAdd.size)
    }

    fun removeItemsFromIndex(indexToRemoveFrom: Int, countOfItemsToRemove: Int) {
        val snippedList = items.subList(indexToRemoveFrom, indexToRemoveFrom + countOfItemsToRemove)
        items.removeAll(snippedList)
        notifyItemRangeRemoved(indexToRemoveFrom, countOfItemsToRemove)
    }

}

