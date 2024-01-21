package com.ajrocks.conversationnotes

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.AttributeSet
import android.util.Log
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.LinearLayout
import android.widget.PopupMenu
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.ViewCompat


class TestingAppBarBehaviour : CoordinatorLayout.Behavior<View> {

    private var dependentHeight: Int = 0
    private lateinit var dependencyView: LinearLayout
    private var lastDependencyYPosition = 0f
    private var isCoupled = true


    constructor(): super(){
    }

    constructor(context: Context, attrs: AttributeSet): super(context, attrs) {
    }

    override fun layoutDependsOn(
        parent: CoordinatorLayout,
        child: View,
        dependency: View
    ): Boolean {
        Log.d("Dependency", "${dependency}, child- ${child}")
        if(dependency is LinearLayout){
            dependencyView = dependency
        }
        return dependency is LinearLayout
    }

    override fun onDependentViewChanged(
        parent: CoordinatorLayout,
        child: View,
        dependency: View
    ): Boolean {
        Log.d("DependentViewChanged", "${dependency.height}")
        if(isCoupled){
            dependentHeight = dependency.height
            val maxYPosForChild = dependency.height
            child.y = Math.min(dependency.y.toInt() + dependency.height, maxYPosForChild).toFloat()
        }
        return super.onDependentViewChanged(parent, child, dependency)
    }

    override fun onStartNestedScroll(
        coordinatorLayout: CoordinatorLayout,
        child: View,
        directTargetChild: View,
        target: View,
        axes: Int,
        type: Int
    ): Boolean {
        return true
    }

    override fun onNestedPreScroll(
        coordinatorLayout: CoordinatorLayout,
        child: View,
        target: View,
        dx: Int,
        dy: Int,
        //output
        consumed: IntArray,
        type: Int
    ) {

        val isScrollingUp = dy >= 0
        val isScrollingDown = dy < 0

        val childPosition = child.y

        val actionBarY = dependencyView.y

        Log.d("OnNestedPreScroll", "$actionBarY $childPosition $isScrollingUp $isScrollingDown")

        if(childPosition.toInt() <= 0 && isScrollingUp){
           isCoupled = false
            child.y = 0f
        }
        else if(isScrollingDown && childPosition > 0 && actionBarY.toInt() == 0) {
           isCoupled = false
        }
        else if(isScrollingDown && childPosition > 0) {
            isCoupled = true
            //eat up the scroll
            consumed[1] = dy
            val newY = dependencyView.y - dy
            if(newY > 0){
                dependencyView.y = 0f
            }
            else{
                dependencyView.y = newY
            }
        }
        else if(actionBarY < 0){
            isCoupled = true
            //eat up the scroll
            consumed[1] = dy
            val newY = dependencyView.y - dy
            if(newY > 0){
                dependencyView.y = 0f
            }
            else{
                dependencyView.y = newY
            }
        }
        else if(isScrollingUp && actionBarY.toInt() == 0) {
            isCoupled = true
            consumed[1] = dy
            val newY = dependencyView.y - dy
            if(newY > 0){
                dependencyView.y = 0f
            }
            else{
                dependencyView.y = newY
            }
        }
        else {
            isCoupled = false
        }

        super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed, type)
    }

    override fun onNestedScroll(
        coordinatorLayout: CoordinatorLayout,
        child: View,
        target: View,
        dxConsumed: Int,
        dyConsumed: Int,
        dxUnconsumed: Int,
        dyUnconsumed: Int,
        type: Int,
        consumed: IntArray
    ) {
        Log.d("OnNestedScroll", "x-$dxConsumed, y-$dyConsumed, x1-$dxUnconsumed, y1-$dyUnconsumed")
        super.onNestedScroll(
            coordinatorLayout,
            child,
            target,
            dxConsumed,
            dyConsumed,
            dxUnconsumed,
            dyUnconsumed,
            type,
            consumed
        )
    }
}

class TestingAppBarActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_testing_app_bar)
        val p  = PopupMenu(this, null)
        val menu = p.menu
        menuInflater.inflate(R.menu.common_top_bar, null)

        Log.d("TestingAppBar", "${menu}")
    }
}