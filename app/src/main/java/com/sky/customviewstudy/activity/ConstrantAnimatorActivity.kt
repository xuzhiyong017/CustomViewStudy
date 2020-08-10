package com.sky.customviewstudy.activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.constraint.ConstraintSet
import android.transition.TransitionManager
import android.view.View
import com.sky.customviewstudy.R
import kotlinx.android.synthetic.main.activity_constrant_animator.*

class ConstrantAnimatorActivity : AppCompatActivity() {

    val  constraintSetStart = ConstraintSet()
    val  constraintSetEnd = ConstraintSet()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_constrant_animator)

        constraintSetEnd.clone(this,R.layout.activity_constrant_animator_action)
        constraintSetStart.clone(root_view)
    }

    var boolean = true

    fun changeAnimator(v:View?){
        TransitionManager.beginDelayedTransition(root_view)
        if(boolean){
            boolean = false
            constraintSetEnd.applyTo(root_view)
        }else{
            boolean = true
            constraintSetStart.applyTo(root_view)
        }
    }
}