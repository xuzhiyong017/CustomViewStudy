package com.sky.customviewstudy.activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.constraint.ConstraintSet
import com.sky.customviewstudy.R

class ConstrantAnimatorActivity : AppCompatActivity() {

    val  constraintSetStart = ConstraintSet()
    val  constraintSetEnd = ConstraintSet()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_constrant_animator)
    }
}