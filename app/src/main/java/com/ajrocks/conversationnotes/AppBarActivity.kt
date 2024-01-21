package com.ajrocks.conversationnotes

import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout


class AppBarActivityBehaviour : CoordinatorLayout.Behavior<View> {

    private var dependentHeight: Int = 0
    private lateinit var dependencyView: LinearLayout
    private var lastDependencyYPosition = 0f
    private var isCoupled = true
    private val contractedHeight = 0


    constructor() : super() {
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
    }

    override fun layoutDependsOn(
        parent: CoordinatorLayout,
        child: View,
        dependency: View
    ): Boolean {
        Log.d("Dependency", "${dependency}, child- ${child}")
        if (dependency is LinearLayout) {
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
        if (isCoupled) {
            dependentHeight = dependency.height
            child.y = (dependency.y + dependency.height)
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

        val isScrollingDown = dy < 0

        val actionBarHeight = dependencyView.height
        val childY = child.y
        val childScrollPosition = child.scrollY

        if(childY == actionBarHeight.toFloat() && childScrollPosition > 0 && isScrollingDown){
            isCoupled = false
        }
        else if(childY == 0f && childScrollPosition > 0 && isScrollingDown){
            consumed[1] = dy
            isCoupled = true
            val newY = dependencyView.y - dy
            if(newY > 0){
                dependencyView.y = 0f
            }
            else if(newY < -1 * actionBarHeight){
                dependencyView.y = -1f * actionBarHeight
            }
            else{
                dependencyView.y = newY
            }
        }
        else if(childY == 0f){
            isCoupled = false
        }
        else if(childY <= actionBarHeight){
            consumed[1] = dy
            isCoupled = true
            val newY = dependencyView.y - dy
            if(newY > 0){
                dependencyView.y = 0f
            }
            else if(newY < -1 * actionBarHeight){
                dependencyView.y = -1f * actionBarHeight
            }
            else{
                dependencyView.y = newY
            }
        }
        else{
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

class AppBarLayout : ViewGroup {

    constructor(context: Context): super(context){
    }

    constructor(context: Context, attrs: AttributeSet): super(context, attrs){
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        TODO("Not yet implemented")
    }

}

class AppBarActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_app_bar)
    }
}