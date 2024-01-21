package com.ajrocks.conversationnotes

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet

class AppBarConstraintLayout: ConstraintLayout {

    private val correctArrangement = listOf(
        TestingSlots.LEFT,
        TestingSlots.TITLE,
        TestingSlots.RIGHT
    )

    fun returnMapOfSlotNameAndItemsOfThatSlot(){

        val mapOfSlotAndConstituents = mutableMapOf<TestingSlots, List<View>>()

        for(i in 0 until childCount){
            val child = getChildAt(i)
            val slotType = (child as TestingConstituent).slotType

            if(slotType == TestingSlots.LEFT){
                mapOfSlotAndConstituents.put(TestingSlots.LEFT, listOf(child))
            }
            else if(slotType == TestingSlots.TITLE){
                mapOfSlotAndConstituents.put(TestingSlots.LEFT, listOf(child))
            }
            else if(slotType == TestingSlots.RIGHT){
                val itemsAlreadyAtThisSlot = mapOfSlotAndConstituents[slotType]
                if(itemsAlreadyAtThisSlot != null){
                    val mutableListOfItems = itemsAlreadyAtThisSlot.toMutableList()
                    mutableListOfItems.add(child)
                    mapOfSlotAndConstituents[slotType] = mutableListOfItems.toList()
                }
            }
        }

    }

    @JvmOverloads
    constructor(
        context: Context,
        attributeSet: AttributeSet,
        defStyleInt: Int = 0
    ): super(context, attributeSet, defStyleInt) {
        Log.d("ConstraintLayout", "Initialized it here")
    }

    fun reconnectConstraints(){
       val constraintSet = ConstraintSet()

    }

    override fun onViewAdded(view: View?) {
        super.onViewAdded(view)
        Log.d("AllChildren", "$childCount")

    }


}